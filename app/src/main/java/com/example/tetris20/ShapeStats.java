package com.example.tetris20;

import java.util.HashMap;

public class ShapeStats {
//    static int[][] shape = new int[10][10];
    static HashMap<Integer, int[][]> shapes = new HashMap<Integer, int[][]>();

    public static void initShapes() {

        int[][] lShape = voidShape();
        // L shape
        lShape[0][4] = 1;
        lShape[1][4] = 1;
        lShape[2][4] = 1;
        lShape[2][5] = 1;
        shapes.put(0, lShape);

        int[][] sShape = voidShape();
        // S shape
        sShape[0][3] = 1;
        sShape[1][3] = 1;
        sShape[1][4] = 1;
        sShape[2][4] = 1;
        shapes.put(1, sShape);

        int[][] oShape = voidShape();
        // O shape
        oShape[0][3] = 1;
        oShape[0][4] = 1;
        oShape[1][3] = 1;
        oShape[1][4] = 1;
        shapes.put(2, oShape);

        int[][] tShape = voidShape();
        // T shape
        tShape[0][4] = 1;
        tShape[1][4] = 1;
        tShape[1][3] = 1;
        tShape[2][4] = 1;
        shapes.put(3, tShape);


        int[][] iShape = voidShape();
        // I shape
        iShape[0][4] = 1;
        iShape[1][4] = 1;
        iShape[2][4] = 1;
        iShape[3][4] = 1;
        shapes.put(4, oShape);

    }

    static int[][] voidShape(){
        int[][] shape = new int[15][10];
        for (int[] line : shape)
            for(int n : line)
                n = 0;

        return shape;
    }
}
