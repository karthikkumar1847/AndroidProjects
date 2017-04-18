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

public class MenuMexican extends AppCompatActivity {
    Button btnGTCart;
    String[] menmexican,itemName;
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
        setContentView(R.layout.activity_menu_mexican);
        myDb = new DatabaseHelper(this);

        btnGTCart = (Button)findViewById(R.id.btnGoToCartMexcnMnu);

        // menmexican= new String[]{"Chimichanga                          -  5$","Cheese and Bean Burrito     -  6$","Chicken Fiesta Taco Salad   -  9$","Crunchy Taco Supreme        - 10$","Chicken Enchilada                 - 10$"};

        menImage=new Integer[]{R.drawable.chimi,R.drawable.chese,R.drawable.fiesta,R.drawable.crunchy,R.drawable.endo};
        menName=new String[]{"Chimichanga","Cheese and Bean Burrito","Chicken Fiesta Taco Salad","Crunchy Taco Supreme","Chicken Enchilada"};
        menPrice=new String[]{"$5.00","$6.00","$9.00","$10.00","$10.00"};

        itemName= new String[]{"Chimichanga","Cheese and Bean Burrito","Chicken Fiesta Taco Salad","Crunchy Taco Supreme","Chicken Enchilada"};
        itemPrice= new int[]{5,6,9,10,10};


        CustomAdapter adapter=new CustomAdapter(this,menName,menPrice,menImage);

        ListView listv=(ListView)findViewById(R.id.listViewMenMexican);
//        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.list_veiw_resource,R.id.textview,menmexican);

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
    public void gotoCartFromMexican(View v){
        Intent intent = new Intent(MenuMexican.this,Cart.class);
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
                    Toast.makeText(MenuMexican.this, itemName[i]+" added", Toast.LENGTH_LONG).show();
                    countCart = countCart+1;
                    btnGTCart.setText("CART("+countCart+")");
                } else{
                    Toast.makeText(MenuMexican.this, "Not added", Toast.LENGTH_LONG).show();
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
