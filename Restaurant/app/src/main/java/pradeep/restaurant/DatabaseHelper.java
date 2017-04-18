package pradeep.restaurant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "foodyringR.db";
    public static final String TABLE_NAME = "USERDETAILS_TABLE";
    public static final String COL1 = "NAME";
    public static final String COL2 = "EMAIL";
    public static final String COL3 = "PASSWORD_KEY";
    public static final String COL4 = "ADDRESS";

    SQLiteDatabase db;

    public static final String TABLE_NAME2 = "ORDERS_TABLE";
    public static final String OCOL1 = "ORDER_NUMBER";
    public static final String OCOL2 = "EMAIL";
    public static final String OCOL3 = "ITEM";
    public static final String OCOL4 = "QUANTITY";
    public static final String OCOL5 = "ITEM_PRICE";
    public static final String OCOL6 = "TOTAL_PRICE";
    public static final String OCOL7 = "DATE";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME+"(EMAIL TEXT PRIMARY KEY,NAME TEXT,PASSWORD_KEY TEXT,ADDRESS CHAR(50))");
        db.execSQL("create table "+TABLE_NAME2+"(ORDER_NUMBER INTEGER,EMAIL TEXT NOT NULL," +
                "ITEM TEXT,QUANTITY INTEGER,ITEM_PRICE INTEGER,TOTAL_PRICE DOUBLE,DATE DATETIME DEFAULT CURRENT_TIMESTAMP)");
        db.execSQL("create table CART_TABLE(ITEM TEXT,QUANTITY INTEGER,ITEM_PRICE INTEGER,TOTAL_PRICE DOUBLE)");
        db.execSQL("create table TABLE_PWDKEY(EMAIL TEXT PRIMARY KEY,PASSWORD_KEY TEXT NOT NULL,PASSWORD TEXT NOT NULL)" );
        db.execSQL("create table TEMP_EMAIL(EMAIL TEXT PRIMARY KEY)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS TABLE_PWDKEY");
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME2);
        db.execSQL("DROP TABLE IF EXISTS CART_TABLE");
        db.execSQL("DROP TABLE IF EXISTS TEMP_EMAIL");
        onCreate(db);
    }

    public boolean insertData(String name,String email,String password_key,String address){
         db =  this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1,name);
        contentValues.put(COL2,email);
        contentValues.put(COL3,password_key);
        contentValues.put(COL4,address);

       long result =  db.insert(TABLE_NAME,null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertData_Pass(String email,String password_key,String password){
         db =  this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("EMAIL",email);
        contentValues.put("PASSWORD_KEY",password_key);
        contentValues.put("PASSWORD",password);

        long result =  db.insert("TABLE_PWDKEY",null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }




    public Cursor getEmailPass(String e,String p){
         db =  this.getWritableDatabase();
        Cursor res = db.rawQuery("select EMAIL,PASSWORD_KEY from userdetails_table where EMAIL='"+e+"' and " +
                "PASSWORD_KEY=(SELECT PASSWORD_KEY FROM TABLE_PWDKEY WHERE PASSWORD='"+p+"' and EMAIL='"+e+"')",null);
        return res;
    }
    public Cursor getAccount(String e){
         db =  this.getWritableDatabase();
        Cursor res = db.rawQuery("select NAME,EMAIL,ADDRESS from userdetails_table where EMAIL='"+e+"'",null);
        return res;
    }
    public Cursor getAccountPass(String e){
        db =  this.getWritableDatabase();
        Cursor res = db.rawQuery("select PASSWORD from TABLE_PWDKEY where EMAIL='"+e+"'",null);
        return res;
    }
    public boolean updateUserDetails(String name,String email,String password,String address){
         db =  this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1,name);
        contentValues.put(COL2,email);
        contentValues.put(COL4,address);
        db.update(TABLE_NAME,contentValues,"EMAIL = ?",new String[]{ email });

        updatePass(password,email);

        return true;
    }
    public boolean updatePass(String pass,String email){
        db =  this.getWritableDatabase();
         ContentValues contentValues1 = new ContentValues();
         contentValues1.put("PASSWORD",pass);
        db.update("table_pwdkey",contentValues1,"EMAIL = ?",new String[]{ email });
        return true;
    }
    public int deleteAccount(String email){
         db =  this.getWritableDatabase();
        db.delete("table_pwdkey","EMAIL = ?",new String[]{ email });
        return db.delete(TABLE_NAME,"EMAIL = ?",new String[]{ email });
    }

    public boolean insertTempUser(String email){
        db =  this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("EMAIL",email);

        long result =  db.insert("TEMP_EMAIL",null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public String getTempUser(){
        db =  this.getReadableDatabase();
        Cursor res = db.rawQuery("select EMAIL from TEMP_EMAIL",null);
        res.moveToFirst();
        return res.getString(0);
    }

    public void deleteTempUser(){
        db =  this.getWritableDatabase();
        db.delete("TEMP_EMAIL",null,null);
    }

    public boolean insertData_Order(int ordno,String email,String item,int quantity,int price,double total){
        db =  this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(OCOL1,ordno);
        contentValues.put(OCOL2,email);
        contentValues.put(OCOL3,item);
        contentValues.put(OCOL4,quantity);
        contentValues.put(OCOL5,price);
        contentValues.put(OCOL6,total);

        long result =  db.insert(TABLE_NAME2,null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }
    public Cursor getOrdersOnOrdno(int ordno){
        db =  this.getReadableDatabase();
        Cursor res = db.rawQuery("select ITEM,QUANTITY,ITEM_PRICE,TOTAL_PRICE from ORDERS_TABLE where ORDER_NUMBER='"+ordno+"'",null);
        return res;
    }
    public Cursor getOrdersOnEmail(String e){
        db =  this.getReadableDatabase();
        Cursor res = db.rawQuery("select ORDER_NUMBER,TOTAL_PRICE,DATE from ORDERS_TABLE where EMAIL='"+e+"'",null);
        return res;
    }
    public int getOrdersCount(){
        db =  this.getReadableDatabase();
        Cursor res = db.rawQuery("select ORDER_NUMBER from ORDERS_TABLE",null);

        if(res.getCount()>0){
            res.moveToLast();
            return res.getInt(0);
        }else
            return 0;
    }

    public boolean insertData_Cart(String item,int quantity,int itemPrice,double totalPrice){
        db =  this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ITEM",item);
        contentValues.put("QUANTITY",quantity);
        contentValues.put("ITEM_PRICE",itemPrice);
        contentValues.put("TOTAL_PRICE",totalPrice);

        long result =  db.insert("CART_TABLE",null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }
    public Cursor getCartDetails(){
        db =  this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from CART_TABLE",null);
        return res;
    }
    public int getCartCount(){
        db =  this.getReadableDatabase();
        Cursor res = db.rawQuery("select count(*) from CART_TABLE",null);

        if(res.getCount()>0){
            res.moveToFirst();
            return res.getInt(0);
        }else
            return 0;
    }
    public void deleteCartOrder(){
        db =  this.getWritableDatabase();
        db.delete("CART_TABLE",null,null);
       //db.execSQL("delete from CART_TABLE");
    }
    public int deleteCartItem(String item){
        db =  this.getWritableDatabase();
        return db.delete("CART_TABLE","ITEM = ?",new String[]{ item });
    }
}
