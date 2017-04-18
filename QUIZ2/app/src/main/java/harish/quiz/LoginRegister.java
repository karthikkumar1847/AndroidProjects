package harish.quiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginRegister extends AppCompatActivity {
    Button btguest,btlogin,btreg;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_loginregister);


       /* mp = MediaPlayer.create(getApplicationContext(), R.raw.tananane);
        mp.start();*/

        btguest=(Button)findViewById(R.id.btnguestLR);
        btlogin=(Button)findViewById(R.id.btnLoginLR);
        btreg=(Button)findViewById(R.id.btnregLR);

        btguest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(LoginRegister.this,Allmain.class);
                intent.putExtra("gkey","guest");
                startActivity(intent);
               // mp.stop();
            }
        });


        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(LoginRegister.this,Login.class);
                startActivity(intent);
              //  mp.stop();
            }
        });

        btreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(LoginRegister.this,Register.class);
                startActivity(intent);
               // mp.stop();
            }
        });


        }

    @Override
    public void onBackPressed() {

        Toast.makeText(getApplicationContext(),"Please Login or Continue as Guest !!",Toast.LENGTH_SHORT).show();
    }
    }