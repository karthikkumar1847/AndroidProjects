package harish.quiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class Result extends AppCompatActivity {
    Button btnmainmenu;
    int receivedRightCount,receivedWrongCount,questionsAttempt;
    RatingBar rb;
    int scorePer;
    String user;
    float score;
    TextView rCountTv,wCountTv,questionsAtmptTv,scoreperTv;
    MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        mp = MediaPlayer.create(getApplicationContext(), R.raw.claps);
        mp.start();

        user = getIntent().getExtras().getString("gkey");
        btnmainmenu=(Button)findViewById(R.id.BtnMainMenuResult);
        btnmainmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                Intent i=new Intent(Result.this,Allmain.class);
                i.putExtra("gkey",user);
                startActivity(i);
            }
        });
        rCountTv=(TextView)findViewById(R.id.tvcorrectResult);
        wCountTv=(TextView)findViewById(R.id.TvWrongResult);
        questionsAtmptTv=(TextView)findViewById(R.id.TvQuestionAtmptResult);
        scoreperTv=(TextView)findViewById(R.id.tvScoreResult);


        rb=(RatingBar)findViewById(R.id.ratingBar);
        rb.setStepSize(0.1f);

        receivedRightCount = getIntent().getExtras().getInt("rCount");
        receivedWrongCount=getIntent().getExtras().getInt("wCount");
        questionsAttempt=receivedRightCount + receivedWrongCount;

        score= ((float) receivedRightCount/questionsAttempt);

        rb.setRating(score*5);
        rb.setClickable(false);

        scorePer=(int)(score*100) ;
        scoreperTv.setText("Scored "+scorePer +"%");

        rCountTv.setText(""+receivedRightCount);
        wCountTv.setText(""+receivedWrongCount);
        questionsAtmptTv.setText("Questions Attempted :"+questionsAttempt);

    }
    @Override
    public void onBackPressed() {
        mp.stop();
        AlertDialog.Builder alertDel = new AlertDialog.Builder(this);
        alertDel.setMessage("Do you want to Exit Quiz ?");
        alertDel.setCancelable(true);
        alertDel.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

              Intent i = new Intent(getApplicationContext(),LoginRegister.class);
                startActivity(i);
            }
        });
        alertDel.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDel.create().show();
    }
}
