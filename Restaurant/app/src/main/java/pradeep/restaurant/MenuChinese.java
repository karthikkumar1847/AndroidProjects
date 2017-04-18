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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MenuChinese extends AppCompatActivity {
    Button btnGTCart;
    String[] menchina,itemName;
    int[] itemPrice;
    int qnty=0;
    String qtystr;
    DatabaseHelper myDb;
    int countCart;

    Integer[] menImage;
    String[] menName;
    String[] menPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_chinese);
        myDb= new DatabaseHelper(this);

        btnGTCart = (Button)findViewById(R.id.btnGoToCartChnseMnu) ;
       // menchina= new String[]{"Sweet and Sour Pork  -  5$","Gong Bao Chicken       -  8$","Spring Rolls                   -  8$","Crab Rangoon              - 10$","Cheng Du Chicken       - 12$"};

         menImage=new Integer[]{R.drawable.sourpork,R.drawable.bao,R.drawable.spring,R.drawable.rangoon,R.drawable.chengdu};
        menName=new String[]{"Sweet and Sour Pork","Gong Bao Chicken","Spring Rolls","Crab Rangoon","Cheng Du Chicken"};
        menPrice=new String[]{"$5.00","$8.00","$8.00","$8.00","$10.00","$12.00"};

        itemName= new String[]{"Sweet and Sour Pork","Gong Bao Chicken","Spring Rolls","Crab Rangoon","Cheng Du Chicken"};

        itemPrice= new int[]{5,8,8,10,12};

        CustomAdapter adapter=new CustomAdapter(this,menName,menPrice,menImage);

        ListView listv=(ListView)findViewById(R.id.listViewMenChina);

//        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.list_veiw_resource,R.id.textview,menchina);

        listv.setAdapter(adapter);
        listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                    alertQnty(i);
            }
        });

        getCartCount();
        onRestart();
    }

    @Override
    public void onRestart() {
        getCartCount();
        super.onRestart();
    }
    public void getCartCount() {
        countCart = myDb.getCartCount();
        if(countCart > 0)
            btnGTCart.setText("CART("+countCart+")");
        else
            btnGTCart.setText("CART");
    }

    public void gotoCartFromChnseMnu(View v){

        Intent intent = new Intent(getApplicationContext(),Cart.class);
        startActivity(intent);
    }
    public void alertQnty(final int i){

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setCancelable(false);
        alert.setMessage("Quantity?");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                qtystr = input.getText().toString().trim();
                qnty = Integer.parseInt(qtystr);

                double totalPrice = qnty*itemPrice[i];
                boolean isInserted = myDb.insertData_Cart(itemName[i],qnty,itemPrice[i],totalPrice);

                if (isInserted) {
                    Toast.makeText(MenuChinese.this, itemName[i]+" added", Toast.LENGTH_LONG).show();
                    countCart = countCart+1;
                    btnGTCart.setText("CART("+countCart+")");
                } else{
                    Toast.makeText(MenuChinese.this, "Not added", Toast.LENGTH_LONG).show();
                }

            }
        });

        alert.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
        alert.show();

    }

}
