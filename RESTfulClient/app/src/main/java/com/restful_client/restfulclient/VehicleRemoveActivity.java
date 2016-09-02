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

import com.restful_client.restfulclient.model.Vehicle;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VehicleRemoveActivity extends AppCompatActivity{

    private Spinner spinner;
    private TextView lblFuelRemove;
    private TextView lblDoorRemove;
    private Boolean onload = false;
    private ArrayAdapter<String> dataAdapter;
    private Long selection;
    private String fuel;
    private String door;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_remove);
        spinner = (Spinner) findViewById(R.id.removeSelection);

        lblFuelRemove = (TextView) findViewById(R.id.lblFuelRemove);
        lblDoorRemove = (TextView) findViewById(R.id.lblDoorRemove);
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

    public void actionRemoveVehicle(View view) {
        Intent intent = new Intent(this, ConfirmRemoveActivity.class);
        intent.putExtra("id", selection);
        intent.putExtra("fuel", fuel);
        intent.putExtra("door", door);
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

                }catch(HttpClientErrorException registerError) {
                    System.out.println("ERROR: SEAND/RECEIVE_REQUEST - " + registerError);
                }catch(Exception e){
                    System.out.println("ERROR: SEAND/RECEIVE_REQUEST - " + e);
                }
                return vehicsSingle;
            }
        }

        @Override
        protected void onPostExecute(Vehicle[] vehicles) {
            if(onload){
                System.out.println("vehicles: " + vehicles.length);
                List<String> ids = new ArrayList<String>();
                for (int i = 0; i < vehicles.length; i++){
                    ids.add(""+vehicles[i].getId());
                }
                System.out.println("vehicles: " + ids.size());
                dataAdapter = new ArrayAdapter<String>(VehicleRemoveActivity.this.getApplicationContext(), android.R.layout.simple_spinner_item, ids);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);
            }else{
                if(vehicles.length>0) {
                    lblFuelRemove.setText(vehicles[0].getEngine());
                    lblDoorRemove.setText(vehicles[0].getDoors());
                    fuel = vehicles[0].getEngine();
                    door = vehicles[0].getDoors();
                }else{
                    Vehicle v1 = new Vehicle("error", "error");
                    v1.setId(1L);
                    Vehicle[] vehicsErr = {v1};
                    lblFuelRemove.setText(vehicsErr[0].getEngine());
                    lblDoorRemove.setText(vehicsErr[0].getDoors());
                }
            }
        }
    }

    public Vehicle[] fetchIds(){
        try{
            //not converting to json on return
            System.out.println("In fetchIds");
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
        Vehicle[] ve={v, v1};
        return ve;
    }


}
