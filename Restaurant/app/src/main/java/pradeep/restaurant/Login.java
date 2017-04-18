package pradeep.restaurant;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {
    DatabaseHelper myDb;
    Button btlogin;
    EditText email;
    EditText pass;
    TextView logincor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginn);
        myDb = new DatabaseHelper(this);

        btlogin=(Button)findViewById(R.id.btnLoginn);
        email=(EditText)findViewById(R.id.loginEmail);
        pass=(EditText)findViewById(R.id.loginPass);
        logincor=(TextView)findViewById(R.id.loginIncorrect);
        loginUser();
    }

    public void loginUser(){

        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String e = email.getText().toString();
                String p = pass.getText().toString();


        if(e.isEmpty()||p.isEmpty()){
            logincor.setVisibility(View.VISIBLE);
            logincor.setText("Enter all fields, then click login!!");
        }else{
                Cursor res = myDb.getEmailPass(e,p);
                if(res.getCount()==0){
                     logincor.setVisibility(View.VISIBLE);
                     logincor.setText("Entered Wrong Email ID or  Password ..!!");
              }  else{

                    res.moveToFirst();

                        Intent intent = new Intent(Login.this,AllRes.class);
                        intent.putExtra("userEmailFromLogin",e);
                        startActivity(intent);
                        logincor.setVisibility(View.INVISIBLE);
                }
            }}
        });

    }

    public void forgotPass(){


    }
}
