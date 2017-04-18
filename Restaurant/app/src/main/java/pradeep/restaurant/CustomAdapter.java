package pradeep.restaurant;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String> {


    private final Activity context;
    private final String [] menNam;
    private final String [] menPrice;
    private final Integer [] menImg;

    public CustomAdapter(Activity context, String[] menNam, String[] menPrice, Integer[] menImg) {

        super(context,R.layout.activity_image_list,menNam);
        this.context = context;
        this.menNam = menNam;
        this.menPrice = menPrice;
        this.menImg = menImg;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=context.getLayoutInflater();

        View rowView=inflater.inflate(R.layout.activity_image_list,null,true);

        TextView textView=(TextView)rowView.findViewById(R.id.textViewlist);
        TextView textViewPrice=(TextView)rowView.findViewById(R.id.textViewPriceList);
        ImageView imageView=(ImageView)rowView.findViewById(R.id.imageViewList);

        textView.setText(menNam[position]);
        textViewPrice.setText(menPrice[position]);

        imageView.setImageResource(menImg[position]);

        return rowView;
    }
}
