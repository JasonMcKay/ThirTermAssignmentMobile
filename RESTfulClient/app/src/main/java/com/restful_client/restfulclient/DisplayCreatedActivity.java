package com.restful_client.restfulclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DisplayCreatedActivity extends AppCompatActivity {

    private TextView lblIdAss;
    private TextView lblFuelAss;
    private TextView lblDoorAss;
    private Long id;
    private String engineType;
    private String doorType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_created);

        lblIdAss = (TextView)findViewById(R.id.lblIdAss);
        lblFuelAss = (TextView)findViewById(R.id.lblFuelAss);
        lblDoorAss = (TextView)findViewById(R.id.lblDoorAss);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }
        id = bundle.getLong("id");
        System.out.println("******--->"+id);
        engineType = bundle.getString("fuel");
        doorType = bundle.getString("door");

        lblIdAss.setText(""+id);
        lblFuelAss.setText(engineType);
        lblDoorAss.setText(doorType);
    }

    public void actionHomeAss(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}
