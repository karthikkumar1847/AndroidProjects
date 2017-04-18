package pradeep.restaurant;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.MenuItemHoverListener;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MenuIndian extends AppCompatActivity {

    Button btnGTCart;
    ListView listv;
    String[] menindian,itemName;
    int[] itemPrice;
    int qnty=0;
    String qtystr;
    DatabaseHelper myDb;
    int countCart;
    Context context;
    ArrayList arrayList;

     Integer[] menImage;
     String[] menName;
    String[] menPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_indian);
        myDb = new DatabaseHelper(this);


       // menindian =new String[]{"Tandoori Curry            -   5$","Chicken Tikka Curry   - 10$","Veg Biryani                  - 10$","Chicken Biryani          - 12$","Lamb Biryani              - 14$"};
        itemName =new String[]{"Naan","Tandoori Curry","Chicken Tikka Curry","Veg Biryani","Chicken Biryani","Lamb Biryani"};
        itemPrice =new int[]{1,5,10,10,12,14};

        menImage=new Integer[]{R.drawable.naan,R.drawable.tandoricurry,R.drawable.chickentikka,R.drawable.vegbiryani,R.drawable.chickenbiryani,R.drawable.lambbiryani};
        menName=new String[]{"Naan","Tandoori Curry","Chicken Tikka Curry","Veg Biryani","Chicken Biryani","Lamb Biryani"};
        menPrice = new String[]{"$1.00","$5.00","$10.00","$10.00","$12.00","$14.00"};

        btnGTCart = (Button)findViewById(R.id.btnGoToCartIndMenu);

        CustomAdapter adapter=new CustomAdapter(this,menName,menPrice,menImage);
        listv=(ListView)findViewById(R.id.listViewMenIndian);

       // final ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.list_veiw_resource,R.id.textview,menindian);
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

    public void gotoCart(View v){

       Intent intent = new Intent(MenuIndian.this,Cart.class);
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
                    Toast.makeText(MenuIndian.this, itemName[i]+" added", Toast.LENGTH_LONG).show();
                    countCart = countCart+1;
                    btnGTCart.setText("CART("+countCart+")");
                } else{
                    Toast.makeText(MenuIndian.this, "Not added", Toast.LENGTH_LONG).show();
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();

            }
        });
        alert.show();

    }
}
