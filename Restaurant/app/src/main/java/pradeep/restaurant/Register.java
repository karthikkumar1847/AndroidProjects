package pradeep.restaurant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.CryptoPrimitive;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class Register extends AppCompatActivity {
    DatabaseHelper myDb;

    Button btreg;
    EditText name;
    EditText email;
    EditText pass,cpass;
    EditText address;
    TextView tvincor;
    String keyValues;
    SecureRandom secRndKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis);
        myDb = new DatabaseHelper(this);

        btreg = (Button) findViewById(R.id.btnRegister);
        email = (EditText) findViewById(R.id.regEmail);
        pass = (EditText) findViewById(R.id.regPassword);
        cpass = (EditText) findViewById(R.id.etCinfirmPwdR);
        name = (EditText) findViewById(R.id.regName);
        address = (EditText) findViewById(R.id.regAddressEdt);
        tvincor = (TextView) findViewById(R.id.regnIcorrectStmt);

        addData();
    }

    public void addData() {
        btreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyValues = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
                secRndKey = new SecureRandom();

                String username = name.getText().toString();
                String useremail = email.getText().toString();
                String userpass = pass.getText().toString();
                String userCpass = cpass.getText().toString();
                String useraddr = address.getText().toString();

                if(username.isEmpty()||useremail.isEmpty()||userpass.isEmpty()||useraddr.isEmpty()){
                    tvincor.setVisibility(View.VISIBLE);
                    tvincor.setText("All fields are Mandatory.");

                }else if(!userpass.equals(userCpass)){

                    tvincor.setVisibility(View.VISIBLE);
                    tvincor.setText("Password doesnot match Confirm Password.");
                } else{
                    tvincor.setVisibility(View.INVISIBLE);

                    String pwdkey = randomKey(32);
                    myDb.insertData_Pass(useremail,pwdkey,userpass);

                    boolean isInserted = myDb.insertData(username,useremail,pwdkey,useraddr);

                    if (isInserted) {
                        tvincor.setVisibility(View.INVISIBLE);
                            Toast.makeText(Register.this, "Successfully Registered", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Register.this, Login.class);
                            startActivity(intent);
                     } else{
                            Toast.makeText(Register.this, "Not Registered", Toast.LENGTH_LONG).show();
                        tvincor.setVisibility(View.VISIBLE);
                        tvincor.setText("Email ID already exists!");
            }}
         }
        });
    }
    public String randomKey( int length ){
        StringBuilder sb = new StringBuilder( length );
        for( int i = 0; i < length; i++ )
            sb.append( keyValues.charAt( secRndKey.nextInt(keyValues.length()) ) );
        return sb.toString();
    }
}

