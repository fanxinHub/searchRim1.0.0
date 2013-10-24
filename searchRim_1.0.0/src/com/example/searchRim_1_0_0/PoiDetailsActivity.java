package com.example.searchRim_1_0_0;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class PoiDetailsActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.poi_details);
        //后退按钮
        Button detailsBack  = (Button) findViewById(R.id.detailsBack);
        detailsBack.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //右箭头刷新按钮
        Button mapRefreshButton= (Button) findViewById(R.id.mapRefreshButton);
        mapRefreshButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //待写
            }
        });

    }
}
