package com.aurorav2.digital.signage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.aurorav2.digital.signage.Interface.LoginRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {
    private EditText emailText;
    private EditText passwordText;
    private TextView registerTextView;
    private Button signButton;
    private String email;
    private String password;
    private ProgressBar progressBar;
   // public static final int MY_PERMISSIONS_REQUEST_READ_AND_WRITE_SDK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailText = findViewById(R.id.emailEditText);
        passwordText = findViewById(R.id.passwordEditText);
        signButton = findViewById(R.id.signButton);
        registerTextView = findViewById(R.id.registerTextView);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        checkStoragePermission();

        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailText.getText().toString();
                if (email.isEmpty()){
                    Toasty.error(getApplicationContext(), "Please input your email !", Toast.LENGTH_LONG, true).show();
                    return;
                }
                if (!validateEmail(email)){
                    Toasty.error(getApplicationContext(), "Invalid email !", Toast.LENGTH_LONG, true).show();
                    return;
                }
                password = passwordText.getText().toString();
                if (password.isEmpty()){
                    Toasty.error(getApplicationContext(), "Please input your password !", Toast.LENGTH_LONG, true).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            int value = jsonResponse.getInt("login");
                            if (value == 1){
                                Intent intent = new Intent(LoginActivity.this, PaneActivity.class);
                                startActivity(intent);
                            }else if(value == -2){
                                Toasty.error(getApplicationContext(), "No Register. Please sign up !", Toast.LENGTH_LONG, true).show();
                            }else if (value == 0){
                                Toasty.error(getApplicationContext(), "No have permission. Please wait until allowing !", Toast.LENGTH_LONG, true).show();
                            }else if (value == -1){
                                Toasty.error(getApplicationContext(), "Wrong Password. Please retype !", Toast.LENGTH_LONG, true).show();
                            }else{
                                Toasty.error(getApplicationContext(), "Failed to login !", Toast.LENGTH_LONG, true).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(email, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MainActivity.MY_PERMISSIONS_REQUEST_READ_AND_WRITE_SDK);
            return;
        }
    }

    protected boolean validateEmail(String email){
        String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
