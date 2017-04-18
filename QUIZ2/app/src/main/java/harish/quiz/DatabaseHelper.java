package harish.quiz;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Quiz.db";

    public static final String TABLE_NAME = "user_table";
    public static final String COL1 = "NAME";
    public static final String COL2 = "EMAIL";
    public static final String COL3 = "PASSWORDKEY";
    public static final String COL4 = "SECURITY_QTN";
    public static final String COL5 = "ANSWER";

    public static final String QUIZ_TABLE_NAME = "Quiz_table";
    public static final String QCOL1 = "CATEGORY";
    public static final String QCOL2 = "QUESTION";
    public static final String QCOL3 = "OPTION1";
    public static final String QCOL4 = "OPTION2";
    public static final String QCOL5 = "OPTION3";
    public static final String QCOL6 = "OPTION4";
    public static final String QCOL7 = "ANSWER";



    SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table "+TABLE_NAME+"(EMAIL TEXT PRIMARY KEY,NAME TEXT NOT NULL," +
                "PASSWORDKEY TEXT NOT NULL,SECURITY_QTN TEXT NOT NULL,ANSWER TEXT NOT NULL)";
        db.execSQL(query);

        String query2 = "create table "+QUIZ_TABLE_NAME+"(CATEGORY TEXT NOT NULL,QUESTION TEXT NOT NULL," +
                "OPTION1 TEXT NOT NULL,OPTION2 TEXT NOT NULL,OPTION3 TEXT NOT NULL,OPTION4 TEXT NOT NULL,ANSWER TEXT NOT NULL)";
        db.execSQL(query2);

        db.execSQL("create table TABLE_PWD(EMAIL TEXT PRIMARY KEY,PASSWORDKEY TEXT NOT NULL,PASSWORD TEXT NOT NULL)" );

        for(int i=0; i<qtnsData().length;i++){
            final String Insert_Data= qtnsData()[i];
            db.execSQL(Insert_Data);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+QUIZ_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS TABLE_PWD");
        onCreate(db);

    }

    public boolean insertRecord(String name,String email,String passwordkey,String qtn,String ans){
        db =  this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1,name);
        contentValues.put(COL2,email);
        contentValues.put(COL3,passwordkey);
        contentValues.put(COL4,qtn);
        contentValues.put(COL5,ans);

        long result =  db.insert(TABLE_NAME,null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }
    public boolean insertRecPwd(String email,String passwordkey,String password){
        db =  this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("EMAIL",email);
        contentValues.put("PASSWORDKEY",passwordkey);
        contentValues.put("PASSWORD",password);

        long result =  db.insert("TABLE_PWD",null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean updatePassword(String email,String qtn,String ans,String pass){
        db =  this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        String query = "select EMAIL,PASSWORDKEY from "+TABLE_NAME+" where " +
                "EMAIL='"+email+"'  and SECURITY_QTN= '"+qtn+"' and ANSWER='"+ans+"'";

        Cursor res = db.rawQuery(query,null);

        if(res.getCount()>0) {
            contentValues.put("PASSWORD", pass);
            int res1 = db.update("TABLE_PWD", contentValues, "Email = ?", new String[]{email });
            if (res1 > 0) return true;
            else return false;
        }

        else return false;

    }
    public Cursor getEmailPassword(String e, String p){
        db =  this.getReadableDatabase();
        String query = "select EMAIL,PASSWORDKEY from "+TABLE_NAME+" where " +
                "EMAIL='"+e+"' and " +
                "PASSWORDKEY=(SELECT PASSWORDKEY FROM TABLE_PWD WHERE PASSWORD='"+p+"' and EMAIL='"+e+"')";
        Cursor res = db.rawQuery(query,null);
        return res;
    }
    public boolean insertQuestion(String ctgry,String qtn,String opt1,String opt2,String opt3,String opt4,String ans){
        db =  this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(QCOL1,ctgry);
        contentValues.put(QCOL2,qtn);
        contentValues.put(QCOL3,opt1);
        contentValues.put(QCOL4,opt2);
        contentValues.put(QCOL5,opt3);
        contentValues.put(QCOL6,opt4);
        contentValues.put(QCOL7,ans);

        long result =  db.insert(TABLE_NAME,null,contentValues);
        if(result == -1)
            return false;
        else
            return true;

    }

    public Cursor getQtnAns(String ctgry){
        db =  this.getReadableDatabase();
        String query = "select * from "+QUIZ_TABLE_NAME+" where CATEGORY='"+ctgry+"' ORDER BY RANDOM()";
        Cursor res = db.rawQuery(query,null);
        return res;
    }
    public Cursor getQtnAnsGuest(String ctgry) {
        db = this.getReadableDatabase();
        String query = "select * from " + QUIZ_TABLE_NAME + " where CATEGORY='" + ctgry + "'" + "ORDER BY RANDOM() LIMIT 5";
        Cursor res = db.rawQuery(query, null);
        return res;
    }

    public String[] qtnsData(){

        String[] s = new String[]{
                "INSERT INTO Quiz_table VALUES('Maths','A clock reads 4:30. If the minute hand points East, in what direction will the hour hand point?','North','North-West','North-East','South-East','North-West');",
                "INSERT INTO Quiz_table VALUES('Technology','Who invented ctrl+alt+delete ?','Basking Ridge','Steve Jobs','Bill gates','David Bradley','David Bradley');",
                "INSERT INTO Quiz_table VALUES('Technology','First computer virus is known as','Rabbit','Elk virus','creeper virus','SCA Virus','creeper virus');",
                "INSERT INTO Quiz_table VALUES('Technology','Computer Hard disk was first introduced in 1956 by','Dell','Apple','IBM','Microsoft','IBM');",
                "INSERT INTO Quiz_table VALUES('Technology','A folder in windows computer can not be made with name','can','con','made','make','con');",
                "INSERT INTO Quiz_table VALUES('Technology','India owned first super computer is','Agni','Param','Trisul','Flow Solver','Param');",
                "INSERT INTO Quiz_table VALUES('Technology','The first 3-D home theatre in world was launched by the company in 2010 by','Samsung',' Sony','LG','Panasonic','Samsung');",
                "INSERT INTO Quiz_table VALUES('Technology','Who invented USB ?','AMD','Microsoft','Dell','Intel','Intel');",
                "INSERT INTO Quiz_table VALUES('Technology','What is the meaning of CC in case of E-mail?','Carbon copy','Copier copy','Cater copy','Case copy','Carbon copy');",
                "INSERT INTO Quiz_table VALUES('Technology','Who was the father of Internet ?','Denis Riche','Vint Cerf','Tim Berner ','Martin Cooper','Vint Cerf');",
                "INSERT INTO Quiz_table VALUES('Technology','A process known as ____________ is used by large retailers to study trends','data selection','POS','data mining','data research','data mining');",
                "INSERT INTO Quiz_table VALUES('Maths','How many faces does a dodecahedron have?','12','16','14','24','12');",
                "INSERT INTO Quiz_table VALUES('Maths','20 % of 2 is equal to ','0.2','0.02','0.4','0.04','0.4');",
                "INSERT INTO Quiz_table VALUES('Maths','Which weighs more? A pound of iron or a pound of copper?','cooper','iron ','same','depends on quality','same');",
                "INSERT INTO Quiz_table VALUES('Maths','1/2 of 2/3 of 3/4 of 4/5 of 5/6 of 6/7 of 7/8 of 8/9 of 9/10 of 1,000 = ?','100','1000','500','5000','100');",
                "INSERT INTO Quiz_table VALUES('Maths','What is the angle between minute hand and hour hand at a quarter past three?','8.5 deg','9.5 deg','6.5 deg','7.5 deg','7.5 deg');",
                "INSERT INTO Quiz_table VALUES('Maths','On a clock, how many times a day do the minute and hour hand overlap?','12 times','22 times','24 times','48 times','22 times');",
                "INSERT INTO Quiz_table VALUES('Maths','There are 6 sisters. Each sister has 1 brother. How many brothers are in the sister family?','12','6','1','0','1');",
                "INSERT INTO Quiz_table VALUES('Maths','What is the bigger number ?','Million','Billion','Trillion','Googol','Googol');",
                "INSERT INTO Quiz_table VALUES('Maths','True or false? -4 is a natural number.','True','False','neither','none','True');",
                "INSERT INTO Quiz_table VALUES('Maths','10001 - 101 = ?','9999','9990','9900','9099','9900');",
                "INSERT INTO Quiz_table VALUES('Transportation','In 1936  __ made the first diesel-engined production passenger car - the 260D.','BMW','Ford','Tesla','Mercedes Benz','Mercedes Benz');",
                "INSERT INTO Quiz_table VALUES('Transportation','The RENFE is the name of the railway in which European country?','Germany','Austria','Spain','Poland','Spain');",
                "INSERT INTO Quiz_table VALUES('Transportation','Which country is the originator of the high speed Bullet Train?','Japan','Korea','North Korea','China','Japan');",
                "INSERT INTO Quiz_table VALUES('Transportation','Trains that carry people are called freight trains.','True','False','Neither','None','False');",
                "INSERT INTO Quiz_table VALUES('Transportation','Who invented the motorcycle?','Gottlieb Daimler ','Otto Lilienthal','Nicolas-Joseph Cugnot ','Wilbur and Orville Wright','Gottlieb Daimler ');",
                "INSERT INTO Quiz_table VALUES('Transportation','In 2001, Ford ended its relationship with which tire manufacturer?','Uniroyal','Michelin','Firestone','MRF','Firestone');",
                "INSERT INTO Quiz_table VALUES('Transportation','What is the term for the compression brakes on a semi truck?','Power Brake','Motor Brake','Locking Brake','Jake Brake','Jake Brake');",
                "INSERT INTO Quiz_table VALUES('Transportation','Recently, Tata Motors brought these two companies','Rover','Mustang Range Rover','Jaguar Land Rover','Jaguar Range Rover','Jaguar Land Rover');",
                "INSERT INTO Quiz_table VALUES('Transportation','What helped to propel the boats when traveling north?','current','Wind','pulled by hippos','all of the above','current');",
                "INSERT INTO Quiz_table VALUES('Transportation','What was the name of the plane that dropped the atomic bomb on Hiroshima?','Marie Celeste',' Mauritania','Enola Gay','Pendolino','Enola Gay');",
                "INSERT INTO Quiz_table VALUES('Education','Which planet has the largest moon: Earth, Jupiter, or Saturn?','Earth','Jupiter','Saturn ','Pluto','Jupiter');",
                "INSERT INTO Quiz_table VALUES('Education','French chemists Marie and Pierre Curie discovered what chemical element?','RADIUM','COOPER','URANIUM','NICKEL','RADIUM');",
                "INSERT INTO Quiz_table VALUES('Education','Which International Telecom Company uses the baseline Intelligence Everywhere ?','ATT','Motorola','Vodafone','Verizon','Motorola');",
                "INSERT INTO Quiz_table VALUES('Education','The first airline to allow flyers to surf the net was-','British Airlines','Ethiad Airlines','Singapore Airlines','Lufthansa Airlines','Singapore Airlines');",
                "INSERT INTO Quiz_table VALUES('Education','Who had propounded the planetary laws?','Newton','Kepler','Galileo','Copernicus','Kepler');",
                "INSERT INTO Quiz_table VALUES('Music','The world guitar was adopted in England after they heard about___ instrument named guitarra.','French','German','Roman','Spanish','Spanish');",
                "INSERT INTO Quiz_table VALUES('Music','The King of Music Instruments','Guitar','Piano','Pipe Organ','key board','Pipe Organ');",
                "INSERT INTO Quiz_table VALUES('Music','Worlds Most Expensive Musical Instrument, A Stradivarius violin sold for','US $ 20 Million','US $ 35.9 Million','US $ 14.9 Million','US $ 15.9 Million','US $ 15.9 Million');",
                "INSERT INTO Quiz_table VALUES('Music','Michael Jackson famous dance move, the ___, is said to have been based on the routine of mime artist Marcel Marceau.','Skywalk','moonwalk','landmark','rampwalk','moonwalk');",
                "INSERT INTO Quiz_table VALUES('Music','Gems of India are title given from the government of India to','K S Chitra','Lata Mangeshkar','K J Yesudas','Sonu Nigam','Lata Mangeshkar');",
                "INSERT INTO Quiz_table VALUES('Sports','The San Siro Stadium is in which city?','Milan','Chicago','New York','Dallas','Milan');",
                "INSERT INTO Quiz_table VALUES('Sports','In rugby, which country will the British and Irish Lions tour in 2017? ','Australia','New Zealand','USA','Brazil','New Zealand');",
                "INSERT INTO Quiz_table VALUES('Sports','How many players, including the goaltender, make up an ice hockey team?','8','10','12','6','6');",
                "INSERT INTO Quiz_table VALUES('Sports','In feet, how high is a basketball hoop?','10','12','14','16','10');",
                "INSERT INTO Quiz_table VALUES('Sports',' The American basketball team The Bulls, represent which city?','New Mexico','San Fransisco','New York','Chicago','Chicago');",
                "INSERT INTO Quiz_table VALUES('Literature','Who wrote the 1845 poem The Pied Piper of Hamelin? ','Robert Browning','Walt Whitman','Boris Leonidovich Pasternak','Siegfried Sassoon','Robert Browning');",
                "INSERT INTO Quiz_table VALUES('Literature','Which famous novelist was Governor General of Canada?','Dan Brown','John Buchan','Dan Brown','George Orwell','John Buchan');",
                "INSERT INTO Quiz_table VALUES('Literature','Which British prime minister was awarded the Nobel Prize for Literature?','Edgar Degas',' Richard Yates','J.M.W Turner','Winston Churchill','Winston Churchill');",
                "INSERT INTO Quiz_table VALUES('Literature','Hamlet was the Prince of which country?','Denmark','Portugal','Poland','Hungary','Denmark');",
                "INSERT INTO Quiz_table VALUES('Literature','In which year BHAGAVAD GITA was first translated to English ','1780','1785','1790','1775','1785');",
                "INSERT INTO Quiz_table VALUES('Movies',' In the Tarzan films, what was Jane last name?','Pumba','Pongo','Parker','poker','Parker');",
                "INSERT INTO Quiz_table VALUES('Movies','Which 1969 Oscar winning film was X-rated in the USA?','Midnight Cowboy','Titanic','Marathon Man','Wild Geese ','Midnight Cowboy');",
                "INSERT INTO Quiz_table VALUES('Movies','Spats Columbo is the bad guy in which popular black and white film?','Some Like It Hot','Greta Garbo','Charles Boyer','Bernard Cribbens','Some Like It Hot');",
                "INSERT INTO Quiz_table VALUES('Movies','In which city is The Untouchables set?','Chicago','New York','Washington','New Jersey','Chicago');",
                "INSERT INTO Quiz_table VALUES('Movies','Who wrote the novel on which ,Where Eagles Dare, was based?','Gallipoli','Jane Fonda','Meryl Streep','Alistair MacLean','Alistair MacLean');",
                "INSERT INTO Quiz_table VALUES('Science','Brass gets discoloured in air because of the presence of which of the following gases in air?','Oxygen peroxide','Hydrogen Sulphide','Carbon dioxide','Nitorgen peroxide','Hydrogen Sulphide');",
                "INSERT INTO Quiz_table VALUES('Science','Which of the following metals forms an amalgam with other metals?','Zinc','Lead ','Tin','Mercury','Mercury');",
                "INSERT INTO Quiz_table VALUES('Science','The average salinity of sea water is','3%','3.5%','4%','4.5%','3.5%');",
                "INSERT INTO Quiz_table VALUES('Science','Sodium metal is kept under','Petrol','Diesel','Kerosene','Water','Kerosene');",
                "INSERT INTO Quiz_table VALUES('Science','From which mineral is radium obtained?','Pitchblende','Rutile','Haematite','Limestone','Pitchblende');",
                "INSERT INTO Quiz_table VALUES('Life Style','What company began manufacturing Ray-Ban sunglasses in 1937?','Oakley','Elton John Ltd.','Costa Del Mar','Bausch & Lomb','Bausch & Lomb');",
                "INSERT INTO Quiz_table VALUES('Life Style','What company bought Banana Republic in 1983','Urban Outfitters','Gap','Lee','Nike','Gap');",
                "INSERT INTO Quiz_table VALUES('Life Style','Who invented lipstick in a tube?','Maurice Levy','Carmine','Sarah Bernhardt',' Elizabeth Arden','Maurice Levy');",
                "INSERT INTO Quiz_table VALUES('Life Style','What company made the first wristwatch designed for men?','Rolex','Fosil','Nautica','Timex','Rolex');",
                "INSERT INTO Quiz_table VALUES('Life Style','What gemstone, often used for jewelry, consists of fossilized tree resin?','Sapphire','Pearl','Amber','Dimond','Amber');"};

        return  s;
    }
}
