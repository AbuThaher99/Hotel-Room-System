package com.example.projectandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ViewReports extends AppCompatActivity {
    EditText date,textarea;
    private String username =null;
    private static final String LOGOUT_URL = "http://10.0.2.2:80/android/logout.php";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reports);
        date = findViewById(R.id.Dated);
        textarea = findViewById(R.id.textArea);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get current date values
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Create DatePickerDialog and set the selected date in EditText
                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewReports.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Update the EditText with the selected date
                                String selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%04d",
                                        dayOfMonth, month + 1, year);
                                date.setText(selectedDate);
                            }
                        }, year, month, day);

                // Show the DatePickerDialog
                datePickerDialog.show();
            }
        });
        username = getIntent().getStringExtra("username");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu2, menu);

        return true;
    }

    public void getReports(View view) {
        String selectedDate = date.getText().toString().trim();
        String url = "http://10.0.2.2:80/android/getAllreport.php";

        GetReportsTask getReportsTask = new GetReportsTask();
        getReportsTask.execute(url, selectedDate);
    }

    private class GetReportsTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String selectedDate = params[1];
             String[] responseMessage = new String[1];

            try {
                // Create a HashMap to hold the POST parameters
                Map<String, String> postParams = new HashMap<>();
                postParams.put("date", selectedDate);

                // Make the POST request using Volley
                RequestQueue requestQueue = Volley.newRequestQueue(ViewReports.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                responseMessage[0] = response;
                                onPostExecute(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("GetReportsTask", error.toString());
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        return postParams;
                    }
                };

                // Add the request to the RequestQueue
                requestQueue.add(stringRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return responseMessage[0];
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                // Process the response
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    StringBuilder stringBuilder = new StringBuilder();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int totalFood = jsonObject.getInt("totalfood");
                        int totalRes = jsonObject.getInt("totalres");
                        stringBuilder.append("Report ").append(i + 1).append(": \r\n")
                                .append("Total Food : ").append(totalFood)
                                .append("\r\n")
                                .append("Total Reservations : ").append(totalRes)
                                .append("\r\n")
                                .append("=========================")
                                .append("\r\n");
                    }

                    textarea.setText(stringBuilder.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                // Handle the case when the response is null
                Log.e("GetReportsTask", "Response is null");
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.home1:
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                Intent intent5 = new Intent(ViewReports.this, AdminPage.class);
                intent5.putExtra("username", username);
                startActivity(intent5);
                return true;


            case R.id.Delete:
                Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
                Intent intent7 = new Intent(ViewReports.this, delete.class);
                intent7.putExtra("username", username);
                startActivity(intent7);
                return true;

            case R.id.addroom:
                Toast.makeText(this, "Add Room", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ViewReports.this, AddRomPage.class);
                intent.putExtra("username", username);
                startActivity(intent);

                return true;

            case R.id.addfood:
                Toast.makeText(this, "Add Food", Toast.LENGTH_SHORT).show();
                Intent intent3 = new Intent(ViewReports.this, AddFoodActivity.class);
                intent3.putExtra("username", username);
                startActivity(intent3);
                return true;

            case R.id.VfoodR:
                Toast.makeText(this, "View food Reservation", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(ViewReports.this, ViewFoodReservation.class);
                intent2.putExtra("username", username);
                startActivity(intent2);
                return true;
            case R.id.VroomR:
                Toast.makeText(this, "View Room Reservation", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(ViewReports.this, ViewReservationPage.class);
                intent1.putExtra("username", username);
                startActivity(intent1);
                return true;

            case R.id.greport:
                Toast.makeText(this, "Generate Report", Toast.LENGTH_SHORT).show();
                Intent intent4 = new Intent(ViewReports.this, Report.class);
                intent4.putExtra("username", username);
                startActivity(intent4);
                return true;
            case R.id.Vreports:
                Toast.makeText(this, "View Reports", Toast.LENGTH_SHORT).show();
                return true;

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
                RequestQueue requestQueue = Volley.newRequestQueue(ViewReports.this);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, logoutUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(ViewReports.this, "Logout successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ViewReports.this, SiginAdmin.class);
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