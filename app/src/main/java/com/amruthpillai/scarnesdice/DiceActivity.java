package com.amruthpillai.scarnesdice;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.Random;

public class DiceActivity extends AppCompatActivity {

    private static int userOverallScore, userTurnScore, cpuOverallScore, cpuTurnScore;
    private static final int WIN_SCORE = 100;

    private static TextView tvScore, tvWinner;
    private static Button btnRoll, btnHold, btnReset;
    private static ImageView ivDiceFace;

    private static int diceFaces[] = {
            R.drawable.dice1,
            R.drawable.dice2,
            R.drawable.dice3,
            R.drawable.dice4,
            R.drawable.dice5,
            R.drawable.dice6
    };

    private static Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice);

        tvScore = (TextView) findViewById(R.id.tv_score);
        tvWinner = (TextView) findViewById(R.id.tv_winner);

        btnRoll = (Button) findViewById(R.id.btn_roll);
        btnHold = (Button) findViewById(R.id.btn_hold);
        btnReset = (Button) findViewById(R.id.btn_reset);

        ivDiceFace = (ImageView) findViewById(R.id.iv_dice_face);

        updateScoreLabel();

        btnRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rollDice();
            }
        });

        btnHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holdTurn();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivDiceFace.setImageResource(diceFaces[0]);
                userOverallScore = userTurnScore = cpuOverallScore = cpuTurnScore = 0;
                updateScoreLabel();
                tvWinner.setText("");
                btnRoll.setEnabled(true);
                btnHold.setEnabled(true);
            }
        });
    }

    public static void rollDice() {
        int diceValue = random.nextInt(6) + 1;

        if (diceValue == 1) {
            Log.d("Score", "You rolled 1");
            ivDiceFace.setImageResource(diceFaces[diceValue - 1]);
            userTurnScore = 0;
            updateScoreLabel();
            btnRoll.setEnabled(false);
            cpuTurn();
        } else {
            Log.d("Score", "You rolled " + diceValue);

            YoYo.with(Techniques.Shake)
                    .duration(250)
                    .playOn(ivDiceFace);

            ivDiceFace.setImageResource(diceFaces[diceValue - 1]);
            userTurnScore += diceValue;
            updateScoreLabel();
        }
    }

    public static void holdTurn() {
        userOverallScore += userTurnScore;
        userTurnScore = 0;
        updateScoreLabel();

        if (userOverallScore >= WIN_SCORE) {
            tvWinner.setText(R.string.user_winner);
            btnRoll.setEnabled(false);
            btnHold.setEnabled(false);
            return;
        }

        cpuTurn();
    }

    public static void cpuTurn() {
        btnRoll.setEnabled(false);
        btnHold.setEnabled(false);

        do {
            int diceValue = random.nextInt(6) + 1;

            if (diceValue == 1) {
                Log.d("Score", "CPU rolled 1");
                cpuTurnScore = 0;
                updateScoreLabel();
                btnRoll.setEnabled(true);
                btnHold.setEnabled(true);
            } else {
                cpuTurnScore += diceValue;
                Log.d("Score", "CPU rolled " + diceValue);
                updateScoreLabel();
            }
        } while (cpuTurnScore < 20);

        cpuOverallScore += cpuTurnScore;
        Log.d("Score", "CPU Overall Score: " + cpuOverallScore);

        cpuTurnScore = 0;
        updateScoreLabel();

        if (cpuOverallScore >= WIN_SCORE) {
            tvWinner.setText(R.string.cpu_winner);
            btnRoll.setEnabled(false);
            btnHold.setEnabled(false);
            return;
        }

        btnRoll.setEnabled(true);
        btnHold.setEnabled(true);
    }

    public static void updateScoreLabel() {
        String lbl_userOverallScore = "Your Overall Score: ";
        String lbl_userTurnScore = "Your Turn Score: ";
        String lbl_cpuOverallScore = "Computer's Overall Score: ";
        String lbl_cpuTurnScore = "Computer's Turn Score: ";

        String overallScoreLabel = lbl_userOverallScore + userOverallScore + "\n"
                + lbl_cpuOverallScore + cpuOverallScore;

        String turnScoreLabel = lbl_userTurnScore + userTurnScore + "\n"
                + lbl_cpuTurnScore + cpuTurnScore;

        String scoreLabel = overallScoreLabel + "\n" + turnScoreLabel;

        tvScore.setText(scoreLabel);
    }
}
