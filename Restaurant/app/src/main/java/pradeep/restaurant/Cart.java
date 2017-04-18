package pradeep.restaurant;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Cart extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    static String[] strItems;
    ListView listv;
    TextView tvSubT,tvDelivery,tvTotal;
    String itemName;
    int qnty;
    int itemPrice;
    double subTotalPrice=0;
    double totalPrice=0;
    String user_email;
    DatabaseHelper myDb;
    ArrayList<String> itemAl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        myDb = new DatabaseHelper(this);

        tvSubT = (TextView)findViewById(R.id.tvSubTotalC);
        tvDelivery = (TextView)findViewById(R.id.tvDeliveryC);
        tvTotal = (TextView)findViewById(R.id.tvTotalC);
        listv=(ListView)findViewById(R.id.listViewCart);


        getCartOrders();
        callAdapter();

    }
    public  void  callAdapter(){
        adapter=new ArrayAdapter<String>(this,R.layout.list_veiw_resource,R.id.textview,strItems);

        listv.setAdapter(adapter);
        listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                if(!strItems[0].equals("No Items in the cart.")){
                    alertDeleteItem(i);
                }
            }
        });
    }
    public void getCartOrders(){
        Cursor res = myDb.getCartDetails();
        if(res.getCount()>0){
            strItems = new String[res.getCount()];
            itemAl = new ArrayList<String>();
            int i = 0;
            itemName = new String();
            qnty=0;
            itemPrice=0;
            subTotalPrice=0;
            totalPrice=0;
            while(res.moveToNext()){

                itemName = res.getString(0);
                itemAl.add(itemName);
                qnty = res.getInt(1);
                itemPrice = res.getInt(2);
                subTotalPrice= subTotalPrice+res.getDouble(3);

                if(itemPrice<10)
                    strItems[i] =  qnty + " x " + " $0" + itemPrice + ".00 ,  "+itemName;
                else
                    strItems[i] =  qnty + " x " + " $" + itemPrice + ".00 ,  "+itemName;
                i++;

            }
            tvSubT.setText("$"+subTotalPrice);
            tvDelivery.setText("$10.0");
            totalPrice = subTotalPrice + 10;
            tvTotal.setText("$"+totalPrice);

        }else {
            strItems = new String[1];
            strItems[0] = "No Items in the cart.";
            tvSubT.setText("");
            tvDelivery.setText("");
            tvTotal.setText("");
        }
    }
    public void placeOrder(View v){

        int count = myDb.getOrdersCount();
        int ordno = count+1;
        user_email = myDb.getTempUser();
        Cursor res = myDb.getCartDetails();

        if(res.getCount()>0){

            while(res.moveToNext()){
                itemName = res.getString(0);
                qnty = res.getInt(1);
                itemPrice = res.getInt(2);
                totalPrice = res.getDouble(3);
                myDb.insertData_Order(ordno,user_email,itemName,qnty,itemPrice,totalPrice);

            }
            Toast.makeText(Cart.this,"Order Placed Successfully",Toast.LENGTH_LONG).show();
            myDb.deleteCartOrder();

            getCartOrders();
            adapter=new ArrayAdapter<String>(this,R.layout.list_veiw_resource,R.id.textview,strItems);
            listv.setAdapter(adapter);
        }
    }
    public void alertDeleteItem(final int i){

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(true);
        alert.setMessage("Do you want to remove item?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
               int del =  myDb.deleteCartItem(itemAl.get(i));
                if(del > 0){
                    Toast.makeText(getApplicationContext(),"Item Removed",Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getApplicationContext(),"Item Not Removed",Toast.LENGTH_SHORT).show();
                getCartOrders();
                callAdapter();

            }
        });

        alert.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
        alert.show();

    }
}
