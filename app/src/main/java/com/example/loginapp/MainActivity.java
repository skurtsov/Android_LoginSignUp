package com.example.loginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
 EditText username,password;
 TextView log;
 Button btn, reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.go);
        reg = findViewById(R.id.reg_button);
        log = findViewById(R.id.logg);
        String u_id;
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchActivities();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = findViewById(R.id.username);
                password = findViewById(R.id.password);

                validate("http://192.168.0.15/login-server/login/validate.php");
            }
        });
    }
    private void switchActivities() {
        // setContentView(R.layout.activity_secon);
        Intent switchActivityIntent = new Intent(this, SignupActivity.class);
        startActivity(switchActivityIntent);
        finish();
    }
    private void SuccessLogin(String u_id) {
        // setContentView(R.layout.activity_secon);
        Intent switchActivityIntent = new Intent(this, SuccessActivity.class);
        switchActivityIntent.putExtra("id",u_id);
        startActivity(switchActivityIntent);
        finish();
    }
    public static String getMd5(String input) {
        try {

            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    public void start(View view) {

    }

    private void validate(String URL){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                // do something
                if(!response.isEmpty()){
                    JSONObject myObject;

                    try {
                        myObject = new JSONObject(response);
                       // log.setText(myObject.get("id").toString());
                        SuccessLogin(myObject.get("id").toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    //setContentView(R.layout.activity_success);

                }
                else{
                    Toast.makeText(MainActivity.this, "incorrect", Toast.LENGTH_SHORT);
                    log.setText("Not OK");
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT);
                log.setText(error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params= new HashMap<String,String>();
                params.put("user",username.getText().toString());
                params.put("password",getMd5(password.getText().toString()));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}