package com.example.searchRim;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScopeActivity extends Activity {
     private Button returnBut;
    private Button refreshBut;
    private TextView textView;
    private Button mapBut;
    private LinearLayout data;
    private LinearLayout map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.poi_listing);
        map = (LinearLayout) findViewById(R.id.map);
        textView  = (TextView) findViewById(R.id.textData);
        textView.setText("范围:3000米内");
        returnBut = (Button) findViewById(R.id.returnButton);
        returnBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        refreshBut = (Button) findViewById(R.id.refreshButton);
        refreshBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapButtonOnClick(view);
            }
        });
        mapBut = (Button) findViewById(R.id.mapButton);

    }

    public void mapButtonOnClick(View v){
        map.setVisibility(v.VISIBLE);
    }
    public void buttonOnClick(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final String[] date = new String[]{"1000米内", "2000米内", "3000米内", "4000米内", "5000米内"};

        builder.setItems(date, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                textView.setText("范围:"+date[which]);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
