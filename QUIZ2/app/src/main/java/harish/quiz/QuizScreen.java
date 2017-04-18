package harish.quiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class QuizScreen extends AppCompatActivity {

    Button btnNext,btnFinish, btnopt1, btnopt2, btnopt3, btnopt4;
    TextView tvdisp, tvQtn, tvCrtAns, tvWrngAns;
    String receivedCategory;
    String qtn, ans;
    int rCount = 0, wCount = 0;
    boolean isAnswered = false;
    int nOFQtns = 0;
    DatabaseHelper myDb;
    Cursor res;
    static String user;
    MediaPlayer mp ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_screen);
        mp = MediaPlayer.create(getApplicationContext(), R.raw.playback);
        mp.start();
        myDb = new DatabaseHelper(this);

        btnopt1 = (Button) findViewById(R.id.BtnOpt1QuizScreen);
        btnopt2 = (Button) findViewById(R.id.BtnOpt2QuizScreen);
        btnopt3 = (Button) findViewById(R.id.BtnOpt3QuizScreen);
        btnopt4 = (Button) findViewById(R.id.BtnOpt4QuizScreen);
        btnNext = (Button) findViewById(R.id.btnNextQS);
        btnFinish = (Button) findViewById(R.id.TvFinishQuResult);

        tvdisp = (TextView) findViewById(R.id.TvCatDispQuizScreen);
        tvQtn = (TextView) findViewById(R.id.TvQtnQuizScreen);
        tvCrtAns = (TextView) findViewById(R.id.tvCorrectQS);
        tvWrngAns = (TextView) findViewById(R.id.tvWrongQS);

        receivedCategory = getIntent().getExtras().getString("ckey");
        user = getIntent().getExtras().getString("gkey");
        tvdisp.setText(receivedCategory);

        if(user.equals("guest")){
            res = myDb.getQtnAnsGuest(receivedCategory);
            Toast.makeText(getApplicationContext(),"Only 5 Questions for Guest!!",Toast.LENGTH_LONG).show();
        }
        else res = myDb.getQtnAns(receivedCategory);


        nOFQtns = res.getCount();
        if (nOFQtns == 0) {

            Toast.makeText(QuizScreen.this, "No Questions Found!", Toast.LENGTH_LONG).show();
        } else {
            res.moveToFirst();
            getQtnDetails();
        }

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(nOFQtns==0){
                  mp.stop();
                  Intent i = new Intent(QuizScreen.this, Result.class);
                  i.putExtra("rCount", rCount);
                  i.putExtra("wCount",wCount);
                  i.putExtra("gkey",user);
                  startActivity(i);
              }else
                  getFinishAlert();
            }
        });


    }


    public void getQtnDetails() {
            tvQtn.setText(res.getString(1));
            btnopt1.setText(res.getString(2));
            btnopt2.setText(res.getString(3));
            btnopt3.setText(res.getString(4));
            btnopt4.setText(res.getString(5));
            ans = res.getString(6);

            isAnswered = false;
            btnopt1.setClickable(true);
            btnopt2.setClickable(true);
            btnopt3.setClickable(true);
            btnopt4.setClickable(true);
            btnopt1.setTextColor(Color.WHITE);
            btnopt2.setTextColor(Color.WHITE);
            btnopt3.setTextColor(Color.WHITE);
            btnopt4.setTextColor(Color.WHITE);
            btnopt1.setBackgroundColor(Color.DKGRAY);
            btnopt2.setBackgroundColor(Color.DKGRAY);
            btnopt3.setBackgroundColor(Color.DKGRAY);
            btnopt4.setBackgroundColor(Color.DKGRAY);
            nOFQtns--;
    }

    public void optionChoosed(View v) {
        Button button = (Button) v;

        String choosenAns = button.getText().toString();

        isAnswered = true;
        if (choosenAns.equals(ans)) {
            rCount++;
            btnopt1.setTextColor(Color.BLACK);
            btnopt2.setTextColor(Color.BLACK);
            btnopt3.setTextColor(Color.BLACK);
            btnopt4.setTextColor(Color.BLACK);

            btnopt1.setBackgroundColor(Color.WHITE);
            btnopt2.setBackgroundColor(Color.WHITE);
            btnopt3.setBackgroundColor(Color.WHITE);
            btnopt4.setBackgroundColor(Color.WHITE);

            button.setBackgroundColor(Color.GREEN);
            button.setTextColor(Color.WHITE);


        } else {

            wCount++;
            btnopt1.setBackgroundColor(Color.WHITE);
            btnopt2.setBackgroundColor(Color.WHITE);
            btnopt3.setBackgroundColor(Color.WHITE);
            btnopt4.setBackgroundColor(Color.WHITE);

            btnopt1.setTextColor(Color.BLACK);
            btnopt2.setTextColor(Color.BLACK);
            btnopt3.setTextColor(Color.BLACK);
            btnopt4.setTextColor(Color.BLACK);

            button.setTextColor(Color.WHITE);
            button.setBackgroundColor(Color.RED);
            getCorrectAnsColor();
        }
        btnopt1.setClickable(false);
        btnopt2.setClickable(false);
        btnopt3.setClickable(false);
        btnopt4.setClickable(false);
    }
    public void getCorrectAnsColor(){


        String b1=btnopt1.getText().toString();
        String b2=btnopt2.getText().toString();
        String b3=btnopt3.getText().toString();
        String b4=btnopt4.getText().toString();
       if(b1.equals(ans)){
           btnopt1.setBackgroundColor(Color.GREEN);
           btnopt1.setTextColor(Color.WHITE);
       }else if(b2.equals(ans)){
           btnopt2.setBackgroundColor(Color.GREEN);
           btnopt2.setTextColor(Color.WHITE);
       }else if(b3.equals(ans)){
           btnopt3.setBackgroundColor(Color.GREEN);
           btnopt3.setTextColor(Color.WHITE);
       }else if(b4.equals(ans)){
           btnopt4.setBackgroundColor(Color.GREEN);
           btnopt4.setTextColor(Color.WHITE);
       }
    }

    public void getNextQtn(View v){

        if(isAnswered == true){
            if(res.moveToNext()){
                getQtnDetails();
            }
            else{
                if(user.equals("guest"))
                    getRegAlert();
                else
                    Toast.makeText(getApplicationContext(),"No Questions",Toast.LENGTH_SHORT).show();
            }
        }else
            Toast.makeText(getApplicationContext(),"Choose Option",Toast.LENGTH_SHORT).show();

    }

    public void getRegAlert(){
        mp.pause();
        AlertDialog.Builder alertDel = new AlertDialog.Builder(this);
        alertDel.setMessage("Please Register For More Questions !!");
        alertDel.setCancelable(false);
        alertDel.setPositiveButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mp.stop();
                Intent i = new Intent(getApplicationContext(),Register.class);
                startActivity(i);
            }
        });
        alertDel.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mp.start();
            }
        });
        alertDel.create().show();
    }
    public void getFinishAlert(){
        mp.pause();
        AlertDialog.Builder alertDel = new AlertDialog.Builder(this);
        alertDel.setMessage("Do you want to Finish Quiz!");
        alertDel.setCancelable(false);
        alertDel.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mp.stop();
                Intent i = new Intent(QuizScreen.this, Result.class);
                i.putExtra("rCount", rCount);
                i.putExtra("wCount",wCount);
                i.putExtra("gkey",user);
                startActivity(i);
            }
        });
        alertDel.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mp.start();
            }
        });
        alertDel.create().show();
    }
    @Override
    public void onBackPressed() {
        mp.pause();
        AlertDialog.Builder alertDel = new AlertDialog.Builder(this);
        alertDel.setMessage("Do you want to Quit Quiz ?");
        alertDel.setCancelable(false);
        alertDel.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mp.stop();
                QuizScreen.super.onBackPressed();
            }
        });
        alertDel.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mp.start();
            }
        });
        alertDel.create().show();
    }


}
