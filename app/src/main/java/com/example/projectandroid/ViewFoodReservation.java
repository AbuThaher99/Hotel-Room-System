package com.example.projectandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewFoodReservation extends AppCompatActivity {
    private static final String LOGOUT_URL = "http://10.0.2.2:80/android/logout.php";
    private String username ;
    private ListView listView;
    private ResFoodAdapter adapter;
    private List<Resfood> reservations;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_food_reservation);

        listView = findViewById(R.id.listViewFood);
        reservations = new ArrayList<>();
        adapter = new ResFoodAdapter(this, reservations);
        listView.setAdapter(adapter);
        fetchReservationsFromServer();
    }


    private void fetchReservationsFromServer() {
        String url = "http://10.0.2.2:80/android/getresevefood.php";

        // Create an AsyncTask to fetch the reservations
        FetchReservationsTask fetchReservationsTask = new FetchReservationsTask();
        fetchReservationsTask.execute(url);
        username = getIntent().getStringExtra("username");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu2, menu);

        return true;
    }


    private class FetchReservationsTask extends AsyncTask<String, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... urls) {
            String url = urls[0];
            final JSONArray[] jsonArray = {null};

            // Make a JSON array request to fetch the reservations
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            jsonArray[0] = response;
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ViewFoodReservation.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            jsonArray[0] = null;
                        }
                    });

            // Get the RequestQueue and add the request to it
            RequestQueue requestQueue = Volley.newRequestQueue(ViewFoodReservation.this);
            requestQueue.add(request);

            // Wait for the response
            while (jsonArray[0] == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return jsonArray[0];
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            if (jsonArray != null) {
                processReservationsResponse(jsonArray);
            }
        }
    }

    private void processReservationsResponse(JSONArray response) {
        try {
            // Clear the existing reservations list
            reservations.clear();

            // Process each reservation object in the response
            for (int i = 0; i < response.length(); i++) {
                JSONObject reservationObject = response.getJSONObject(i);

                int id = reservationObject.getInt("id");
                String username = reservationObject.getString("username");
                String name= reservationObject.getString("name");
                double price = reservationObject.getDouble("price");
                String state = reservationObject.getString("state");
                String des = reservationObject.getString("Description");


                // Create a Reservation object and add it to the list
                Resfood reservation = new Resfood(id,  username, name, state,price, des);
                reservations.add(reservation);
            }

            // Notify the adapter that the data set has changed
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.home1:
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                Intent intent3 = new Intent(ViewFoodReservation.this, AdminPage.class);
                intent3.putExtra("username", username);
                startActivity(intent3);
                return true;


            case R.id.Delete:
                Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.addroom:
                Toast.makeText(this, "Add Room", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ViewFoodReservation.this, AddRomPage.class);
                intent.putExtra("username", username);
                startActivity(intent);

                return true;

            case R.id.addfood:
                Toast.makeText(this, "Add Food", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(ViewFoodReservation.this, AddFoodActivity.class);
                intent1.putExtra("username", username);
                startActivity(intent1);
                return true;

            case R.id.VfoodR:
                Toast.makeText(this, "View food Reservation", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(ViewFoodReservation.this, ViewFoodReservation.class);
                intent2.putExtra("username", username);
                startActivity(intent2);
                return true;
            case R.id.VroomR:
                Toast.makeText(this, "View Room Reservation", Toast.LENGTH_SHORT).show();
                Intent intent4 = new Intent(ViewFoodReservation.this, ViewReservationPage.class);
                intent4.putExtra("username", username);
                startActivity(intent4);
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

                        Toast.makeText(ViewFoodReservation.this, "Logout successful", Toast.LENGTH_SHORT).show();
                        // Navigate to the login page

                        Intent intent = new Intent(ViewFoodReservation.this, SiginAdmin.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finishAffinity();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Toast.makeText(ViewFoodReservation.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("Logout Error", error.getMessage());
                    }
                });

        // Get the RequestQueue and add the request to it
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
