package pradeep.restaurant;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Account extends AppCompatActivity {
    DatabaseHelper myDb;
    Button savebtn,deletebtn;

    EditText edtName,edtEmail,edtPass,edtAddr;

    String user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        myDb = new DatabaseHelper(this);

        edtName = (EditText) findViewById(R.id.edtNameAct);
        edtEmail = (EditText) findViewById(R.id.edtEmailAct);
        edtPass = (EditText) findViewById(R.id.edtPassAct);
        edtAddr = (EditText) findViewById(R.id.edtAddressAct);
        savebtn = (Button)findViewById(R.id.btnSaveAct);
        deletebtn = (Button)findViewById(R.id.btnDelAct);
        user_email = getIntent().getExtras().getString("userEmailIntent");

        getAccoutDetails();
    }

    public void getAccoutDetails(){

        Cursor res = myDb.getAccount(user_email);
        Cursor res_p = myDb.getAccountPass(user_email);
        if(res.getCount()==0){
            Toast.makeText(Account.this,"No account",Toast.LENGTH_LONG).show();
        }else{

           res.moveToFirst();
            res_p.moveToFirst();

                edtName.setText(res.getString(0));
                edtEmail.setText(res.getString(1));
                edtPass.setText(res_p.getString(0));
                edtAddr.setText(res.getString(2));

                edtName.setFocusable(false);
                edtEmail.setFocusable(false);
                edtPass.setFocusable(false);
                edtAddr.setFocusable(false);

        }
    }

    public void updateAccount(View v){

        Button button = (Button)v;

        if(((Button) v).getText().equals("EDIT")) {
            ((Button) v).setText("SAVE");
            edtName.setFocusableInTouchMode(true);
            edtPass.setFocusableInTouchMode(true);
            edtAddr.setFocusableInTouchMode(true);
        }
        else if(edtName.getText().toString().isEmpty() ||
                edtPass.getText().toString().isEmpty() || edtAddr.getText().toString().isEmpty()){
            Toast.makeText(Account.this,"Enter All Fields.",Toast.LENGTH_LONG).show();
        }
        else if(((Button) v).getText().equals("SAVE")){
                ((Button) v).setText("EDIT");
                edtName.setFocusable(false);
                edtPass.setFocusable(false);
                edtAddr.setFocusable(false);

            boolean isUpdated = myDb.updateUserDetails(edtName.getText().toString(),edtEmail.getText().toString(),
            edtPass.getText().toString(),edtAddr.getText().toString());

                if(isUpdated == true){
                    Toast.makeText(Account.this,"Account Updated",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(Account.this, "Not Updated", Toast.LENGTH_LONG).show();
                }
        }
    }
    public void alertDelete(){
        AlertDialog.Builder alertDel = new AlertDialog.Builder(this);
        alertDel.setMessage("Are you sure? Do you want to delete the Account?");
        alertDel.setCancelable(true);
        alertDel.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

     @Override
     public void onClick(DialogInterface dialog, int which) {
         Intent intent = new Intent(Account.this,LoginRegActivity.class);
         startActivity(intent);

            int deleteAct = myDb.deleteAccount(user_email);
             myDb.deleteTempUser();
            if(deleteAct > 0){
                Toast.makeText(Account.this,"Account Deleted",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(Account.this, "Not Deleted", Toast.LENGTH_LONG).show();
            }
        }
    });
    alertDel.setNegativeButton("No", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    });
    alertDel.create().show();
  }

    public void deleteAccount(View v){

      alertDelete();

    }
}
