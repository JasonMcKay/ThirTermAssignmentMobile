package com.restful_client.restfulclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DisplayModifiedActivity extends AppCompatActivity {

    private TextView lblIdMod;
    private TextView lblFuelMod;
    private TextView lblDoorMod;
    private String id;
    private String engineType;
    private String doorType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_modified);

        lblIdMod = (TextView)findViewById(R.id.lblIdDisMod);
        lblFuelMod = (TextView)findViewById(R.id.lblFuelDisMod);
        lblDoorMod = (TextView)findViewById(R.id.lblDoorDisMod);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }
        id = ""+bundle.getLong("id");
        engineType = bundle.getString("fuel");
        doorType = bundle.getString("door");

        lblIdMod.setText(id);
        lblFuelMod.setText(engineType);
        lblDoorMod.setText(doorType);
    }

    public void actionHomeDisplayMod(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}
