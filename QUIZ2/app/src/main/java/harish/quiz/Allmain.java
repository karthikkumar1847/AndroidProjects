package harish.quiz;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Allmain extends AppCompatActivity {

    String user = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allmain);


        final String[] category={"Technology","Transportation","Maths","Movies","Sports","Science","Education","Music","Life Style","Literature"};
        ListView listv=(ListView)findViewById(R.id.listViewAllMain);
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.resourcefile,R.id.textView,category);

        user = getIntent().getExtras().getString("gkey");

        listv.setAdapter(adapter);
        listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Intent aa =new Intent(Allmain.this,QuizScreen.class);
                aa.putExtra("ckey",category[i]);
                aa.putExtra("gkey",user);
                startActivity(aa);

            }
        });

    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder alertDel = new AlertDialog.Builder(this);
        alertDel.setMessage("Do you want to Exit Quiz ?");
        alertDel.setCancelable(true);
        alertDel.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent i = new Intent(getApplicationContext(),LoginRegister.class);
                startActivity(i);
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
