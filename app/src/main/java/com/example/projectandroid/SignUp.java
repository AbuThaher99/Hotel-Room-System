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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    private EditText Username, Password, Email, Phone;
    private Button signup;
    private String user, pass, mail, phn;
    private Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.signup);
        setup();
        signup.setVisibility(View.INVISIBLE);

    }
    public void swith(View view) {
       if(aSwitch.isChecked()){
           signup.setVisibility(View.VISIBLE);
       }else {
           signup.setVisibility(View.INVISIBLE);
       }
    }
    public void setup() {
        Username = findViewById(R.id.username);
        Password = findViewById(R.id.password);
        Email = findViewById(R.id.email);
        Phone = findViewById(R.id.phone);
        signup = findViewById(R.id.signup);
        aSwitch = findViewById(R.id.SigunUpSwitch);
    }

    public void adduser(View view) {
        user = Username.getText().toString();
        mail = Email.getText().toString();
        pass = Password.getText().toString();
        phn = Phone.getText().toString();
        if (validate()) {
            AddUserTask addUserTask = new AddUserTask();
            addUserTask.execute(user, mail, pass, phn);
        }
    }

    public boolean validate() {
        String user = Username.getText().toString().trim();
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();
        String phone = Phone.getText().toString().trim();
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
            String phone = params[3];

            String url = "http://10.0.2.2:80/android/adduser.php";
            String [] stringsResponse = new String[1];
            RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            stringsResponse[0] = response;
                            onPostExecute(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            stringsResponse[0] = error.toString();
                            onPostExecute(error.toString());
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", username);
                    params.put("email", email);
                    params.put("password", password);
                    params.put("phone", phone);
                    return params;
                }
            };

            // Add the request to the RequestQueue
            requestQueue.add(stringRequest);

            return stringsResponse[0];
        }

        @Override
        protected void onPostExecute(String response) {

            if( response != null && response.equals("User added successfully.")){
                Toast.makeText(SignUp.this, response, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUp.this, SignIn.class);
                startActivity(intent);
            }else{
                Toast.makeText(SignUp.this, response, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
