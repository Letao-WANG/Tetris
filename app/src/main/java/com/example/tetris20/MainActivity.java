package com.example.tetris20;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.GridView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private GridView mGridView;
    BlockAdapter blockAdapter;
    Timer timer;
    int movingBlocksNumber;
    Random random;
    private Button mButtonLeft;
    private Button mButtonRight;
    private Button mButtonRotate;
    private Button mButtonPause;

    final Handler myHandler = new Handler() {
        @Override
        //重写handleMessage方法,根据msg中what的值判断是否执行后续操作
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                update();
                blockAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGridView = findViewById(R.id.gridView);
        blockAdapter = new BlockAdapter(this);
        mGridView.setAdapter(blockAdapter);

        for (int i = 0; i < 15; i++)
            for (int j = 0; j < 10; j++) {
                blockAdapter.movingBlocks[i][j] = 0;
                blockAdapter.fixedBlocks[i][j] = 0;
            }

        ShapeStats.initShapes();
        newShape(0);

        mButtonLeft = findViewById(R.id.button_left);
        mButtonRight = findViewById(R.id.button_right);
        mButtonRotate = findViewById(R.id.button_rotate);
        mButtonPause = findViewById(R.id.button_pause);
        mButtonLeft.setOnClickListener(v -> {
            movingBlocksToLeft();
        });
        mButtonRight.setOnClickListener(v -> {
            movingBlocksToRight();
        });
        mButtonRotate.setOnClickListener(v -> {
            movingBlocksRotate();
        });
        mButtonPause.setOnClickListener(v -> {
            stopTimer();
        });

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                myHandler.sendEmptyMessage(0);
            }
        }, 0, 2000);
    }

    public void update() {
        // blocks go down
        blocksDown(blockAdapter.movingBlocks);

        // Check for collision
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 10; j++) {
                if ((blockAdapter.fixedBlocks[i][j] == 1 && blockAdapter.movingBlocks[i - 1][j] == 1)
                        || blockAdapter.movingBlocks[14][j] == 1) {
                    for (int x = 14; x >= 0; x--) {
                        for (int y = 0; y < 10; y++) {
                            blockAdapter.fixedBlocks[x][y] += blockAdapter.movingBlocks[x][y];
                            blockAdapter.movingBlocks[x][y] = 0;

                            // check if game over
                            if (blockAdapter.fixedBlocks[x][y] > 1) {
                                gameOver();
                            }
                        }
                    }
                    // new shape
                    random = new Random();
                    newShape(random.nextInt(5));
                    break;
                }
            }
        }

        // check if clear blocks
        boolean isClear = true;
        for (int j = 0; j < 10; j++) {
            if (blockAdapter.fixedBlocks[14][j] == 0) {
                isClear = false;
            }
        }
        if (isClear) {
            blocksDown(blockAdapter.fixedBlocks);
        }


        blockAdapter.addBlocks();
    }

    void blocksTransfer(int[][] newBlocks, int[][] oldBlocks) {
        for (int x = 0; x < 15; x++)
            for (int y = 0; y < 10; y++)
                newBlocks[x][y] = oldBlocks[x][y];
    }

    void newShape(int number) {
        blocksTransfer(blockAdapter.movingBlocks, ShapeStats.shapes.get(number));
        blockAdapter.centerX = 1;
        blockAdapter.centerY = 4;
        movingBlocksNumber = number;
    }

    void gameOver() {
        stopTimer();
    }

    void stopTimer(){
        if (timer != null) {
            timer.cancel();
            // need to set null
            timer = null;
        }
    }

    void movingBlocksToLeft() {

        // check for collision
        for (int x = 0; x < 15; x++) {
            for (int y = 1; y < 10; y++) {
                if (blockAdapter.fixedBlocks[x][y - 1] == 1 && blockAdapter.movingBlocks[x][y] == 1)
                    return;
                if (blockAdapter.movingBlocks[x][0] == 1)
                    return;
            }
        }

        // move
        for (int x = 0; x < 15; x++) {
            for (int y = 1; y < 10; y++) {
                blockAdapter.movingBlocks[x][y - 1] = blockAdapter.movingBlocks[x][y];
                blockAdapter.movingBlocks[x][y] = 0;
            }
        }
        blockAdapter.centerY--;
    }

    void movingBlocksToRight() {

        // check for collision
        for (int x = 0; x < 15; x++) {
            for (int y = 8; y >= 0; y--) {
                if (blockAdapter.fixedBlocks[x][y + 1] == 1 && blockAdapter.movingBlocks[x][y] == 1)
                    return;
                if (blockAdapter.movingBlocks[x][9] == 1)
                    return;
            }
        }

        // move
        for (int x = 0; x < 15; x++) {
            for (int y = 8; y >= 0; y--) {
                blockAdapter.movingBlocks[x][y + 1] = blockAdapter.movingBlocks[x][y];
                blockAdapter.movingBlocks[x][y] = 0;
            }
        }
        blockAdapter.centerY++;
    }

    void blocksDown(int[][] blocks) {
        for (int x = 14; x > 0; x--) {
            for (int y = 0; y < 10; y++) {
                blocks[x][y] = blocks[x - 1][y];
                blocks[x - 1][y] = 0;
            }
        }
        blockAdapter.centerX++;
    }

    void movingBlocksRotate() {
        blockAdapter.movingBlocks = rotate(blockAdapter.centerX, blockAdapter.centerY, blockAdapter.movingBlocks);
    }

    int[][] rotate(int centerX, int centerY, int[][] blocks) {


        if(centerX ==0 || centerX == blocks.length-1 || centerY == 0 || centerY == blocks[0].length-1){
            return blocks;
        }

        int l = 3;//length
        int[][] result = new int[blocks.length][blocks[0].length];

        // if blocks is I shape
        if(movingBlocksNumber == 4){
            if(blocks[centerX+1][centerY] == 1){
                result[centerX-1][centerY] = 1;
                result[centerX][centerY] = 1;
                result[centerX+1][centerY] = 1;
                result[centerX+2][centerY] = 1;
            }
            else if(blocks[centerX][centerY+1] == 1){
                result[centerX][centerY-1] = 1;
                result[centerX][centerY] = 1;
                result[centerX][centerY+1] = 1;
                result[centerX][centerY+2] = 1;
            }
        }

        // if blocks is O shape
        if(movingBlocksNumber == 2){
            return blocks;
        }

        for (int i = centerX - 1; i <= centerX + 1; i++) {
            for (int j = centerY - 1; j <= centerY + 1; j++) {
                if(blockAdapter.fixedBlocks[j-centerY+centerX][l-i+centerX+centerY-3] != 0){
                    return blocks;
                }
                result[j-centerY+centerX][l-i+centerX+centerY-3] = blocks[i][j];
//                result[j-(centerY-1)+centerX-1][l-1-i+(centerX-1)+centerY-1] = blocks[i][j];
            }
        }
        return result;
    }

}