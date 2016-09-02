package com.restful_client.restfulclient;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.restful_client.restfulclient.model.Vehicle;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VehicleModifyActivity extends AppCompatActivity {

    private Spinner spinner;
    private TextView lblIdMod;
    private TextView lblFuelMod;
    private TextView lblDoorMod;
    private Boolean onload = false;
    private ArrayAdapter<String> dataAdapter;
    private Long selection;
    private String selectionDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_modify);
        spinner = (Spinner) findViewById(R.id.modifySelection);

        lblIdMod = (TextView) findViewById(R.id.lblIdMod);
        lblFuelMod = (TextView) findViewById(R.id.lblFuelMod);
        lblDoorMod = (TextView) findViewById(R.id.lblDoorMod);

        System.out.println("onload: " + onload);
        onload = true;
        System.out.println("onload: " + onload);
        new HttpRequestTask().execute();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String item = parent.getItemAtPosition(position).toString();
                selection = Long.parseLong(item);
                onload = false;
                new HttpRequestTask().execute();
            }
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    public void actionModifyVehicle(View view) {
        Intent intent = new Intent(this, ConfirmModifyActivity.class);
        intent.putExtra("id", selection);
        intent.putExtra("doors", selectionDetails);
        System.out.println("Modify: " + selection+"****"+selectionDetails);
        startActivity(intent);
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, Vehicle[]> {
        String response="default";
        @Override
        protected Vehicle[] doInBackground(Void... params) {
            System.out.println("in execute");
            System.out.println("onload: " + onload);
            Vehicle[] vehics;
            Vehicle[] vehicsSingle = new Vehicle[0];
            if(onload){
                vehics = fetchIds();
                System.out.println("vehicles: " + vehics.length);
                return vehics;
            }else {
               try{
                   final String url = "http://148.100.4.102:8080/api/vehicles/{id}";
                   RestTemplate rest = new RestTemplate();
                   rest.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                   HashMap<String, Long> map = new HashMap<>();
                   map.put("id", selection);
                   vehicsSingle = rest.getForObject(url, Vehicle[].class, map);

                   System.out.println(vehicsSingle[0].getEngine());
                }catch(HttpClientErrorException registerError) {

                }catch(Exception e){
                    System.out.println("ERROR: SEAND/RECEIVE_REQUEST - " + e);
                }
                return vehicsSingle;
            }
        }

        @Override
        protected void onPostExecute(Vehicle[] vehicles) {
            if(onload) {
                if(vehicles != null) {
                    System.out.println("vehicles: " + vehicles.length);
                    List<String> ids = new ArrayList<String>();
                    for (int i = 0; i < vehicles.length; i++) {
                        ids.add("" + vehicles[i].getId());
                    }
                    System.out.println("vehicles: " + ids.size());
                    dataAdapter = new ArrayAdapter<String>(VehicleModifyActivity.this.getApplicationContext(), android.R.layout.simple_spinner_item, ids);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(dataAdapter);
                }else{
                    Toast.makeText(VehicleModifyActivity.this.getApplicationContext(), "Error fetchong ids.", Toast.LENGTH_LONG).show();
                }
            }else{
                if(vehicles.length > 0) {
                    lblIdMod.setText("" + vehicles[0].getId());
                    lblFuelMod.setText(vehicles[0].getEngine());
                    selectionDetails = vehicles[0].getDoors();
                    lblDoorMod.setText(vehicles[0].getDoors());
                }else{
                    Vehicle v1 = new Vehicle("error", "error");
                    v1.setId(1L);
                    Vehicle[] vehicsErr = {v1};
                    lblIdMod.setText("" + vehicsErr[0].getId());
                    lblFuelMod.setText(vehicsErr[0].getEngine());
                    selectionDetails = vehicsErr[0].getDoors();
                    lblDoorMod.setText(vehicsErr[0].getDoors());
                }
            }
        }
    }

    public Vehicle[] fetchIds(){
        try{
            final String url = "http://148.100.4.102:8080/api/vehicles";
            RestTemplate rest = new RestTemplate();
            rest.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            Vehicle[] vehicles = rest.getForObject(url, Vehicle[].class);
            if(vehicles != null){
                System.out.println(vehicles.length+"***"+vehicles[1].getId()+"***"+vehicles[1].getEngine());
                return vehicles;
            }
        }catch(HttpClientErrorException registerError) {
            System.out.println("ERROR: SEAND/RECEIVE_REQUEST - " + registerError);
        }catch(Exception e){
            System.out.println("ERROR: SOME ERROR - " + e);
        }
        Vehicle v = new Vehicle("bla", "bla");
        v.setId(1L);
        Vehicle v1 = new Vehicle("bla", "bla");
        v1.setId(2L);
        Vehicle[] ve = {v, v1};
        return ve;
    }
}
