package com.example.tetris20;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.GridView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private GridView mGridView;
    BlockAdapter blockAdapter;
    Timer timer;
    private Button mButtonLeft;
    private Button mButtonRight;

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

        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++) {
                blockAdapter.movingBlocks[i][j] = 0;
                blockAdapter.fixedBlocks[i][j] = 0;
            }

        ShapeStats.initShapes();
        blocksTransfer(blockAdapter.movingBlocks, ShapeStats.shapes.get(0));

        mButtonLeft = findViewById(R.id.button_left);
        mButtonRight = findViewById(R.id.button_right);
        mButtonLeft.setOnClickListener(v -> {
            movingBlocksToLeft();
        });
        mButtonRight.setOnClickListener(v -> {
            movingBlocksToRight();
        });

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                myHandler.sendEmptyMessage(0);
            }
        }, 0, 1000);
    }

    public void update() {
        // blocks go down
        blocksDown(blockAdapter.movingBlocks);

        // Check for collision
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if ((blockAdapter.fixedBlocks[i][j] == 1 && blockAdapter.movingBlocks[i - 1][j] == 1)
                        || blockAdapter.movingBlocks[9][j] == 1) {
                    for (int x = 9; x >= 0; x--) {
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
                    newShape(0);
                    break;
                }
            }
        }

        // check if clear blocks
        boolean isClear = true;
        for (int j = 0; j < 10; j++) {
            if (blockAdapter.fixedBlocks[9][j] == 0) {
                isClear = false;
            }
        }
        if (isClear) {
            blocksDown(blockAdapter.fixedBlocks);
        }


        blockAdapter.addBlocks();
    }

    void blocksTransfer(int[][] newBlocks, int[][] oldBlocks) {
        for (int x = 0; x < 10; x++)
            for (int y = 0; y < 10; y++)
                newBlocks[x][y] = oldBlocks[x][y];
    }

    void newShape(int number) {
        blocksTransfer(blockAdapter.movingBlocks, ShapeStats.shapes.get(number));
    }

    void gameOver() {
        if (timer != null) {
            timer.cancel();
            // need to set null
            timer = null;
        }
    }

    void movingBlocksToLeft() {

        // check for collision
        for (int x = 0; x < 10; x++) {
            for (int y = 1; y < 10; y++) {
                if (blockAdapter.fixedBlocks[x][y - 1] == 1 && blockAdapter.movingBlocks[x][y] == 1 )
                    return;
                if(blockAdapter.movingBlocks[x][0] == 1)
                    return;
            }
        }

        // move
        for (int x = 0; x < 10; x++) {
            for (int y = 1; y < 10; y++) {
                blockAdapter.movingBlocks[x][y - 1] = blockAdapter.movingBlocks[x][y];
                blockAdapter.movingBlocks[x][y] = 0;
            }
        }
    }

    void movingBlocksToRight() {

        // check for collision
        for (int x = 0; x < 10; x++) {
            for (int y = 8; y >= 0; y--) {
                if (blockAdapter.fixedBlocks[x][y + 1] == 1 && blockAdapter.movingBlocks[x][y] == 1 )
                    return;
                if(blockAdapter.movingBlocks[x][9] == 1)
                    return;
            }
        }

        // move
        for (int x = 0; x < 10; x++) {
            for (int y = 8; y >= 0; y--) {
                blockAdapter.movingBlocks[x][y + 1] = blockAdapter.movingBlocks[x][y];
                blockAdapter.movingBlocks[x][y] = 0;
            }
        }
    }

    void blocksDown(int[][] blocks) {
        for (int x = 9; x > 0; x--) {
            for (int y = 0; y < 10; y++) {
                blocks[x][y] = blocks[x - 1][y];
                blocks[x - 1][y] = 0;
            }
        }
    }
}