package com.restful_client.restfulclient;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.restful_client.restfulclient.model.User;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class RegisterActivity extends AppCompatActivity {

    private EditText txtUsernameReg;
    private EditText txtPasswordReg;
    private EditText txtPasswordRegConfirm;
    private String username=null;
    private String password=null;
    private String passwordConfirm=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtUsernameReg = (EditText) findViewById(R.id.txtUsernameReg);
        txtPasswordReg = (EditText) findViewById(R.id.txtPasswordReg);
        txtPasswordRegConfirm = (EditText) findViewById(R.id.txtPasswordRegConfirm);
    }

    public void actionCreateUser(View view) {
        username = txtUsernameReg.getText().toString();
        password = txtPasswordReg.getText().toString();
        passwordConfirm = txtPasswordRegConfirm.getText().toString();
        if(!username.equals(null) && !password.equals(null) && !username.isEmpty() && !password.isEmpty() && !passwordConfirm.equals(null) && !passwordConfirm.isEmpty()){
            String response=null;
            if(password.equals(passwordConfirm)) {
                try {
                    response = new HttpRequestTask().execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if(response.equals("USER_EXISTS")){
                    Toast.makeText(this, "Username already exists, enter a new one.", Toast.LENGTH_LONG).show();
                }else if(response.equals("NOT_CREATED")){
                    Toast.makeText(this, "Error creating user.", Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent(this, MenuActivity.class);
                    startActivity(intent);
                }
            }else{
                Toast.makeText(this, "Passwords must match.", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_LONG).show();
        }
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, String> {

        String response="default";

        @Override
        protected String doInBackground(Void... params) {

            response = checkUserExists();
            System.out.println("1:"+response);
            if(response.equals("USER_EXISTS")){
                return response;
            }else {

                try{
                    final String url = "http://148.100.4.102:8080/api/users";
                    RestTemplate rest = new RestTemplate();
                    rest.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<User> request = new HttpEntity<>(new User(username, password), headers);
                    User user = rest.postForObject(url, request, User.class);
                    response = "USER_CREATED";

                }catch(HttpClientErrorException registerError) {
                    response = "NOT_CREATED";
                }catch(Exception e){
                    System.out.println("ERROR: SEAND/RECEIVE_REQUEST - " + e);
                    response = "NOT_CREATED";
                }
            }
            System.out.println("2:"+response);
            return response;
        }
    }

    public String checkUserExists(){
        String response="default";
        try{
            final String url = "http://148.100.4.102:8080/api/users/{username}";
            RestTemplate rest = new RestTemplate();
            rest.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            HashMap<String, String> map = new HashMap<String, String>(1);
            map.put("username", username);
            ResponseEntity<User> userResponseEntity = rest.getForEntity(url, User.class, map);
            if(userResponseEntity.getStatusCode() == HttpStatus.OK){
                response = "USER_EXISTS";
            }

        }catch(HttpClientErrorException registerError) {
            if(registerError.getStatusCode().equals(HttpStatus.NOT_FOUND)){
                response = "USER_NOT_FOUND";
            }
        }catch(Exception e){
            System.out.println("ERROR: SEAND/RECEIVE_REQUEST - " + e);
        }
        return response;
    }
}
