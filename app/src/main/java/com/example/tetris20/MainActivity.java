package com.example.tetris20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    int timeInterval=800;
    int movingBlocksNumber;
    Random random;
    int grade;
    private Button mButtonLeft;
    private Button mButtonRight;
    private Button mButtonRotate;
    private Button mButtonMoveDown;
    private Button mButtonPause;

    final Handler myHandler = new Handler() {
        @Override
        //重写handleMessage方法,根据msg中what的值判断是否执行后续操作
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                blocksDown(blockAdapter.movingBlocks);
                update();
                blockAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        grade = intent.getIntExtra("grade", 3);

        switch (grade) {
            case 1:
                timeInterval=1200;
                break;
            case 2:
                timeInterval=1000;
                break;
            case 3:
                timeInterval=800;
                break;
            case 4:
                timeInterval=600;
                break;
            case 5:
                timeInterval=400;
                break;
            default:
                break;
        }

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
        mButtonMoveDown = findViewById(R.id.button_move_down);
        mButtonPause = findViewById(R.id.button_pause);

        mButtonLeft.setOnClickListener(v -> {
            movingBlocksToLeft();
            update();
            blockAdapter.notifyDataSetChanged();
        });
        mButtonRight.setOnClickListener(v -> {
            movingBlocksToRight();
            update();
            blockAdapter.notifyDataSetChanged();
        });
        mButtonRotate.setOnClickListener(v -> {
            movingBlocksRotate();
            update();
            blockAdapter.notifyDataSetChanged();
        });
        mButtonMoveDown.setOnClickListener(v -> {
            moveDown();
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
        }, 0, timeInterval);
    }

    public void update() {
        // Check for collision
        for (int i = 1; i < 15; i++) {
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
                    int nextShape = random.nextInt(5);
                    newShape(nextShape);
                    break;
                }
            }
        }

        // check if clear blocks
        checkClear();

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
        // check for collision
        for (int x = 13; x >= 0; x--) {
            for (int y = 0; y < 10; y++) {
                if (blockAdapter.fixedBlocks[x+1][y] == 1 && blockAdapter.movingBlocks[x][y] == 1)
                    return;
                if (blockAdapter.movingBlocks[14][y] == 1)
                    return;
            }
        }

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
                result[centerX][centerY-1] = 1;
                result[centerX][centerY] = 1;
                result[centerX][centerY+1] = 1;
                result[centerX][centerY+2] = 1;
            }
            else if(blocks[centerX][centerY+1] == 1){
                result[centerX-1][centerY] = 1;
                result[centerX][centerY] = 1;
                result[centerX+1][centerY] = 1;
                result[centerX+2][centerY] = 1;
            }
            return result;
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

    void checkClear(){
        boolean isClear = false;
        do {
            for (int i = 0; i < 15; i++) {
                isClear = true;
                for (int j = 0; j < 10; j++) {
                    if (blockAdapter.fixedBlocks[i][j] == 0) {
                        isClear = false;
                    }
                }
                if (isClear) {
                    for (int x = i; x > 0; x--) {
                        for (int y = 0; y < 10; y++) {
                            blockAdapter.fixedBlocks[x][y] = blockAdapter.fixedBlocks[x - 1][y];
                        }
                    }
                }
            }
        }while(isClear);
    }

    void moveDown(){
        int[][] lastFixedBlocks = new int[15][10];
        for(int i=0;i<15; i++) {
            for (int j = 0; j < 10; j++) {
                lastFixedBlocks[i][j] = blockAdapter.fixedBlocks[i][j];
            }
        }

        while(BlocksEquals(lastFixedBlocks, blockAdapter.fixedBlocks)){
            blocksDown(blockAdapter.movingBlocks);
            update();
        }
    }

    boolean BlocksEquals(int[][] blocksA, int[][] blocksB){
        for(int i=0;i<blocksA.length; i++){
            for(int j=0; j<blocksA[0].length; j++){
                if(blocksA[i][j] != blocksB[i][j]){
                    return false;
                }
            }
        }
        return true;
    }

}