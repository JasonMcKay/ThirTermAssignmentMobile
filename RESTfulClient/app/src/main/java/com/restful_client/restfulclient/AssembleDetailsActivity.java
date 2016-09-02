package com.restful_client.restfulclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

public class AssembleDetailsActivity extends AppCompatActivity {

    private String engineType = null;
    private String doorType = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assemble_details);
    }

    public void actionAssembleNext(View view) {
        if(engineType == null || doorType == null)
        {
            Toast.makeText(this, "A fuel and door type are required.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent intent = new Intent(this, ConfirmDetailsActivity.class);
            intent.putExtra("fuelType", engineType);
            intent.putExtra("doorType", doorType);
            startActivity(intent);
        }
    }

    public void selectDoor(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId())
        {
            case R.id.rbFour:
                if(checked)
                {
                    doorType = "four door";
                    System.out.println("fordoor");
                }
                break;
            case R.id.rbTwo:
                if(checked)
                {
                    doorType = "two door";
                }
                break;
            case R.id.rbNo:
                if(checked)
                {
                    doorType = "no door";
                }
                break;
        }
    }

    public void selectEngine(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId())
        {
            case R.id.rbDiesel:
                if(checked)
                {
                    engineType = "diesel";
                    System.out.println("Diesel");
                }
                break;
            case R.id.rbUnleaded:
                if(checked)
                {
                    engineType = "unleaded";
                }
                break;
            case R.id.rbLeaded:
                if(checked)
                {
                    engineType = "leaded";
                }
                break;
        }
    }
}
