package com.mkmkbug.my2048;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Arrays;

public class DataHolder {

    private static final String DATA_KEY = "DATA";

    private DataList dataList;

    private MainActivity context;

    private SharedPreferences pref;

    public DataHolder(Context context) {
        this.context = (MainActivity) context;
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        dataList = new DataList();
    }

    public void saveData(Card[][] cards) {
        int[][] data = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                data[i][j] = cards[i][j].getNumber();
            }
        }
        dataList.add(data);
    }

    public void restoreData() {
        int[][] data = dataList.pop();
        if (data == null) return;
        Card[][] cards = context.getCards();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cards[i][j].setNumber(data[i][j]);
            }
        }
    }


    private class DataList {
        private int index;
        private int dataLength;
        private int[][][] dataArray;

        public DataList() {
            index = -1;
            // 这个值是回退限制次数
            dataLength = 50;
            dataArray = new int[dataLength][4][4];
        }

        public void add(int[][] data) {
            if (index == dataLength - 1) index = -1;
            dataArray[++index] = data;
        }

        public int[][] pop() {
            if (index == 0) {
                if (Arrays.deepEquals(dataArray[dataLength - 1], new int[4][4])) {
                    return null;
                } else {
                    index = dataLength;
                }
            }
            return dataArray[--index];
        }
    }
}
