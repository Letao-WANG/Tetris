package com.example.tetris20;

import java.util.HashMap;

public class ShapeStats {
//    static int[][] shape = new int[10][10];
    static HashMap<Integer, int[][]> shapes = new HashMap<Integer, int[][]>();

    public static void initShapes() {

        int[][] Lshape = voidShape();
        // L shape
        Lshape[0][4] = 1;
        Lshape[1][4] = 1;
        Lshape[2][4] = 1;
        Lshape[2][5] = 1;
        shapes.put(0, Lshape);

        int[][] shape = voidShape();
        // S shape
        shape[0][3] = 1;
        shape[1][3] = 1;
        shape[1][4] = 1;
        shape[2][4] = 1;
        shapes.put(1, shape);
    }

    static int[][] voidShape(){
        int[][] shape = new int[10][10];
        for (int[] line : shape)
            for(int n : line)
                n = 0;

        return shape;
    }
}
