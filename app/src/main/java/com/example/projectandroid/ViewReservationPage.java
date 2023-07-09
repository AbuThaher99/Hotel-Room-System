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

public class ViewReservationPage extends AppCompatActivity {
    private static final String LOGOUT_URL = "http://10.0.2.2:80/android/logout.php";

    private ListView listView;
    private ReservationAdapter adapter;
    private List<Reservation> reservations;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reservation_page);

        listView = findViewById(R.id.listView);
        reservations = new ArrayList<>();
        adapter = new ReservationAdapter(this, reservations);
        listView.setAdapter(adapter);
        fetchReservationsFromServer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home1:
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(ViewReservationPage.this, AdminPage.class);
                intent1.putExtra("username", getIntent().getStringExtra("username"));
                startActivity(intent1);
                return true;
            case R.id.Delete:
                Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.addroom:
                Toast.makeText(this, "Add Room", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ViewReservationPage.this, AddRomPage.class);
                intent.putExtra("username", getIntent().getStringExtra("username"));
                startActivity(intent);
                return true;
            case R.id.addfood:
                Toast.makeText(this, "Add Food", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(ViewReservationPage.this, AddFoodActivity.class);
                intent2.putExtra("username", getIntent().getStringExtra("username"));
                startActivity(intent2);
                return true;
            case R.id.VfoodR:
                Toast.makeText(this, "View food Reservation", Toast.LENGTH_SHORT).show();
                Intent intent3 = new Intent(ViewReservationPage.this, ViewFoodReservation.class);
                intent3.putExtra("username", getIntent().getStringExtra("username"));
                startActivity(intent3);
                return true;
            case R.id.VroomR:
                Toast.makeText(this, "View Room Reservation", Toast.LENGTH_SHORT).show();
                Intent intent4 = new Intent(ViewReservationPage.this, ViewReservationPage.class);
                intent4.putExtra("username", getIntent().getStringExtra("username"));
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
                        Toast.makeText(ViewReservationPage.this, "Logout successful", Toast.LENGTH_SHORT).show();
                        // Navigate to the login page
                        Intent intent = new Intent(ViewReservationPage.this, SiginAdmin.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finishAffinity();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Toast.makeText(ViewReservationPage.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("Logout Error", error.getMessage());
                    }
                });

        // Get the RequestQueue and add the request to it
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void fetchReservationsFromServer() {
        String url = "http://10.0.2.2:80/android/getresvervation.php";

        // Create an AsyncTask to fetch the reservations
        FetchReservationsTask fetchReservationsTask = new FetchReservationsTask();
        fetchReservationsTask.execute(url);
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
                            Toast.makeText(ViewReservationPage.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            jsonArray[0] = null;
                        }
                    });

            // Get the RequestQueue and add the request to it
            RequestQueue requestQueue = Volley.newRequestQueue(ViewReservationPage.this);
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
                String imageUrl = reservationObject.getString("name");
                double price = reservationObject.getDouble("price");
                String state = reservationObject.getString("state");
                int roomid = reservationObject.getInt("roomid");

                // Create a Reservation object and add it to the list
                Reservation reservation = new Reservation(id, imageUrl, username, price, state, roomid);
                reservations.add(reservation);
            }

            // Notify the adapter that the data set has changed
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
