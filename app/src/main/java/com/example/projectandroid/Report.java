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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class Report extends AppCompatActivity {
    TextView textView;
    private String username =null;

    private static final String LOGOUT_URL = "http://10.0.2.2:80/android/logout.php";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        textView = findViewById(R.id.Time);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss", java.util.Locale.getDefault());
        String currentDateandTime = sdf.format(new java.util.Date());
        textView.setText(currentDateandTime);
        username = getIntent().getStringExtra("username");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu2, menu);

        return true;
    }


    public void ganerteReport(View view) {
        new ReportTask().execute();
    }

    private class ReportTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String url = "http://10.0.2.2:80/android/ganertreport.php";
            final String[] responseMessage = new String[1];

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            responseMessage[0] = response;
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            responseMessage[0] = error.toString();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<>();
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(Report.this);
            requestQueue.add(stringRequest);


            while (responseMessage[0] == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return responseMessage[0];
        }

        @Override
        protected void onPostExecute(String response) {
            if(response.equals("Report added.")) {
                Toast.makeText(Report.this, "Report added.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Report.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.home1:
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                Intent intent5 = new Intent(Report.this, AdminPage.class);
                intent5.putExtra("username", username);
                startActivity(intent5);
                return true;


            case R.id.Delete:
                Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
                Intent intent7 = new Intent(Report.this, delete.class);
                intent7.putExtra("username", username);
                startActivity(intent7);

                return true;

            case R.id.addroom:
                Toast.makeText(this, "Add Room", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Report.this, AddRomPage.class);
                intent.putExtra("username", username);
                startActivity(intent);

                return true;

            case R.id.addfood:
                Toast.makeText(this, "Add Food", Toast.LENGTH_SHORT).show();
                Intent intent3 = new Intent(Report.this, AddFoodActivity.class);
                intent3.putExtra("username", username);
                startActivity(intent3);
                return true;

            case R.id.VfoodR:
                Toast.makeText(this, "View food Reservation", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(Report.this, ViewFoodReservation.class);
                intent2.putExtra("username", username);
                startActivity(intent2);
                return true;
            case R.id.VroomR:
                Toast.makeText(this, "View Room Reservation", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(Report.this, ViewReservationPage.class);
                intent1.putExtra("username", username);
                startActivity(intent1);
                return true;

            case R.id.greport:
                Toast.makeText(this, "Generate Report", Toast.LENGTH_SHORT).show();
                Intent intent4 = new Intent(Report.this, Report.class);
                intent4.putExtra("username", username);
                startActivity(intent4);
                return true;
            case R.id.Vreports:
                Toast.makeText(this, "View Reports", Toast.LENGTH_SHORT).show();
                Intent intent6 = new Intent(Report.this, ViewReports.class);
                intent6.putExtra("username", username);
                startActivity(intent6);

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


            try {
                // Make a GET request to the logout URL
                RequestQueue requestQueue = Volley.newRequestQueue(Report.this);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, LOGOUT_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(Report.this, "Logout successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Report.this, SiginAdmin.class);
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







