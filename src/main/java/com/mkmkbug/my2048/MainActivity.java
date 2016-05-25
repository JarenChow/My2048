package com.mkmkbug.my2048;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    
    private static String TAG = "MainActivity";
    
    private GridLayout gameContainer;
    
    private int gameContainerLength;
    private Card[][] cards;
    private Random rand;

    private ImageButton details;

    private Button moveBack;

    private DataHolder dataHolder;

    private int score;

    public int getGameContainerLength() {
        return gameContainerLength;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void init() {
        gameContainerLength = getResources().getDisplayMetrics().widthPixels;
        gameContainer = (GridLayout) findViewById(R.id.game_container);
        assert gameContainer != null;
        gameContainer.setMinimumWidth(gameContainerLength);
        gameContainer.setMinimumHeight(gameContainerLength);

        cards = new Card[4][4];
        rand = new Random();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cards[i][j] = new Card(this);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.setMargins(10, 10, 10, 10);
                gameContainer.addView(cards[i][j], params);
            }
        }

        gameContainer.setOnTouchListener(new MyOnTouchListener());
        createRandomCard();
        createRandomCard();


        dataHolder = new DataHolder(this);
        dataHolder.saveData(cards);


        details = (ImageButton) findViewById(R.id.details);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        moveBack = (Button) findViewById(R.id.move_back);
        moveBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataHolder.restoreData();
            }
        });
    }
    
    private void createRandomCard() {
        while (true) {
            int i = rand.nextInt(4);
            int j = rand.nextInt(4);
            if (cards[i][j].getNumber() == 0) {
                cards[i][j].setNumber(rand.nextInt(10) < 1 ? 4 : 2);
                break;
            }
        }
    }


    public Card[][] getCards() {
        return cards;
    }

    private boolean isGameOver() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (cards[i][j].getNumber() == 0)
                    return false;
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                if (cards[i][j].getNumber() == cards[i][j + 1].getNumber()
                        || cards[j][i].getNumber() == cards[j + 1][i].getNumber()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    
    private class MyOnTouchListener implements View.OnTouchListener {
        private float startX, startY, offsetX, offsetY;
        private boolean isMoved;
        
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = event.getX();
                    startY = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    offsetX = event.getX() - startX;
                    offsetY = event.getY() - startY;
                    if (Math.abs(offsetX) > Math.abs(offsetY)) {
                        if (offsetX > 5) {
                            moveRight();
                        } else if (offsetX < -5) {
                            moveLeft();
                        }
                    } else {
                        if (offsetY > 5) {
                            moveDown();
                        } else if (offsetY < -5) {
                            moveUp();
                        }
                    }
                    break;
            }
            return true;
        }

        private void moveRight() {
            isMoved = false;
            for (int i = 0; i < 4; i++) {
                int zeroCount = 0;
                for (int j = 0; j < 4; j++) {
                    if (cards[i][j].getNumber() == 0) {
                        zeroCount++;
                    }
                }
                for (int j = zeroCount; j > 0; j--) {
                    for (int k = 3; k > 0; k--) {
                        moveCard(i, k, 0, -1);
                    }
                }
                for (int j = 3; j > 0; j--) {
                    combineCard(i, j, 0, -1);
                }
                for (int j = 3; j > 0; j--) {
                    moveCard(i, j, 0, -1);
                }
            }
            if (isMoved) {
                createRandomCard();
                dataHolder.saveData(cards);
            }
        }

        private void moveLeft() {
            isMoved = false;
            for (int i = 0; i < 4; i++) {
                int zeroCount = 0;
                for (int j = 0; j < 4; j++) {
                    if (cards[i][j].getNumber() == 0) {
                        zeroCount++;
                    }
                }
                for (int j = 0; j < zeroCount; j++) {
                    for (int k = 0; k < 3; k++) {
                        // 如果前者等于 0, 后者不等于 0, 则前移一位
                        // 最多需要执行三次循环, 如 0 0 0 2
                        moveCard(i, k, 0, 1);
                    }
                }
                for (int j = 0; j < 3; j++) {
                    // 如果数值相等, 则合并, 并且后者置 0
                    combineCard(i, j, 0, 1);
                }
                for (int j = 0; j < 3; j++) {
                    moveCard(i, j, 0, 1);
                }
            }
            if (isMoved) {
                createRandomCard();
                dataHolder.saveData(cards);
            }
        }

        private void moveDown() {
            isMoved = false;
            for (int i = 0; i < 4; i++) {
                int zeroCount = 0;
                for (int j = 0; j < 4; j++) {
                    if (cards[j][i].getNumber() == 0) {
                        zeroCount++;
                    }
                }
                for (int j = zeroCount; j > 0; j--) {
                    for (int k = 3; k > 0; k--) {
                        moveCard(k, i, -1, 0);
                    }
                }
                for (int j = 3; j > 0; j--) {
                    combineCard(j, i, -1, 0);
                }
                for (int j = 3; j > 0; j--) {
                    moveCard(j, i, -1, 0);
                }
            }
            if (isMoved) {
                createRandomCard();
                dataHolder.saveData(cards);
            }
        }

        private void moveUp() {
            isMoved = false;
            for (int i = 0; i < 4; i++) {
                int zeroCount = 0;
                for (int j = 0; j < 4; j++) {
                    if (cards[j][i].getNumber() == 0) {
                        zeroCount++;
                    }
                }
                for (int j = 0; j < zeroCount; j++) {
                    for (int k = 0; k < 3; k++) {
                        moveCard(k, i, 1, 0);
                    }
                }
                for (int j = 0; j < 3; j++) {
                    combineCard(j, i, 1, 0);
                }
                for (int j = 0; j < 3; j++) {
                    moveCard(j, i, 1, 0);
                }
            }
            if (isMoved) {
                createRandomCard();
                dataHolder.saveData(cards);
            }
        }

        private void moveCard(int i, int j, int s1, int s2) {
            if (cards[i][j].getNumber() == 0 && cards[i + s1][j + s2].getNumber() != 0) {
                cards[i][j].setNumber(cards[i + s1][j + s2].getNumber());
                cards[i + s1][j + s2].setNumber(0);
                isMoved = true;
            }
        }

        private void combineCard(int i, int j, int s1, int s2) {
            if (cards[i][j].getNumber() == cards[i + s1][j + s2].getNumber()
                    && cards[i][j].getNumber() != 0) {
                cards[i][j].setNumber(cards[i][j].getNumber() * 2);
                cards[i + s1][j + s2].setNumber(0);
                isMoved = true;
            }
        }
    }
    
}
