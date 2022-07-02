package com.example.loginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    EditText u_name,u_pass,u_mail,u_phone ;
    TextView log_q;
    Button reg_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //reg_log = findViewById(R.id.register_log);
        log_q = findViewById(R.id.textView2);
        reg_button = findViewById(R.id.register);
        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View  view) {
                u_name = findViewById(R.id.u_name);
                u_pass = findViewById(R.id.u_pass);
                u_mail = findViewById(R.id.u_mail);
                u_phone = findViewById(R.id.u_phone);
              //reg_log.setText("test");
                register("http://192.168.0.15/login-server/login/signup/signup.php");

            }
        });
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
    private void register(String URL){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                // do something
                if(!response.isEmpty()){
                    log_q.setText("OK");

                    setContentView(R.layout.activity_success);

                }
                else{
                    Toast.makeText(SignupActivity.this, "incorrect", Toast.LENGTH_SHORT);
                    log_q.setText("Not OK");
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignupActivity.this, error.toString(), Toast.LENGTH_SHORT);
                log_q.setText(error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params= new HashMap<String,String>();
                params.put("user",u_name.getText().toString());
                params.put("password",getMd5(u_pass.getText().toString()));
                params.put("mail",u_mail.getText().toString());
                params.put("phone",u_phone.getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}