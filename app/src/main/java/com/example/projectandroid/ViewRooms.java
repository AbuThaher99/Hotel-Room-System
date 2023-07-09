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

public class ViewRooms extends AppCompatActivity {
    private RecyclerView recycler;
    private List<Room> rooms;
    private String username;
    private static final String LOGOUT_URL = "http://10.0.2.2:80/android/logutadmin.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_rooms);
        recycler = findViewById(R.id.Rooms_recycler);
        rooms = new ArrayList<>();
        new FetchRoomsTask().execute();
    }

    private class FetchRoomsTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String url = "http://10.0.2.2:80/android/getrooms.php";

            RequestQueue requestQueue = Volley.newRequestQueue(ViewRooms.this);
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

            // Add the request to the request queue
            requestQueue.add(stringRequest);

            return null;
        }
    }
    public void processResponse(String response) {
        try {
            // Parse the JSON response
            JSONArray jsonArray = new JSONArray(response);

            // Process each room object in the response
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject roomObject = jsonArray.getJSONObject(i);

                // Extract the room details from the JSON object
                String imageUrl = roomObject.getString("name");
                int numFloor = roomObject.getInt("numfloor");
                double price = roomObject.getDouble("price");
                int type = roomObject.getInt("type");
                int id = roomObject.getInt("id");
                String state = roomObject.getString("state");


                Room room = new Room(id, imageUrl, numFloor, price, type, state);
                rooms.add(room);
                System.out.println(imageUrl + " " + numFloor + " " + price + " " + type);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void handleError(VolleyError error) {
        // Handle the error and display an error message
        Toast.makeText(ViewRooms.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    public void populateRecyclerView() {
        String[] imageUrls = new String[rooms.size()];
        int[] numFloors = new int[rooms.size()];
        double[] prices = new double[rooms.size()];
        int[] types = new int[rooms.size()];
        int[] ids = new int[rooms.size()];
        String[] states = new String[rooms.size()];

        for (int i = 0; i < rooms.size(); i++) {
            imageUrls[i] = rooms.get(i).getIamge();
            numFloors[i] = rooms.get(i).getNumfloor();
            prices[i] = rooms.get(i).getPrice();
            types[i] = rooms.get(i).getType();
            ids[i] = rooms.get(i).getId();
            states[i] = rooms.get(i).getState();
        }
        username = getIntent().getStringExtra("username");
        recycler.setLayoutManager(new LinearLayoutManager(this));
        RecycleRoom adapter = new RecycleRoom(ids, imageUrls, numFloors, prices, types,states ,username, new RecycleRoom.OnReservationClickListener() {
            @Override
            public void onReservationClick(int roomId, String username, String name, double price) {
                new ReservationTask().execute(roomId, name, username, price);
            }
        });
        recycler.setAdapter(adapter);
    }

    private class ReservationTask extends AsyncTask<Object, Void, String> {

        @Override
        protected String doInBackground(Object... params) {
            int roomId = (int) params[0];
            String name = (String) params[1];
            String username = (String) params[2];
            double price = (double) params[3];

            String url = "http://10.0.2.2:80/android/reseveroom.php"; // Replace with your actual API endpoint

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Handle the response from the server
                            Toast.makeText(ViewRooms.this, "Reservation saved successfully", Toast.LENGTH_SHORT).show();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle errors
                            Toast.makeText(ViewRooms.this, "Error saving reservation: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("Reservation Error", error.getMessage());
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    // Pass the roomId, username, name, and price as parameters to the server
                    Map<String, String> params = new HashMap<>();
                    params.put("roomid", String.valueOf(roomId));
                    params.put("name", name);
                    if (username != null) {
                        params.put("username", username);
                    }
                    params.put("price", String.valueOf(price));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(ViewRooms.this);
            requestQueue.add(stringRequest);

            return null;
        }
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
                Intent intent3 = new Intent(this, search.class);
                intent3.putExtra("username", username);
                startActivity(intent3);
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

    private class LogoutTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String logoutUrl = "http://10.0.2.2:80/android/logout.php";

            try {
                // Make a GET request to the logout URL
                RequestQueue requestQueue = Volley.newRequestQueue(ViewRooms.this);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, logoutUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(ViewRooms.this, "Logout successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ViewRooms.this, SignIn.class);
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
    private void logoutdd() {
        LogoutTask logoutTask = new LogoutTask();
        logoutTask.execute();
    }

}
