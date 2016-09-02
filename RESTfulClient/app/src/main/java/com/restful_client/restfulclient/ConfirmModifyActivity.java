package com.restful_client.restfulclient;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.restful_client.restfulclient.model.Vehicle;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

public class ConfirmModifyActivity extends AppCompatActivity {

    private String engineType;
    private Long id;
    private String doorType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_modify);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }
        id = bundle.getLong("id");
        doorType = bundle.getString("doors");
        System.out.println("Modify: " + id+"****"+doorType);
    }

    public void actionModifyConfirm(View view) {
        if(engineType == null){
            Toast.makeText(this, "A fuel type is required.", Toast.LENGTH_LONG).show();
        }else{
            Vehicle v = new Vehicle(engineType, doorType);
            v.setId(id);
            String response = null;
            new HttpRequestTask().execute(v);

            Intent intent = new Intent(this, DisplayModifiedActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("fuel", engineType);
            intent.putExtra("door", doorType);
            startActivity(intent);

        }
    }

    public void selectEngine(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId())
        {
            case R.id.rbDieselMod:
                if(checked)
                {
                    engineType = "diesel";
                    System.out.println("Diesel");
                }
                break;
            case R.id.rbUnleadedMod:
                if(checked)
                {
                    engineType = "unleaded";
                }
                break;
            case R.id.rbLeadedMod:
                if(checked)
                {
                    engineType = "leaded";
                }
                break;
        }
    }

    private class HttpRequestTask extends AsyncTask<Vehicle, Void, Vehicle[]>{

        @Override
        protected Vehicle[] doInBackground(Vehicle... params) {
            Vehicle[] vehicles = new Vehicle[0];
            try{
                //not converting to json on return
                System.out.println("params: "+params[0]);
                final String url = "http://148.100.4.102:8080/api/vehicles/{id}";
                RestTemplate rest = new RestTemplate();
                rest.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                HashMap<String, Long> map = new HashMap<>();
                map.put("id", id);
                Vehicle v = new Vehicle(engineType, doorType);
                v.setId(id);
                HttpEntity<Vehicle> request = new HttpEntity<>(v);
                ResponseEntity<Vehicle[]> vehicle = rest.exchange(url, HttpMethod.PUT, request, Vehicle[].class, map);
                vehicles = vehicle.getBody();
                return vehicles;
            }catch(HttpClientErrorException registerError) {
                System.out.println("ERROR: SEAND/RECEIVE_REQUEST - " + registerError);
            }catch(Exception e){
                System.out.println("ERROR: SOME KIND - " + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Vehicle[] vehicles) {
            id = vehicles[0].getId();
            engineType = vehicles[0].getEngine();
            doorType = vehicles[0].getDoors();
        }
    }
}
