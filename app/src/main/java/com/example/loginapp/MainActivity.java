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
                        log.setText(myObject.get("id").toString());
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
                params.put("password",password.getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}