package com.afa.fizicianu.fragments;

import android.app.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.afa.fizicianu.App;
import com.afa.fizicianu.MainActivity;
import com.afa.fizicianu.R;
import com.afa.fizicianu.database.SQLController;
import com.afa.fizicianu.models.Question;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Game;
import com.google.android.gms.games.Games;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;

import fr.tvbarthel.lib.blurdialogfragment.BlurDialogFragment;

/**
 * Created by filip on 4/26/2016.
 */
public class GameFragment extends Fragment implements View.OnClickListener {

    private TextView mQuestion;
    private int mLives;
    private int mScore;
    private SQLController sqlController;
    private ArrayList<Question> mQuestions;
    private Button R1;
    private Button R2;
    private Button R3;
    private Button R4;
    private ImageView life1;
    private ImageView life2;
    private ImageView life3;
    private int pos=0;
    private TextView scoreTV;
    private TextView timerTV;
    private GoogleApiClient mGoogleApiClient;

    CountDownTimer cntdwon;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_fragment,container,false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLives = 3;
        mScore = 0;

        sqlController = new SQLController(getActivity());
        sqlController.open();
        mQuestions = sqlController.getQuestions();
        Collections.shuffle(mQuestions);
        sqlController.close();

        R1 = (Button) view.findViewById(R.id.rasp1);
        R2 = (Button) view.findViewById(R.id.rasp2);
        R3 = (Button) view.findViewById(R.id.rasp3);
        R4 = (Button) view.findViewById(R.id.rasp4);
        mQuestion = (TextView) view.findViewById(R.id.questionTV);
        life1 = (ImageView) view.findViewById(R.id.life1);
        life2= (ImageView) view.findViewById(R.id.life2);
        life3 = (ImageView) view.findViewById(R.id.life3);
        scoreTV = (TextView) view.findViewById(R.id.scoretv);

        R1.setOnClickListener(this);
        R2.setOnClickListener(this);
        R3.setOnClickListener(this);
        R4.setOnClickListener(this);


        mGoogleApiClient = ((MainActivity)getActivity()).getmGoogleApiClient();
        timerTV = (TextView) getView().findViewById(R.id.timerTV);
        cntdwon = new CountDownTimer(20000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                timerTV.setText(String.valueOf(millisUntilFinished/1000));
                if(millisUntilFinished/1000>15) {
                    view.findViewById(R.id.sv).setVisibility(View.GONE);
                    view.findViewById(R.id.questionTV).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.timerTV).setVisibility(View.INVISIBLE);
                }
                else{
                    view.findViewById(R.id.questionTV).setVisibility(View.GONE);
                    view.findViewById(R.id.sv).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.timerTV).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFinish() {
                if(mQuestions.get(pos).getRc()==1)
                    next(2);
                else
                    next(1);
            }

        };

        show();

    }

    private void show(){

        cntdwon.cancel();
        if(App.isActivityVisible())
        cntdwon.start();
        //Achivement
        if(mGoogleApiClient.isConnected()) {
            if (mLives == 3) {
                switch (mScore) {
                    case 5:
                        Games.Achievements.unlock((mGoogleApiClient), "CgkI5Oyeu-UdEAIQAw");
                        Log.v("ts", "5");
                        break;
                    case 10:
                        Games.Achievements.unlock((mGoogleApiClient), "CgkI5Oyeu-UdEAIQBA");
                        break;
                    case 15:
                        Games.Achievements.unlock((mGoogleApiClient), "CgkI5Oyeu-UdEAIQBQ");

                }
            }
        }



        mQuestion.setText(mQuestions.get(pos).getIntrebare());
        R1.setText(mQuestions.get(pos).getR1());
        R2.setText(mQuestions.get(pos).getR2());
        R3.setText(mQuestions.get(pos).getR3());
        R4.setText(mQuestions.get(pos).getR4());
        scoreTV.setText(String.valueOf(mScore));

        Log.v("Help",String.valueOf(mQuestions.get(pos).getRc()));

        //Timer




    }


    @Override
    public void onClick(View v) {

        int answ=0;
        if(pos<mQuestions.size()&&mLives>0){

            if (v == R1) {
                answ = 1;
            } else if (v == R2) {
                answ = 2;
            } else if (v == R3) {
                answ = 3;
            } else if (v == R4) {
                answ = 4;
            }
        }
        next(answ);

    }

    public void next(int answ){
        int rc=mQuestions.get(pos).getRc();
        if(answ!=rc){
            mLives--;
            updateLives(mLives);
            if(mLives ==0 || pos == mQuestions.size()-1){
                Log.v("TEST","Final");
                finalJoc();
            }
            if(pos<mQuestions.size()-1){
                pos++;
                show();
            }
        }
        else if(pos<mQuestions.size()-1){
            mScore++;
            pos++;
            show();
        }
        else if(pos==mQuestions.size()-1){
            mScore++;
            finalJoc();
            Log.v("TEST","FINAL2");
        }


    }

    public void updateLives(int life){
        Resources resources = getResources();
        Drawable img = resources.getDrawable(R.drawable.ic_favorite_border_black_24dp);
        if(life==2)
            life1.setImageDrawable(img);
        if(life==1)
            life2.setImageDrawable(img);
        if(life==0)
            life3.setImageDrawable(img);
    }

    public void finalJoc(){

        cntdwon.cancel();


        SharedPreferences prefs = getActivity().getSharedPreferences(getString(R.string.sp), Context.MODE_PRIVATE);

        int scor = prefs.getInt("PersonalScore",0);
        if(mScore>scor) {
            SharedPreferences.Editor e = prefs.edit();
            e.putInt("PersonalScore", mScore);
            e.commit();
            ((MainActivity)getActivity()).updateHeader();
        }
        if(((MainActivity)getActivity()).getmGoogleApiClient().isConnected())
        Games.Leaderboards.submitScore(((MainActivity)getActivity()).getmGoogleApiClient(),"CgkI5Oyeu-UdEAIQAg",mScore);
        int highscore = prefs.getInt("PersonalScore",23);
        Log.v("TEST",String.valueOf(highscore));
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.final_dialog_layout);
        TextView currentScore = (TextView) dialog.findViewById(R.id.currentScoreTV);
        currentScore.setText(currentScore.getText()+String.valueOf(mScore));
        TextView highScore = (TextView) dialog.findViewById(R.id.highscoreTV);
        highScore.setText(highScore.getText()+String.valueOf(prefs.getInt("PersonalScore",mScore)));

        ImageButton newGame = (ImageButton) dialog.findViewById(R.id.NewGameButton);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = getActivity().getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                getActivity().overridePendingTransition(0,0);
                getActivity().finish();
                getActivity().overridePendingTransition(0,0);
                startActivity(intent);*/
                reset();
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                ((MainActivity)getActivity()).selectDrawerItem(((MainActivity)getActivity()).nvDrawer.getMenu().getItem(1));
                reset();
            }
        });
        int dividerId = dialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = dialog.findViewById(dividerId);
        if(divider!=null)
        divider.setBackgroundDrawable(null);

        dialog.show();
        this.onPause();

    }

    public void reset(){

        this.onResume();
        pos=0;
        mLives = 3;
        mScore = 0;
        Collections.shuffle(mQuestions);
        Resources resources = getResources();
        Drawable img = resources.getDrawable(R.drawable.ic_favorite_black_24dp);
        life1.setImageDrawable(img);
        life2.setImageDrawable(img);
        life3.setImageDrawable(img);

        show();



    }

    @Override
    public void onPause() {
        super.onPause();
        App.activityPaused();
        cntdwon.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        App.activityResumed();
        cntdwon.start();
    }
}
