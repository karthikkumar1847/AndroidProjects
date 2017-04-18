package pradeep.restaurant;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AllRes extends AppCompatActivity {
    Button btnMenu;
    Button btnAccount;
    Button btnOrders;
    Button btnCart;
    String user_email;
    DatabaseHelper myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all);
        myDb = new DatabaseHelper(this);

        btnMenu = (Button)findViewById(R.id.btnMenu);
        btnAccount = (Button)findViewById(R.id.btnAccount);
        btnOrders = (Button)findViewById(R.id.btnOrder);
        btnCart = (Button)findViewById(R.id.btnCart);
        user_email = getIntent().getExtras().getString("userEmailFromLogin");
        if(!user_email.isEmpty()) {
            myDb.deleteCartOrder();
            myDb.deleteTempUser();
            myDb.insertTempUser(user_email);
        }else
            user_email = myDb.getTempUser();

        account();
        rMenu();
        cart();
        orders();
    }

    public void account(){
        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AllRes.this,Account.class);
                intent.putExtra("userEmailIntent",user_email);
                startActivity(intent);
            }
        });
    }

    public void rMenu(){
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllRes.this,RMenu.class);
                startActivity(intent);
            }
        });

    }

    public void orders(){

        btnOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),OrderHistory.class);
                intent.putExtra("userEmailIntent",user_email);
                startActivity(intent);
            }
        });
    }

    public void cart(){
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AllRes.this,Cart.class);
                intent.putExtra("userEmailIntent",user_email);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onBackPressed() {
        alertExit();
    }

    public void alertExit(){
        AlertDialog.Builder alertDel = new AlertDialog.Builder(this);
        alertDel.setMessage("Are you sure? Do you want to Exit?");
        alertDel.setCancelable(true);
        alertDel.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              myDb.deleteTempUser();
              myDb.deleteCartOrder();
              AllRes.super.onBackPressed();
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
