package com.example.projectandroid;


import android.content.Intent;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class delete extends AppCompatActivity {

    private EditText roomIdEditText;
    private Button deleteButton;
    private String username =null;
    private static final String LOGOUT_URL = "http://10.0.2.2:80/android/logout.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        roomIdEditText = findViewById(R.id.room_id);
        deleteButton = findViewById(R.id.buttons);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRoom();
            }
        });

        username = getIntent().getStringExtra("username");
    }

    private void deleteRoom() {
        String roomId = roomIdEditText.getText().toString().trim();

        if (roomId.isEmpty()) {
            Toast.makeText(this, "Please enter a room ID", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://10.0.2.2:80/android/delete_room.php?room_id=" + roomId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                Toast.makeText(delete.this, "Room deleted successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(delete.this, "Failed to delete room", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("DeleteRoomActivity", "Error: " + error.getMessage());
                        Toast.makeText(delete.this, "Error occurred", Toast.LENGTH_SHORT).show();
                    }
                });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu2, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.home1:
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                Intent intent6 = new Intent(delete.this, AdminPage.class);
                intent6.putExtra("username", username);
                startActivity(intent6);
                return true;


            case R.id.Delete:
                Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
                Intent intent7 = new Intent(delete.this, delete.class);
                intent7.putExtra("username", username);
                startActivity(intent7);
                return true;

            case R.id.addroom:
                Toast.makeText(this, "Add Room", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(delete.this, AddRomPage.class);
                intent.putExtra("username", username);
                startActivity(intent);

                return true;

            case R.id.addfood:
                Toast.makeText(this, "Add Food", Toast.LENGTH_SHORT).show();
                Intent intent3 = new Intent(delete.this, AddFoodActivity.class);
                intent3.putExtra("username", username);
                startActivity(intent3);
                return true;

            case R.id.VfoodR:
                Toast.makeText(this, "View food Reservation", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(delete.this, ViewFoodReservation.class);
                intent2.putExtra("username", username);
                startActivity(intent2);
                return true;
            case R.id.VroomR:
                Toast.makeText(this, "View Room Reservation", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(delete.this, ViewReservationPage.class);
                intent1.putExtra("username", username);
                startActivity(intent1);
                return true;

            case R.id.greport:
                Toast.makeText(this, "Generate Report", Toast.LENGTH_SHORT).show();
                Intent intent4 = new Intent(delete.this, Report.class);
                intent4.putExtra("username", username);
                startActivity(intent4);
                return true;
            case R.id.Vreports:
                Toast.makeText(this, "View Reports", Toast.LENGTH_SHORT).show();
                Intent intent5 = new Intent(delete.this, ViewReports.class);
                intent5.putExtra("username", username);
                startActivity(intent5);
                return true;

            case R.id.logout1:
                logout();
                Toast.makeText(this, "logout ", Toast.LENGTH_SHORT).show();
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
                        Intent intent = new Intent(delete.this, SiginAdmin.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finishAffinity();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Toast.makeText(delete.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("Logout Error", error.getMessage());
                    }
                });

        // Get the RequestQueue and add the request to it
        RequestQueue requestQueue = Volley.newRequestQueue(delete.this);
        requestQueue.add(stringRequest);
    }
}