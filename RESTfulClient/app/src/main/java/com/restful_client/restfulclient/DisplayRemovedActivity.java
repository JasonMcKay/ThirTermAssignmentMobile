package com.restful_client.restfulclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DisplayRemovedActivity extends AppCompatActivity {

    private TextView lblIdRem;
    private TextView lblFuelRem;
    private TextView lblDooRem;
    private String id;
    private String engineType;
    private String doorType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_removed);

        lblIdRem = (TextView)findViewById(R.id.lblIdDisRem);
        lblFuelRem = (TextView)findViewById(R.id.lblFuelDisRem);
        lblDooRem = (TextView)findViewById(R.id.lblDoorDisRem);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }
        id = ""+bundle.getLong("id");
        engineType = bundle.getString("fuel");
        doorType = bundle.getString("door");

        lblIdRem.setText(id);
        lblFuelRem.setText(engineType);
        lblDooRem.setText(doorType);
    }

    public void actionHomeRem(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}
