package pradeep.restaurant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class LoginRegActivity extends AppCompatActivity {
    Button btnlogn;
    Button btnreg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        btnlogn = (Button)findViewById(R.id.mainLogin);
        btnreg = (Button)findViewById(R.id.mainRegister);

        btnlogn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginRegActivity.this,Login.class);
                startActivity(intent);
            }
        });
        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginRegActivity.this,Register.class);
                startActivity(intent);
            }
        });


    }
    }

