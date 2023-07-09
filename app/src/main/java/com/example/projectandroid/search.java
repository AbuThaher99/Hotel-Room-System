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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class search extends AppCompatActivity {
    private ArrayList<Room> rooms;
    private static final String LOGOUT_URL = "http://10.0.2.2:80/android/logout.php";
    private EditText roomIdEditText;
    TextView idTextView;
    ImageView imageTextView;
    TextView floorTextView;
    TextView priceTextView;
    TextView typeTextView;
    TextView stateTextView;
    private String username =null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        roomIdEditText = findViewById(R.id.room_id);
        rooms = new ArrayList<>();
         idTextView = findViewById(R.id.room_id_textview);
         imageTextView = findViewById(R.id.room_image_textview);
         floorTextView = findViewById(R.id.room_floor_textview);
         priceTextView = findViewById(R.id.room_price_textview);
         typeTextView = findViewById(R.id.room_type_textview);
         stateTextView = findViewById(R.id.room_state_textview);
        username = getIntent().getStringExtra("username");

    }

    public void search(View view) {
        String roomId = roomIdEditText.getText().toString();
        String url = "http://10.0.2.2:80/android/find_room.php?room_id=" + roomId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Process the room details
                            int id = response.getInt("id");
                            String image = response.getString("name");
                            int numFloor = response.getInt("numfloor");
                            double price = response.getDouble("price");
                            int type = response.getInt("type");
                            String state = response.getString("state");

                            idTextView.setText("Room ID: " + id);
                             Picasso.get().load(image).into(imageTextView);
                            floorTextView.setText("Floor: " + numFloor);
                            priceTextView.setText("Price: " + price);
                            typeTextView.setText("Type: " + type);
                            stateTextView.setText("State: " +state);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("SearchRoomActivity", "Error: " + error.getMessage());
                        Toast.makeText(search.this, "Error: " + "The Room not Found" , Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the Volley request queue
        VolleySingleton.getInstance(this).addToRequestQueue(request);
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
                        Intent intent = new Intent(search.this, SignIn.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finishAffinity();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Toast.makeText(search.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("Logout Error", error.getMessage());
                    }
                });

        // Get the RequestQueue and add the request to it
        RequestQueue requestQueue = Volley.newRequestQueue(search.this);
        requestQueue.add(stringRequest);
    }


}
