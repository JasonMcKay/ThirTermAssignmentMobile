package com.restful_client.restfulclient;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.restful_client.restfulclient.model.Vehicle;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class ViewAllVehiclesActivity extends AppCompatActivity {

    private TextView lblVehicles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_vehicles);

        lblVehicles = (TextView)findViewById(R.id.lblVehicles);
        new HttpRequestTask().execute();
    }

    public void actionHomeViewAll(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, Vehicle[]>{

        @Override
        protected Vehicle[] doInBackground(Void... params) {
            try{
                //not converting to json on return
                System.out.println("In fetchVehicles");
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
            return null;
        }

        @Override
        protected void onPostExecute(Vehicle[] s) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < s.length; i++){
                sb.append("ID:         "+s[i].getId());
                sb.append("\nFuel Type:  "+s[i].getEngine());
                sb.append("\nDoor Type:  "+s[i].getDoors());
                sb.append("\n\n");
            }
            lblVehicles.setText(sb);
        }
    }
}
