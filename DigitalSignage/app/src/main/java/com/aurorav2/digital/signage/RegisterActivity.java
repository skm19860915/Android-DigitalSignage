package com.aurorav2.digital.signage;

import android.content.Intent;
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
import com.aurorav2.digital.signage.Interface.RegisterRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity {
    private EditText nameText;
    private EditText emailText;
    private EditText passwordText;
    private EditText confirmPasswordText;
    private Button registerButton;
    private TextView signTextView;
    private String name;
    private String email;
    private String password;
    private String confirmPassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameText = findViewById(R.id.nameEditText);
        emailText = findViewById(R.id.emailEditText);
        passwordText = findViewById(R.id.passwordEditText);
        confirmPasswordText = findViewById(R.id.confirmPasswordEditText);
        registerButton = findViewById(R.id.registerButton);
        signTextView = findViewById(R.id.signTextView);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameText.getText().toString();
                if (name.isEmpty()){
                    Toasty.error(getApplicationContext(), "Please input your name !", Toast.LENGTH_LONG, true).show();
                    return;
                }
                email = emailText.getText().toString();
                if (email.isEmpty()){
                    Toasty.error(getApplicationContext(), "Please input your email !", Toast.LENGTH_LONG, true).show();
                    return;
                }
                if (!validateEmail(email)){
                    Toasty.error(getApplicationContext(), "Invalid email !", Toast.LENGTH_LONG, true).show();
                    return;
                }
                password = passwordText.getText().toString().trim();
                if (password.isEmpty()){
                    Toasty.error(getApplicationContext(), "Please input your password !", Toast.LENGTH_LONG, true).show();
                    return;
                }
                confirmPassword = confirmPasswordText.getText().toString().trim();
                if (confirmPassword.isEmpty()){
                    Toasty.error(getApplicationContext(), "Please input your confirm password !", Toast.LENGTH_LONG, true).show();
                    return;
                }
                if (!password.equals(confirmPassword)){
                    Toasty.error(getApplicationContext(), "Doesn't match password !", Toast.LENGTH_LONG, true).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            int value = jsonResponse.getInt("register");
                            if (value == 1){
                                Toasty.success(getApplicationContext(), "This email registered in successfully !", Toast.LENGTH_LONG, true).show();
                            }else if(value == -1){
                                Toasty.error(getApplicationContext(), "This email already registered at server !", Toast.LENGTH_LONG, true).show();
                            }else{
                                Toasty.error(getApplicationContext(), "Fail to register of email !", Toast.LENGTH_LONG, true).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                };

                RegisterRequest registerRequest = new RegisterRequest(name, email, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }
        });

        signTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    protected boolean validateEmail(String email){
        String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
