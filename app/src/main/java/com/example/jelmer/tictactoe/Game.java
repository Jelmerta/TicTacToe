package com.example.jelmer.tictactoe;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/* Class for the game activity, used to play the game */
public class Game extends AppCompatActivity {

    private static final int CELLFREE = 0;
    private static final int X = 1;
    private static final int O = 2;
    private static final int XWINS = 1;
    private static final int OWINS = 2;
    private static final int TIE = 3;

    private Button[][] board;
    private int[][] cellState;
    private int cellsFilled;
    private boolean oTurn;
    private boolean aiTurn;

    /* Creates the content of the activity */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        setBoard();
    }

    /* Creates the new menu, everything will be the same as the title screen menu */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MainActivity.menuObject = menu;
        MainActivity.setMenuItemTitles();
        return true;
    }

    /* Tells what to do when a setting is changed. Currently, AI can be toggled. */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.ai_setting:
                MainActivity.toggleAI();
                MainActivity.setMenuItemTitles();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* Sets the initial state of the tictactoe board, as well as a restart button */
    private void setBoard() {
        board = new Button[3][3];
        cellState = new int[3][3];
        oTurn = true;
        aiTurn = false;
        cellsFilled = 0;

        board[0][0] = (Button) findViewById(R.id.button1);
        board[0][1] = (Button) findViewById(R.id.button2);
        board[0][2] = (Button) findViewById(R.id.button3);
        board[1][0] = (Button) findViewById(R.id.button4);
        board[1][1] = (Button) findViewById(R.id.button5);
        board[1][2] = (Button) findViewById(R.id.button6);
        board[2][0] = (Button) findViewById(R.id.button7);
        board[2][1] = (Button) findViewById(R.id.button8);
        board[2][2] = (Button) findViewById(R.id.button9);

        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                board[i][j].setOnClickListener(new ClickListenerCellState(i, j));
            }
        }

        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                cellState[i][j] = CELLFREE;
            }
        }

        Button restartButton = (Button) findViewById(R.id.restart_button);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartGame(v);
            }
        });
    }

    /* Sets the whole field to either enabled or disabled, useful for the end of the game and
        restarting the game. */
    private void enableClickableField(boolean enabler)
    {
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                board[i][j].setEnabled(enabler);
            }
        }
    }

    /* Resets the board and settings to the initial state */
    public void restartGame(View v) {
        aiTurn = false;
        oTurn = true;
        cellsFilled = 0;
        enableClickableField(true);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j].setText("");
                cellState[i][j] = CELLFREE;
            }
        }
    }

    /* Checks if a final state is reached (win/lose/tie), or if the game can continue */
    public int getState() {
        if (checkEndState(O)) {
            return OWINS;
        } else if (checkEndState(X)) {
            return XWINS;
        } else if (cellsFilled == 9) {
            return TIE;
        } else {
            return CELLFREE;
        }
    }

    /* Handles the state acquired from the function getState above, shows a toast when a final state is reached.
        When AI is enabled, a move is acquired and used.
     */
    public void handleState(int state) {
        switch(state) {
            case OWINS:
                Toast oWon = Toast.makeText(getApplicationContext(), "O won!",
                        Toast.LENGTH_LONG);
                oWon.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                oWon.show();
                enableClickableField(false);
                break;
            case XWINS:
                Toast xWon = Toast.makeText(getApplicationContext(), "X won!",
                        Toast.LENGTH_LONG);
                xWon.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                xWon.show();
                enableClickableField(false);
                break;
            case TIE:
                Toast tie = Toast.makeText(getApplicationContext(), "It's a tie!",
                        Toast.LENGTH_LONG);
                tie.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                tie.show();
                enableClickableField(false);
                break;
            case CELLFREE:
                if (MainActivity.aiTurnedOn && aiTurn) {
                    aiGenerateAndMove();
                    aiTurn = !aiTurn;
                    int newState = getState();
                    handleState(newState);
                    oTurn = !oTurn;
                }
                break;
        }
    }

    /* Checks for a winning state, called twice to check for both sides */
    public boolean checkEndState(int sideInt) {
        return ((cellState[0][0] == sideInt && cellState[0][1] == sideInt && cellState[0][2] == sideInt) ||
                (cellState[1][0] == sideInt && cellState[1][1] == sideInt && cellState[1][2] == sideInt) ||
                (cellState[2][0] == sideInt && cellState[2][1] == sideInt && cellState[2][2] == sideInt) ||
                (cellState[0][0] == sideInt && cellState[1][0] == sideInt && cellState[2][0] == sideInt) ||
                (cellState[0][1] == sideInt && cellState[1][1] == sideInt && cellState[2][1] == sideInt) ||
                (cellState[0][2] == sideInt && cellState[1][2] == sideInt && cellState[2][2] == sideInt) ||
                (cellState[0][0] == sideInt && cellState[1][1] == sideInt && cellState[2][2] == sideInt) ||
                (cellState[2][0] == sideInt && cellState[1][1] == sideInt && cellState[0][2] == sideInt));
    }

    /* Generates and uses a (currently random) move for the AI , could be split*/
    //TODO could split the generation and usage part
    public void aiGenerateAndMove() {
        ArrayList<Point> cellList = new ArrayList<>();
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                if(board[i][j].isEnabled())
                {
                    cellList.add(new Point(i, j));
                }
            }
        }

        Random indexGenerator = new Random();
        int index = 0;
        if(cellList.size() != 1) {
            index = indexGenerator.nextInt(cellList.size() - 1);
        }
        Point aiCell = cellList.get(index);

        board[aiCell.x][aiCell.y].setEnabled(false);
        if(oTurn) {
            board[aiCell.x][aiCell.y].setText("o");
            cellState[aiCell.x][aiCell.y] = O;
        } else {
            board[aiCell.x][aiCell.y].setText("x");
            cellState[aiCell.x][aiCell.y] = X;
        }
        cellsFilled++;
    }

    /* Each cell has an OnClickListener, this is implemented using this class */
    class ClickListenerCellState implements View.OnClickListener {
        int x;
        int y;

        public ClickListenerCellState(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /* When a cell is clicked and enabled, the cell will change and get a circle or cross */
        public void onClick(View view) {
            if (board[x][y].isEnabled()) {
                board[x][y].setEnabled(false);
                if(oTurn) {
                    oTurn = !oTurn;
                    board[x][y].setText("o");
                    cellState[x][y] = O;
                    cellsFilled++;
                } else {
                    oTurn = !oTurn;
                    board[x][y].setText("x");
                    cellState[x][y] = X;
                    cellsFilled++;
                }
                if(MainActivity.aiTurnedOn) {
                    aiTurn = !aiTurn;
                }
            }
            int state = getState();
            handleState(state);
        }
    }
}