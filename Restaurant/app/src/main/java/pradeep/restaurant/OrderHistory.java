package pradeep.restaurant;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class OrderHistory extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    static String[] strItems;
    ListView listv;
    ArrayList<Double> priceAL;
    ArrayList<Integer> ordnoAL;
    ArrayList<Integer> countAL;

    String user_email;
    DatabaseHelper myDb;

    Double subTotalPrice ,totalPrice ;
    String date;
    int ordno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        user_email = getIntent().getExtras().getString("userEmailIntent");
        myDb = new DatabaseHelper(this);

        priceAL = new ArrayList<Double>();
        ordnoAL = new ArrayList<Integer>();
        countAL = new ArrayList<Integer>();
        getOrders();

       listv=(ListView)findViewById(R.id.listViewOrdersHistory);
        adapter=new ArrayAdapter<String>(this,R.layout.cart_row,R.id.tvItemNameCR,strItems);

        listv.setAdapter(adapter);
        listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                if(!strItems[0].equals("No Recent Orders.")){
                Intent intent = new Intent(getApplicationContext(),OrderDetails.class);
                intent.putExtra("ordnoFromOH",ordnoAL.get(i));
                intent.putExtra("userEmailIntent",user_email);
                startActivity(intent);
                }
            }
        });

    }
    public void getOrders(){
        Cursor res = myDb.getOrdersOnEmail(user_email);
        if(res.getCount()>0){
            int i = 0,count = 0;
            totalPrice = 0.0;
            while(res.moveToNext()){

                ordno = res.getInt(0);
                subTotalPrice = res.getDouble(1);

                boolean isThere = res.moveToNext();

                if(isThere == true && res.getInt(0) == ordno) {
                    totalPrice = totalPrice + subTotalPrice;
                    count++;
                    res.moveToPrevious();
                }
                else {
                    count++;
                    totalPrice = totalPrice + subTotalPrice+10;

                    ordnoAL.add(ordno);
                    countAL.add(count);
                    priceAL.add(totalPrice);

                    totalPrice = 0.0;
                    count = 0;
                    res.moveToPrevious();
                }
                i++;
            }
            strItems = new String[ordnoAL.size()];
            for(int k=0;k<ordnoAL.size();k++){
                strItems[k] = "ORD00"+ordnoAL.get(k)+" , "+countAL.get(k)+" Items , Total $"+priceAL.get(k);
            }

        }else {
            strItems = new String[1];
            strItems[0] = "No Recent Orders.";
        }
    }


}
