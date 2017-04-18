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
import android.widget.Toast;


public class ResetPassword extends AppCompatActivity {
    DatabaseHelper quizDb;
    EditText email,pass,cpass,ans;
    Spinner spiSctyQtn;
    String sqn = "";
    Button btnresetpwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        quizDb = new DatabaseHelper(this);

        email=(EditText)findViewById(R.id.edtEmailRP);
        pass=(EditText)findViewById(R.id.edtNewPassRP);
        cpass=(EditText)findViewById(R.id.edtCNewPassRP);
        ans=(EditText)findViewById(R.id.edtAnsRP);
        spiSctyQtn = (Spinner)findViewById(R.id.spinnerResetPassRP);
        btnresetpwd=(Button)findViewById(R.id.btnResetPassRP);


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

        btnresetpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String useremail = email.getText().toString();
                String userpass = pass.getText().toString();
                String usercpass = cpass.getText().toString();
                String userans = ans.getText().toString();

                if(useremail.isEmpty()||userpass.isEmpty()||usercpass.isEmpty()||sqn.isEmpty()||userans.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter all fields.", Toast.LENGTH_LONG).show();
                }
                else if(!userpass.equals(usercpass)){
                    Toast.makeText(getApplicationContext(), "Confirm password not same as password.", Toast.LENGTH_LONG).show();
                } else{

                    boolean isUpdated = quizDb.updatePassword(useremail,sqn,userans,userpass);

                    if (isUpdated == true) {

                        Toast.makeText(getApplicationContext(), "Password Updated.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ResetPassword.this, Login.class);
                        startActivity(intent);
                    } else{
                        Toast.makeText(ResetPassword.this, "Not Updated, Enter correct details.", Toast.LENGTH_LONG).show();

                    }}
            }
        });

    }
}
