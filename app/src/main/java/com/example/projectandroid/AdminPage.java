package com.example.projectandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class AdminPage extends AppCompatActivity {
    private static final String LOGOUT_URL = "http://10.0.2.2:80/android/logout.php";
    private TextView textView;
   private String username =null;
    private Animation Animation , Animation2 , Animation3;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView2);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu2, menu);

        return true;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("username", username);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.home1:
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                return true;


            case R.id.Delete:
                Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.addroom:
                Toast.makeText(this, "Add Room", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminPage.this, AddRomPage.class);
                intent.putExtra("username", username);
                startActivity(intent);

                return true;

            case R.id.addfood:
                Toast.makeText(this, "Add Food", Toast.LENGTH_SHORT).show();
                Intent intent3 = new Intent(AdminPage.this, AddFoodActivity.class);
                intent3.putExtra("username", username);
                startActivity(intent3);
                return true;

            case R.id.VfoodR:
                Toast.makeText(this, "View food Reservation", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(AdminPage.this, ViewFoodReservation.class);
                intent2.putExtra("username", username);
                startActivity(intent2);
                return true;
            case R.id.VroomR:
                Toast.makeText(this, "View Room Reservation", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(AdminPage.this, ViewReservationPage.class);
                intent1.putExtra("username", username);
                startActivity(intent1);
                return true;

            case R.id.logout1:
                logout();
                Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }


    private void logout() {
        // Make a GET request to the logout URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, LOGOUT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                            Toast.makeText(AdminPage.this, "Logout successful", Toast.LENGTH_SHORT).show();
                            // Navigate to the login page

                            Intent intent = new Intent(AdminPage.this, SiginAdmin.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finishAffinity();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Toast.makeText(AdminPage.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("Logout Error", error.getMessage());
                    }
                });

        // Get the RequestQueue and add the request to it
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}