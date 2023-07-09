package com.example.projectandroid;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AddFoodActivity extends AppCompatActivity {

    private EditText foodNameEditText;
    private EditText foodDescriptionEditText;
    private EditText foodPriceEditText;
    private Button addButton;
    private static final String LOGOUT_URL = "http://10.0.2.2:80/android/logout.php";
    String username;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        // Initialize the views
        foodNameEditText = findViewById(R.id.food_name_edittext);
        foodDescriptionEditText = findViewById(R.id.food_description_edittext);
        foodPriceEditText = findViewById(R.id.food_price_edittext);
        addButton = findViewById(R.id.add_button);
        username = getIntent().getStringExtra("username");

        // Set a click listener for the add button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFood();
            }
        });


    }
    private void addFood() {
        String url = "http://10.0.2.2/android/addfood.php";

        // Get the values entered by the user
        String foodName = foodNameEditText.getText().toString().trim();
        String foodDescription = foodDescriptionEditText.getText().toString().trim();
        String foodPrice = foodPriceEditText.getText().toString().trim();

        // Validate the inputs
        if (foodName.isEmpty() || foodDescription.isEmpty() || foodPrice.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Execute the AsyncTask to make the network request
        AddFoodTask addFoodTask = new AddFoodTask(url, foodName, foodDescription, foodPrice);
        addFoodTask.execute();
    }

    private class AddFoodTask extends AsyncTask<Void, Void, String> {
        private String url;
        private String foodName;
        private String foodDescription;
        private String foodPrice;

        public AddFoodTask(String url, String foodName, String foodDescription, String foodPrice) {
            this.url = url;
            this.foodName = foodName;
            this.foodDescription = foodDescription;
            this.foodPrice = foodPrice;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                // Create a HashMap to hold the POST parameters
                Map<String, String> params = new HashMap<>();
                params.put("Name", foodName);
                params.put("Description", foodDescription);
                params.put("Price", foodPrice);

                // Make the POST request using Volley
                RequestQueue requestQueue = Volley.newRequestQueue(AddFoodActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Handle the response from the PHP script
                                if (response.equals("food add")) {
                                    Toast.makeText(AddFoodActivity.this, "Food added successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(AddFoodActivity.this, "Failed to add food", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(AddFoodActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        return params;
                    }
                };

                // Add the request to the RequestQueue
                requestQueue.add(stringRequest);
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }

            return "success";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Perform any UI updates or handle the result as needed
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.home1:
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(AddFoodActivity.this, AdminPage.class);
                intent1.putExtra("username", username);
                startActivity(intent1);
                return true;


            case R.id.Delete:
                Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
                Intent intent7 = new Intent(AddFoodActivity.this, delete.class);
                intent7.putExtra("username", username);
                startActivity(intent7);
                return true;

            case R.id.addroom:
                Toast.makeText(this, "Add Room", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddFoodActivity.this, AddRomPage.class);
                intent.putExtra("username", username);
                startActivity(intent);

                return true;

            case R.id.addfood:
                Toast.makeText(this, "Add Food", Toast.LENGTH_SHORT).show();
                Intent intent3 = new Intent(AddFoodActivity.this, AddFoodActivity.class);
                intent3.putExtra("username", username);
                startActivity(intent3);

                return true;

            case R.id.VfoodR:
                Toast.makeText(this, "View food Reservation", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(AddFoodActivity.this, ViewFoodReservation.class);
                intent2.putExtra("username", username);
                startActivity(intent2);
                return true;
            case R.id.VroomR:
                Toast.makeText(this, "View Room Reservation", Toast.LENGTH_SHORT).show();
                Intent intent4 = new Intent(AddFoodActivity.this, ViewReservationPage.class);
                intent4.putExtra("username", username);
                startActivity(intent4);
                return true;

            case R.id.greport:
                Toast.makeText(this, "Generate Report", Toast.LENGTH_SHORT).show();
                Intent intent5 = new Intent(AddFoodActivity.this, Report.class);
                intent5.putExtra("username", username);
                startActivity(intent5);
                return true;
            case R.id.Vreports:
                Toast.makeText(this, "View Reports", Toast.LENGTH_SHORT).show();
                Intent intent6 = new Intent(AddFoodActivity.this, ViewReports.class);
                intent6.putExtra("username", username);
                startActivity(intent6);

            case R.id.logout1:
                logout();
                Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu2, menu);

        return true;
    }
    private class LogoutTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {

            try {
                // Make a GET request to the logout URL
                RequestQueue requestQueue = Volley.newRequestQueue(AddFoodActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, LOGOUT_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(AddFoodActivity.this, "Logout successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddFoodActivity.this, SiginAdmin.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finishAffinity();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle errors
                                Log.e("Logout Error", error.getMessage());
                            }
                        });

                // Get the RequestQueue and add the request to it
                requestQueue.add(stringRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
    private void logout() {
        LogoutTask logoutTask = new LogoutTask();
        logoutTask.execute();
    }


}
