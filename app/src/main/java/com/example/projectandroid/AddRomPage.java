package com.example.projectandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddRomPage extends AppCompatActivity {
    private static final String LOGOUT_URL = "http://10.0.2.2:80/android/logout.php";
    private EditText Url, Numfloor, Price, Type ,date;
    private Button upload;
    private ImageView imageView;
    private String username = null;
    private boolean image = false;

    private AddRoomTask addRoomTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rom_page);
        username = getIntent().getStringExtra("username");

        setup();
//        date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Get current date values
//                Calendar calendar = Calendar.getInstance();
//                int year = calendar.get(Calendar.YEAR);
//                int month = calendar.get(Calendar.MONTH);
//                int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//                // Create DatePickerDialog and set the selected date in EditText
//                DatePickerDialog datePickerDialog = new DatePickerDialog(AddRomPage.this,
//                        new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                                // Update the EditText with the selected date
//                                String selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%04d",
//                                        dayOfMonth, month + 1, year);
//                                date.setText(selectedDate);
//                            }
//                        }, year, month, day);
//
//                // Show the DatePickerDialog
//                datePickerDialog.show();
//            }
//        });


    }

    public void setup() {
        Url = findViewById(R.id.url);
        Numfloor = findViewById(R.id.numfloor);
        Price = findViewById(R.id.price);
        Type = findViewById(R.id.type);
        imageView = findViewById(R.id.imageView);

    }

    public void addRoom(View view) {
        String name = Url.getText().toString();
        String numfloor = Numfloor.getText().toString();
        String price = Price.getText().toString();
        String type = Type.getText().toString();

        if (validate()) {
            // Cancel any existing task before starting a new one
            if (addRoomTask != null) {
                addRoomTask.cancel(true);
            }

            // Create a new task and execute it
            addRoomTask = new AddRoomTask();
            addRoomTask.execute(name, numfloor, price, type);
            image = false;
        }
    }

    private boolean validate() {
        if (Url.getText().toString().isEmpty()) {
            Url.setError("Enter Url");
            return false;
        }
        if (Numfloor.getText().toString().isEmpty()) {
            Numfloor.setError("Enter Numfloor");
            return false;
        }

        if (Price.getText().toString().isEmpty()) {
            Price.setError("Enter Price");
            return false;
        }

        if (Type.getText().toString().isEmpty()) {
            Type.setError("Enter Type");
            return false;
        }

        return true;
    }

    private class AddRoomTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String numfloor = params[1];
            String price = params[2];
            String type = params[3];

            // Perform the add operation in the background
            return add(name, numfloor, price, type);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Room added successfully.")) {
                Toast.makeText(AddRomPage.this, result, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AddRomPage.this, result, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String add(String name, String numfloor, String price, String type) {
        String url = "http://10.0.2.2:80/android/addroom.php";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the server
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Toast.makeText(AddRomPage.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Set the parameters for the request
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("numfloor", numfloor);
                params.put("price", price);
                params.put("type", type);
                return params;
            }
        };

        // Get the RequestQueue and add the request to it
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        // Return a dummy result for demonstration purposes
        return "Room added successfully.";
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
                Intent intent1 = new Intent(AddRomPage.this, AdminPage.class);
                intent1.putExtra("username", username);
                startActivity(intent1);
                return true;

            case R.id.Delete:
                Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
                Intent intent7 = new Intent(AddRomPage.this, delete.class);
                intent7.putExtra("username", username);
                startActivity(intent7);
                return true;

            case R.id.addroom:
                Toast.makeText(this, "Add Room", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddRomPage.this, AddRomPage.class);
                intent.putExtra("username", username);
                startActivity(intent);
                return true;

            case R.id.addfood:
                Toast.makeText(this, "Add Food", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(AddRomPage.this, AddFoodActivity.class);
                intent2.putExtra("username", username);
                startActivity(intent2);
                startActivity(intent2);
                return true;

            case R.id.VfoodR:
                Toast.makeText(this, "View food Reservation", Toast.LENGTH_SHORT).show();
                Intent intent3 = new Intent(AddRomPage.this, ViewFoodReservation.class);
                intent3.putExtra("username", username);
                startActivity(intent3);
                return true;
            case R.id.VroomR:
                Toast.makeText(this, "View Room Reservation", Toast.LENGTH_SHORT).show();
                Intent intent4 = new Intent(AddRomPage.this, ViewReservationPage.class);
                intent4.putExtra("username", username);
                startActivity(intent4);
                return true;

            case R.id.greport:
                Toast.makeText(this, "Generate Report", Toast.LENGTH_SHORT).show();
                Intent intent5 = new Intent(AddRomPage.this, Report.class);
                intent5.putExtra("username", username);
                startActivity(intent5);
                return true;
            case R.id.Vreports:
                Toast.makeText(this, "View Reports", Toast.LENGTH_SHORT).show();
                Intent intent6 = new Intent(AddRomPage.this, ViewReports.class);
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

    private class LogoutTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String logoutUrl = "http://10.0.2.2:80/android/logout.php";

            try {
                // Make a GET request to the logout URL
                RequestQueue requestQueue = Volley.newRequestQueue(AddRomPage.this);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, logoutUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(AddRomPage.this, "Logout successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddRomPage.this, SiginAdmin.class);
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
