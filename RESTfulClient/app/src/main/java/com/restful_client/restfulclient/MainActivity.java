package com.restful_client.restfulclient;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.restful_client.restfulclient.model.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private EditText txtUsername;
    private EditText txtPassword;
    private String username=null;
    private String password=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
    }

    public void actionLogin(View view) {
        username = txtUsername.getText().toString();
        password = txtPassword.getText().toString();
        String response="default";
        if(!username.equals(null) && !password.equals(null) && !username.isEmpty() && !password.isEmpty()) {
            try {
                response = new HttpRequestTask().execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if(response.equals("USER_ACCEPTED")){
                Intent intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
            }else if(response.equals("INCORRECT_PASSWORD")){
                Toast.makeText(this, "Incorrect password for given username.", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this, "User does not exits, please register.", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "Both username and passwrd are required.", Toast.LENGTH_LONG).show();
        }
    }

    public void actionRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, String>{

        String response="default";

        @Override
        protected String doInBackground(Void... params) {
            try{
                final String url = "http://148.100.4.102:8080/api/users/verify/{username}/{password}";
                RestTemplate rest = new RestTemplate();
                rest.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                HashMap<String, String> map = new HashMap<String, String>(2);
                map.put("username", username);
                map.put("password", password);
                ResponseEntity<User> userResponseEntity = rest.postForEntity(url, null, User.class, map);
                if(userResponseEntity.getStatusCode() == HttpStatus.ACCEPTED){
                    response = "USER_ACCEPTED";
                }

            }catch(HttpClientErrorException loginError) {
                if(loginError.getStatusCode().equals(HttpStatus.NOT_ACCEPTABLE)){
                    response = "INCORRECT_PASSWORD";
                }
                if(loginError.getStatusCode().equals(HttpStatus.NOT_FOUND)){
                    response = "USER_NOT_FOUND";
                }
            }catch(Exception e){
                System.out.println("ERROR: SEAND/RECEIVE_REQUEST - " + e);
            }
            return response;
        }
    }
}
