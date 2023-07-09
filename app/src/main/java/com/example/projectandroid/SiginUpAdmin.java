package com.example.projectandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SiginUpAdmin extends AppCompatActivity {
    EditText Username, Password, Email, secrtkey;
    String user, pass, mail, key;
    Button adduser;
    Switch swith;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sigin_up_admin);
        setup();
        adduser.setVisibility(View.INVISIBLE);
    }

    public void setup() {
        Username = findViewById(R.id.usernameadmin);
        Password = findViewById(R.id.passwordAdmin);
        Email = findViewById(R.id.emailAdmin);
        secrtkey = findViewById(R.id.Secret);
        adduser = findViewById(R.id.signupAdmin);
        swith = findViewById(R.id.SigunUpAdminSwitch);
    }

    public void swithAdmin(View view) {
        if(swith.isChecked()){
            adduser.setVisibility(View.VISIBLE);
        }else {
            adduser.setVisibility(View.INVISIBLE);
        }
    }

    public void adduser(View view) {
        user = Username.getText().toString();
        mail = Email.getText().toString();
        pass = Password.getText().toString();
        key = secrtkey.getText().toString();
        if (key.equals("1234")) {
            if (validate()) {
                AddUserTask addUserTask = new AddUserTask();
                addUserTask.execute(user, mail, pass);

            }
        } else {
            Toast.makeText(this, "Wrong Secret Key", Toast.LENGTH_SHORT).show();
        }
    }



    public boolean validate() {
        String user = Username.getText().toString().trim();
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();
        String phone = secrtkey.getText().toString().trim();
        if (user.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate password length
        if (password.length() < 8) {
            Toast.makeText(this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private class AddUserTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String email = params[1];
            String password = params[2];

            String url = "http://10.0.2.2:80/android/addAdminuser.php";

            RequestQueue requestQueue = Volley.newRequestQueue(SiginUpAdmin.this);
            final String[] responseMessage = new String[1];
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            responseMessage[0] = response;
                            onPostExecute(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            responseMessage[0] = error.toString();
                            onPostExecute(error.toString());
                        }
                    }) {


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding parameters to request
                    params.put("username", username);
                    params.put("email", email);
                    params.put("password", password);
                    //returning parameter
                    return params;
                }
            };

            // Get the RequestQueue and add the request to it
            requestQueue.add(stringRequest);

            return responseMessage[0];
        }

        @Override
        protected void onPostExecute(String response) {
            System.out.println("AbuThaher : "+response);
            if (response!=null&& response.equals("Admin added.")) {
                Toast.makeText(SiginUpAdmin.this, response, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SiginUpAdmin.this, SiginAdmin.class);
                startActivity(intent);

            } else {
                Toast.makeText(SiginUpAdmin.this, response, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
