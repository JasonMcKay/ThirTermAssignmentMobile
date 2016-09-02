package com.restful_client.restfulclient;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.restful_client.restfulclient.model.Vehicle;

import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class ConfirmDetailsActivity extends AppCompatActivity {

    private TextView lblFuelTypeAss;
    private TextView lblDoorTypeAss;
    private String fuelType=null;
    private String doorType=null;
    private Long response=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_details);

        lblFuelTypeAss = (TextView) findViewById(R.id.lblFuelTypeAss);
        lblDoorTypeAss = (TextView) findViewById(R.id.lblDoorTypeAss);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }
        fuelType = bundle.getString("fuelType");
        doorType = bundle.getString("doorType");

        lblFuelTypeAss.setText(fuelType);
        lblDoorTypeAss.setText(doorType);
    }

    public void actionConfirmAssembley(View view) {
        new HttpRequestTask().execute();

    }

    private class HttpRequestTask extends AsyncTask<Void, Void, Vehicle[]> {
        Vehicle[] vehicle = new Vehicle[0];
        @Override
        protected Vehicle[] doInBackground(Void... params) {
            try{
                final String url = "http://148.100.4.102:8080/api/vehicles";
                RestTemplate rest = new RestTemplate();
                rest.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                HttpEntity<Vehicle> request = new HttpEntity<>(new Vehicle(fuelType, doorType));
                vehicle = rest.postForObject(url, request, Vehicle[].class);
            }catch(HttpClientErrorException registerError) {
            }catch(Exception e){
                System.out.println("ERROR: SEAND/RECEIVE_REQUEST - " + e);
            }
            return vehicle;
        }

        @Override
        protected void onPostExecute(Vehicle[] vehicles) {
            if(vehicles != null) {
                fuelType=vehicles[0].getEngine();
                doorType=vehicles[0].getDoors();
                response=vehicles[0].getId();
                System.out.println("LOOKING FOR THIS ---> "+response);
                Intent intent = new Intent(ConfirmDetailsActivity.this.getApplicationContext(), DisplayCreatedActivity.class);
                intent.putExtra("id", response);
                intent.putExtra("fuel", fuelType);
                intent.putExtra("door", doorType);
                startActivity(intent);
            }
        }
    }
}
