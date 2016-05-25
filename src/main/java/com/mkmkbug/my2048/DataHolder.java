package com.mkmkbug.my2048;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;

public class DataHolder {

    public static final String DATA_KEY = "DATA";

    private DataList dataList;

    private MainActivity context;

    private SharedPreferences pref;

    public DataHolder(Context context) {
        this.context = (MainActivity) context;
        pref = context.getSharedPreferences(DATA_KEY, Context.MODE_PRIVATE);
        dataList = new DataList();
    }

    public void saveData(Card[][] cards, int score) {
        int[][] data = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                data[i][j] = cards[i][j].getNumber();
            }
        }
        dataList.add(data, score);
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
        context.updateScore(dataList.getScore() - context.getScore());
    }

    public void saveAll() {
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt("maxScore", context.getMaxScore());
        edit.apply();
    }


    private class DataList {
        private int index;
        private int dataLength;
        private int[][][] dataArray;
        private int[] numberArray;

        public DataList() {
            init();
        }

        private void init() {
            index = -1;
            // 这个值是回退限制次数
            dataLength = 50;
            dataArray = new int[dataLength][4][4];
            numberArray = new int[dataLength];
        }

        public void add(int[][] data, int score) {
            if (index >= dataLength - 1) index = -1;
            dataArray[++index] = data;
            numberArray[index] = score;
        }

        public int[][] pop() {
            if (index <= 0) {
                if (Arrays.deepEquals(dataArray[dataLength - 1], new int[4][4])) {
                    return null;
                } else {
                    index = dataLength;
                }
            }
            return dataArray[--index];
        }

        public int getScore() {
            return numberArray[index];
        }
    }
}
