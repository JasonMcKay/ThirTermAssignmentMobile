package com.restful_client.restfulclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void actionAssemble(View view) {
        Intent intent = new Intent(this, AssembleDetailsActivity.class);
        startActivity(intent);
    }

    public void actionModify(View view) {
        Intent intent = new Intent(this, VehicleModifyActivity.class);
        startActivity(intent);
    }

    public void actionRemove(View view) {
        Intent intent = new Intent(this, VehicleRemoveActivity.class);
        startActivity(intent);
    }

    public void actionViewAll(View view) {
        Intent intent = new Intent(this, ViewAllVehiclesActivity.class);
        startActivity(intent);
    }
}
