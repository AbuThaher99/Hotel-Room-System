package com.example.projectandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewFoodActivity extends AppCompatActivity {
    private RecyclerView recycler;
    private List<food> rooms;
    private String username;
    private static final String LOGOUT_URL = "http://10.0.2.2:80/android/logutadmin.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_food);
        recycler = findViewById(R.id.Foods_recycler);
        rooms = new ArrayList<>();
        new FetchRoomsTask().execute();
    }

    private class FetchRoomsTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String url = "http://10.0.2.2:80/android/getfood.php";

            RequestQueue requestQueue = Volley.newRequestQueue(ViewFoodActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            processResponse(response);
                            populateRecyclerView();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            handleError(error);
                        }
                    });

            requestQueue.add(stringRequest);

            return null;
        }
    }
    public void processResponse(String response) {
        try {

            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject roomObject = jsonArray.getJSONObject(i);
                String name = roomObject.getString("Name");
                String description = roomObject.getString("Description");
                double price = roomObject.getDouble("Price");
                int id = roomObject.getInt("id");

                food room = new food(id, name, description, price);
                rooms.add(room);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void handleError(VolleyError error) {
        // Handle the error and display an error message
        Toast.makeText(ViewFoodActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    public void populateRecyclerView() {
        String[] Name = new String[rooms.size()];
        double[] prices = new double[rooms.size()];
        String[] desc = new String[rooms.size()];
        int[] ids = new int[rooms.size()];


        for (int i = 0; i < rooms.size(); i++) {
            Name[i] = rooms.get(i).getName();
            prices[i] = rooms.get(i).getPrice();
            desc[i] = rooms.get(i).getDescription();
            ids[i] = rooms.get(i).getId();

        }
        username = getIntent().getStringExtra("username");
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recyclefood adapter = new recyclefood(ids, Name, desc, prices,username, new recyclefood.OnReservationClickListener() {
            @Override
            public void onReservationClick( String username, String name, double price,String des) {
                new ReservationTask().execute( name, username, price,des);
            }
        });
        recycler.setAdapter(adapter);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.home:
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(this, CardViewMenue.class);
                intent1.putExtra("username", username);
                startActivity(intent1);
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
                Intent intent2 = new Intent(this, ViewFoodActivity.class);
                intent2.putExtra("username", username);
                startActivity(intent2);
                return true;

            case R.id.logout:
                logoutdd();
                Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void logoutdd() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGOUT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the server
                        Toast.makeText(ViewFoodActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ViewFoodActivity.this, SignIn.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finishAffinity();




                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Toast.makeText(ViewFoodActivity.this, "Error logging out: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("Logout Error", error.getMessage());
                    }
                }) {
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ViewFoodActivity.this);
        requestQueue.add(stringRequest);
    }


    private class ReservationTask extends AsyncTask<Object, Void, String> {

        @Override
        protected String doInBackground(Object... params) {

            String name = (String) params[0];
            String username = (String) params[1];
            double price = (double) params[2];
            String des = (String) params[3];

            String url = "http://10.0.2.2:80/android/reservefood.php"; // Replace with your actual API endpoint

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Handle the response from the server
                            Toast.makeText(ViewFoodActivity.this, "Reservation saved successfully", Toast.LENGTH_SHORT).show();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle errors
                            Toast.makeText(ViewFoodActivity.this, "Error saving reservation: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("Reservation Error", error.getMessage());
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    // Pass the roomId, username, name, and price as parameters to the server
                    Map<String, String> params = new HashMap<>();
                    params.put("name", name);
                    if (username != null) {
                        params.put("username", username);
                    }
                    params.put("price", String.valueOf(price));
                    params.put("Description", des);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(ViewFoodActivity.this);
            requestQueue.add(stringRequest);

            return null;
        }
    }

}
