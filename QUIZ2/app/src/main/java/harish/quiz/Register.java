package harish.quiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.security.SecureRandom;

public class Register extends AppCompatActivity {
    DatabaseHelper quizDb;
    EditText name,email,pass,cpass,ans;
    Spinner spiSctyQtn;
    String sqn = "";
    Button btreg;
    TextView tvincrtstmt;
    String AB;
    SecureRandom rnd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        quizDb = new DatabaseHelper(this);

        name=(EditText) findViewById(R.id.EdtNameR);
        email=(EditText)findViewById(R.id.EdtEmailR);
        pass=(EditText)findViewById(R.id.EdtPassR);
        cpass=(EditText)findViewById(R.id.EdtCpassR);
        ans=(EditText)findViewById(R.id.EdtAnsR);
        spiSctyQtn = (Spinner)findViewById(R.id.spinnerSecurityR);
        btreg=(Button)findViewById(R.id.btnRegR);
        tvincrtstmt = (TextView)findViewById(R.id.tvIncrtStmtR) ;

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.security_questions,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiSctyQtn.setAdapter(adapter);
        spiSctyQtn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sqn = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                sqn = "";
            }
        });

        btreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = name.getText().toString();
                String useremail = email.getText().toString();
                String userpass = pass.getText().toString();
                String usercpass = cpass.getText().toString();
                String userans = ans.getText().toString();

                if(username.isEmpty()||useremail.isEmpty()||userpass.isEmpty()||usercpass.isEmpty()||sqn.isEmpty()||userans.isEmpty()) {
                    tvincrtstmt.setVisibility(View.VISIBLE);
                    tvincrtstmt.setText("Please fill all fields.");
                }
                    else if(!userpass.equals(usercpass)){
                        tvincrtstmt.setVisibility(View.VISIBLE);
                        tvincrtstmt.setText("Password and Confirm password not matched.");
                    } else{

                    tvincrtstmt.setVisibility(View.INVISIBLE);

                    String pwdkey = randomString(16);
                   quizDb.insertRecPwd(useremail,pwdkey,userpass);


                          boolean isInserted = quizDb.insertRecord(username,useremail,pwdkey,sqn,userans);

                        if (isInserted == true ) {
                        tvincrtstmt.setVisibility(View.INVISIBLE);
                        Toast.makeText(Register.this, "Successfully Registered", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Register.this, Login.class);
                        startActivity(intent);
                        } else{
                        Toast.makeText(Register.this, "Not Registered.", Toast.LENGTH_LONG).show();
                        tvincrtstmt.setVisibility(View.VISIBLE);
                        tvincrtstmt.setText("Already Registered. Continue Login!");
                        }

                }
            }
        });

         AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
         rnd = new SecureRandom();


    }
    public String randomString( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }
}
