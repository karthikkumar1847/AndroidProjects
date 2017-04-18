package harish.quiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    Button btlogin,btfp;
    EditText email,pass;
    TextView tvincrtstmt;
    DatabaseHelper quizDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        quizDb = new DatabaseHelper(this);

        btlogin=(Button)findViewById(R.id.btnloginL);
        btfp=(Button)findViewById(R.id.btnForgPassL);
        email=(EditText)findViewById(R.id.edemailL);
        pass=(EditText)findViewById(R.id.edtpassL);
        tvincrtstmt=(TextView)findViewById(R.id.tvIncrtStmtL);

        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String e = email.getText().toString();
                String p = pass.getText().toString();


                if (e.isEmpty() || p.isEmpty()) {
                    tvincrtstmt.setVisibility(View.VISIBLE);
                    tvincrtstmt.setText("Please fill all fields.");
                } else {
                    Cursor res = quizDb.getEmailPassword(e, p);
                    if (res.getCount() == 0) {
                        tvincrtstmt.setVisibility(View.VISIBLE);
                        tvincrtstmt.setText("Email and password did not match.");
                    } else {

                        res.moveToFirst() ;

                            Intent intent = new Intent(getApplicationContext(), Allmain.class);
                            intent.putExtra("userEmailFromLogin", e);
                            intent.putExtra("gkey","user");
                            startActivity(intent);
                            tvincrtstmt.setVisibility(View.INVISIBLE);


                    }
                }
            } });

        btfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Login.this,ResetPassword.class);
                startActivity(intent);
        }
    });
    }

}
