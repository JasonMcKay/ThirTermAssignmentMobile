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

import java.util.HashMap;

public class ConfirmRemoveActivity extends AppCompatActivity {

    private TextView lblIdRemoval;
    private TextView lblFuelRemoval;
    private TextView lblDoorRemoval;
    private Long id;
    private String engineType;
    private String doorType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_remove);

        lblIdRemoval = (TextView)findViewById(R.id.lblIdRemoval);
        lblFuelRemoval = (TextView)findViewById(R.id.lblFuelRemoval);
        lblDoorRemoval = (TextView)findViewById(R.id.lblDoorRemoval);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }
        id = bundle.getLong("id");
        engineType = bundle.getString("fuel");
        doorType = bundle.getString("door");

        lblIdRemoval.setText(""+id);
        lblFuelRemoval.setText(engineType);
        lblDoorRemoval.setText(doorType);
    }

    public void avtionRemoval(View view) {
        new HttpRequestTask().execute();

        Intent intent = new Intent(this, DisplayRemovedActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("fuel", engineType);
        intent.putExtra("door", doorType);
        startActivity(intent);
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, String> {
        Vehicle[] vehicle = new Vehicle[0];
        @Override
        protected String doInBackground(Void... params) {
            try{
                final String url = "http://148.100.4.102:8080/api/vehicles/{id}";
                RestTemplate rest = new RestTemplate();
                rest.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                HashMap<String, Long> map = new HashMap<>();
                map.put("id", id);
                rest.delete(url, map);
                return "deleted";
            }catch(HttpClientErrorException registerError) {
            }catch(Exception e){
                System.out.println("ERROR: SEAND/RECEIVE_REQUEST - " + e);
            }
            return "null";
        }

        @Override
        protected void onPostExecute(String aVoid) {
            System.out.println(aVoid);
        }
    }
}
