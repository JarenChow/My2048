package com.mkmkbug.my2048;

import android.content.Context;

public class DataHolder {

    private Context context;
    private DataList dataList;

    private DataHolder() {
    }

    public static void saveData(Context context,int[][] data){
        
    }


    private class DataList {
        private int index;
        private int[][][] dataArray;

        public DataList() {
            dataArray = new int[30][4][4];
        }

        public void add(int[][] data) {
            dataArray[index++] = data;
        }

        public int[][] pop() {
            return dataArray[--index];
        }
    }
}
