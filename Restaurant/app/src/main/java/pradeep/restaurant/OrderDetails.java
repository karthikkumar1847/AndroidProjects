package pradeep.restaurant;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class OrderDetails extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    static String[] strItems;
    ListView listv;
    TextView tvSubT,tvDelivery,tvTotal,tvEmail,tvAddress;
    String itemName;
    int qnty;
    int itemPrice;
    double subTotalPrice=0;
    double totalPrice=0;
    String user_email;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        myDb = new DatabaseHelper(this);

        tvSubT = (TextView)findViewById(R.id.tvSubTotalOD);
        tvDelivery = (TextView)findViewById(R.id.tvDeliveryOD);
        tvTotal = (TextView)findViewById(R.id.tvTotalOD);
        tvEmail = (TextView)findViewById(R.id.textViewEmailOH);
        tvAddress = (TextView)findViewById(R.id.textViewAddressOH);

        int ord = getIntent().getExtras().getInt("ordnoFromOH");
        user_email = getIntent().getExtras().getString("userEmailIntent");
        getOrders(ord);

        listv=(ListView)findViewById(R.id.listViewOD);
        adapter=new ArrayAdapter<String>(this,R.layout.list_veiw_resource,R.id.textview,strItems);

        listv.setAdapter(adapter);
        listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

            }
        });
        Cursor resAct = myDb.getAccount(user_email);
        resAct.moveToFirst();
        tvEmail.setText( "Email ID : "+resAct.getString(1));
        tvAddress.setText("Address : "+resAct.getString(2));

    }
    public void getOrders(int ord) {
        Cursor res = myDb.getOrdersOnOrdno(ord);
        if (res.getCount() > 0) {
            strItems = new String[res.getCount()];
            int i = 0;
            while (res.moveToNext()) {

                itemName = res.getString(0);
                qnty = res.getInt(1);
                itemPrice = res.getInt(2);
                subTotalPrice = subTotalPrice + res.getDouble(3);

                if(itemPrice<10)
                    strItems[i] =  qnty + " x " + " $0" + itemPrice + ".00 ,  "+itemName;
                else
                    strItems[i] =  qnty + " x " + " $" + itemPrice + ".00 ,  "+itemName;
                i++;

            }
            tvSubT.setText("$" + subTotalPrice);
            tvDelivery.setText("$10.0");
            totalPrice = subTotalPrice + 10;
            tvTotal.setText("$" + totalPrice);

        } else {
            strItems = new String[1];
            strItems[0] = "No Items .";
            tvSubT.setText("");
            tvDelivery.setText("");
            tvTotal.setText("");
        }
    }
}
