package pradeep.restaurant;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RMenu extends AppCompatActivity {
    DatabaseHelper myDb;
    String user_email;

    ImageButton btnIndian,btnAmerican,btnMexican,btnChinese;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rmenu);

        btnIndian=(ImageButton)findViewById(R.id.btnRMenuIndian);
        btnAmerican=(ImageButton)findViewById(R.id.btnRMenuAmerican);
        btnMexican=(ImageButton)findViewById(R.id.btnRMenuMexi);
        btnChinese=(ImageButton)findViewById(R.id.btnRMenuChina);

        btnIndian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(RMenu.this,MenuIndian.class);
                startActivity(i);
            }
        });

        btnAmerican.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(RMenu.this,MenuAmerican.class);
                startActivity(i);
            }
        });
        btnMexican.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(RMenu.this,MenuMexican.class);
                startActivity(i);
            }
        });
        btnChinese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(RMenu.this,MenuChinese.class);
                startActivity(i);
            }
        });

    }

}


