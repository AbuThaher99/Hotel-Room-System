package com.example.projectandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SignIn extends AppCompatActivity {

    private EditText Password, Email;
    private Button signin;
    private String pass, mail;
    private Intent intent;
    private String usernameh;

    public static final String NAME = "NAME";
    public static final String PASS = "PASS";
    public static final String FLAG = "FLAG";
    private boolean flag = false;

    private CheckBox chk;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.signin);
        setup();
        setupSharedPrefs();
        checkPrefs();
    }

    private void checkPrefs() {
        flag = prefs.getBoolean(FLAG, false);

        if(flag){
            String name = prefs.getString(NAME, "");
            String password = prefs.getString(PASS, "");
            Email.setText(name);
            Password.setText(password);
            chk.setChecked(true);
        }
    }

    private void setupSharedPrefs() {
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
    }

    public void btnLoginOnClick(View view) {
        String name = Email.getText().toString();
        String password = Password.getText().toString();

        if(chk.isChecked()){
            if(!flag) {
                editor.putString(NAME, name);
                editor.putString(PASS, password);
                editor.putBoolean(FLAG, true);
                editor.commit();
            }

        }
        // authenticate the user

    }

    public void SingInAdmin(View view) {
        Intent intent = new Intent(this, SiginAdmin.class);
        startActivity(intent);
    }

    public void setup() {
        Password = findViewById(R.id.passwordsignin);
        Email = findViewById(R.id.Emailsginin);
        signin = findViewById(R.id.signin);
        chk = findViewById(R.id.chk);
    }

    public void CheackUser(View view) {
        mail = Email.getText().toString();
        pass = Password.getText().toString();
        if (valdate()) {
            LoginTask loginTask = new LoginTask();
            loginTask.execute(mail, pass);
        }
    }

    public boolean valdate() {
        String Em = Email.getText().toString();
        String Pass = Password.getText().toString();

        if (Em.isEmpty()) {
            Email.setError("Email is required");
            return false;
        }
        if (Pass.isEmpty()) {
            Password.setError("Password is required");
            return false;
        }

        return true;
    }

    private class LoginTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String email = params[0];
            String password = params[1];

            String url = "http://10.0.2.2:80/android/login.php" + "?email=" + email + "&password=" + password;

            RequestQueue requestQueue = Volley.newRequestQueue(SignIn.this);
            final String[] result = new String[1];
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            result[0] = response;
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle errors
                            result[0] = error.toString();
                        }
                    });

            requestQueue.add(stringRequest);

            // Wait for the response
            while (result[0] == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return result[0];
        }

        @Override
        protected void onPostExecute(String response) {
            Toast.makeText(SignIn.this, response, Toast.LENGTH_SHORT).show();
            if (response.equals("Login successful.")) {
                intent = new Intent(SignIn.this, CardViewMenue.class);
                intent.putExtra("username", getusername(mail));
                startActivity(intent);
            }
        }
    }

    public void login(String email, String password) {
        LoginTask loginTask = new LoginTask();
        loginTask.execute(email, password);
    }

    private class GetUsernameTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String email = params[0];

            String url = "http://10.0.2.2:80/android/username.php" + "?email=" + email;

            RequestQueue requestQueue = Volley.newRequestQueue(SignIn.this);
            final String[] result = new String[1];
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            result[0] = response.trim();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle errors
                            result[0] = error.toString();
                        }
                    });

            requestQueue.add(stringRequest);

            // Wait for the response
            while (result[0] == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return result[0];
        }

        @Override
        protected void onPostExecute(String response) {
            usernameh = response;
            System.out.println("Username: " + usernameh);
            intent.putExtra("username", usernameh);
            startActivity(intent);
        }
    }

    public String getusername(String email) {
        GetUsernameTask getUsernameTask = new GetUsernameTask();
        getUsernameTask.execute(email);

        return usernameh;
    }

    public void SingUp(View view) {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

}
