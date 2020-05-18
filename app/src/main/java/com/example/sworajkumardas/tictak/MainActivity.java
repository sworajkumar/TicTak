package com.example.sworajkumardas.tictak;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[][] buttons = new Button[3][3];
    private boolean player1Turn = true;
    private int roundcount;
    private int player1point;
    private int player2point;
    private View localView;
    private Dialog mDialog;
    private TextView textViewplayer1;
    private TextView textViewplayer2;
    private Button newGame;
    private Button newGame1;
    private Button newGamea;
    private Button timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //textViewplayer1 = findViewById(R.id.text_view_p1);
        //textViewplayer2 = findViewById(R.id.text_view_p2);

         timer = (Button) findViewById( R.id.timer );
        new CountDownTimer(60000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                timer.setText(""+String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                timer.setText("done!");
                resetgame();
            }
        }.start();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonId = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonId, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }
        Button buttonreset = findViewById(R.id.reset);
        buttonreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        resetgame();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }
        if (player1Turn) {
            ((Button) v).setText("X");
        } else {
            ((Button) v).setText("O");
        }
        roundcount++;
        if (checkforwin()) {
            if (player1Turn) {
                player1wins();
            } else {
                player2wins();
            }
        } else if (roundcount == 9) {
            draw();
        } else {
            player1Turn = !player1Turn;
        }
    }

    private boolean checkforwin() {
        String[][] field = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (field[i][0].equals(field[i][1])
                        && field[i][0].equals(field[i][2])
                        && !field[i][0].equals("")) {
                    return true;
                }
            }
            for (int j = 0; j < 3; j++) {
                if (field[0][i].equals(field[1][i])
                        && field[0][i].equals(field[2][i])
                        && !field[0][i].equals("")) {
                    return true;
                }
            }
        }
        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")) {
            return true;
        }
        if (field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("")) {
            return true;
        }
        return false;
    }

    private void player1wins() {
       // player1point++;
        Toast.makeText(this, "player A win ! you are welcome.", Toast.LENGTH_LONG).show();
       // Intent intent=new Intent(MainActivity.this,WinActivity.class);
        //startActivity(intent);
        WinPopUp();
        //updatePointsText();
        resetBoard();
    }

    private void player2wins() {
       // player2point++;
        LossPopUp();
        Toast.makeText(this, "player B win ! and You loss the match..", Toast.LENGTH_LONG).show();
        //Intent intent=new Intent(MainActivity.this,LossActivity.class);
        //startActivity(intent);
        //updatePointsText();
        resetBoard();
    }

    private void draw() {
        resetBoard();
        DrawPopUp();
    }
    private void updatePointsText() {
        textViewplayer1.setText("player 1"+player1point);
        textViewplayer2.setText("player 1"+player2point);
    }
    private void resetBoard(){
        for (int i=0; i<3 ; i++){
            for(int j=0; j<3 ;j++){
                buttons[i][j].setText("");
            }
        }
        roundcount=0;
        player1Turn= true;
    }
    private void resetgame(){
        player1point=0;
        player2point=0;
        //updatePointsText();
        resetBoard();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("roundcount",roundcount);
        outState.putInt("player1point",player1point);
        outState.putInt("player2point",player2point);
        outState.putBoolean("player1Turn",player1Turn);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        roundcount=savedInstanceState.getInt("roundcount");
        player1point=savedInstanceState.getInt("player1point");
        player2point=savedInstanceState.getInt("player2point");
        player1Turn=savedInstanceState.getBoolean("player1turn");
    }

    private void WinPopUp() {
        localView = View.inflate(MainActivity.this, R.layout.activity_win, null);
        localView.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.zoom_in_enter));
        this.mDialog = new Dialog(MainActivity.this, R.style.NewDialog);
        this.mDialog.setContentView(localView);
        this.mDialog.setCancelable(true);
        this.mDialog.show();

        Window window = this.mDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER | Gravity.CENTER;
        window.setGravity(Gravity.CENTER);
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.dimAmount = 0.0f;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.windowAnimations = R.anim.slide_move;

        window.setAttributes(wlp);
        window.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        newGame=(Button)mDialog.findViewById(R.id.newGame);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

    }
    private void LossPopUp() {
        localView = View.inflate(MainActivity.this, R.layout.activity_loss, null);
        localView.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.zoom_in_enter));
        this.mDialog = new Dialog(MainActivity.this, R.style.NewDialog);
        this.mDialog.setContentView(localView);
        this.mDialog.setCancelable(true);
        this.mDialog.show();

        Window window = this.mDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER | Gravity.CENTER;
        window.setGravity(Gravity.CENTER);
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.dimAmount = 0.0f;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.windowAnimations = R.anim.slide_move;

        window.setAttributes(wlp);
        window.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        newGame1=(Button)mDialog.findViewById(R.id.newGame1);
        newGame1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

    }

    private void DrawPopUp() {
        localView = View.inflate(MainActivity.this, R.layout.draw_lsyout, null);
        localView.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.zoom_in_enter));
        this.mDialog = new Dialog(MainActivity.this, R.style.NewDialog);
        this.mDialog.setContentView(localView);
        this.mDialog.setCancelable(true);
        this.mDialog.show();

        Window window = this.mDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER | Gravity.CENTER;
        window.setGravity(Gravity.CENTER);
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.dimAmount = 0.0f;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.windowAnimations = R.anim.slide_move;

        window.setAttributes(wlp);
        window.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        newGamea=(Button)mDialog.findViewById(R.id.newGamea);
        newGamea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

    }

}

