package com.example.projectandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainPage extends AppCompatActivity {
    private static final String LOGOUT_URL = "http://10.0.2.2:80/android/logutadmin.php";
    private TextView textView;
    private String username =null;
    private ImageView imageView;
    private android.view.animation.Animation Animation , Animation2 , Animation3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getSupportActionBar().hide();
        setContentView(R.layout.main_page);

        textView = findViewById(R.id.textViewMain);
        imageView = findViewById(R.id.imageView);
        username = getIntent().getStringExtra("username");

        Animation = AnimationUtils.loadAnimation(this, R.anim.image_anim);
        Animation2 = AnimationUtils.loadAnimation(this, R.anim.spinner_anim);
        textView.setAnimation(Animation);
        imageView.setAnimation(Animation2);
        if (savedInstanceState != null) {
            username = savedInstanceState.getString("username");
        } else {
            username = getIntent().getStringExtra("username");
        }
        textView.setText("Welcome " + username);




    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("username", username);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.home:
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.search:
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.Vroom:
                Toast.makeText(this, "View Room", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, ViewRooms.class);
                    intent.putExtra("username", username);
                    startActivity(intent);

                return true;

            case R.id.Vfood:
                Toast.makeText(this, "View food", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(this, ViewFoodActivity.class);
                intent1.putExtra("username", username);
                startActivity(intent1);
                return true;


            case R.id.logout:
                logoutdd();
                Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }


    private void logoutdd() {
        // Make a GET request to the logout URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, LOGOUT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent intent = new Intent(MainPage.this, SignIn.class);
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Toast.makeText(MainPage.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("Logout Error", error.getMessage());
                    }
                });

        // Get the RequestQueue and add the request to it
        RequestQueue requestQueue = Volley.newRequestQueue(MainPage.this);
        requestQueue.add(stringRequest);
    }

}
