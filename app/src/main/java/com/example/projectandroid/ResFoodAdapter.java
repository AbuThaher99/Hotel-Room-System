package com.example.projectandroid;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResFoodAdapter extends ArrayAdapter<Resfood> {

    private List<Resfood> reservations;
    private Context context;

    public ResFoodAdapter(Context context, List<Resfood> reservations) {
        super(context, 0, reservations);
        this.context = context;
        this.reservations = reservations;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.resitemfood, parent, false);
        }

        // Get the current reservation
        Resfood reservation = reservations.get(position);

        TextView usernameTextView = convertView.findViewById(R.id.resusername);
        TextView nameTextView = convertView.findViewById(R.id.resfoodname);
        TextView desTextView = convertView.findViewById(R.id.resfooddes);
        TextView priceTextView = convertView.findViewById(R.id.resfoodprice);
        TextView stateTextView = convertView.findViewById(R.id.resstate);
        Button payButton = convertView.findViewById(R.id.payres);
        double price = reservation.getPrice();
        priceTextView.setText("Price : " + String.valueOf(price));
        usernameTextView.setText("The User : " + reservation.getUsername());
        stateTextView.setText("The States : " + reservation.getState());
        nameTextView.setText("Name : " + reservation.getName());
        desTextView.setText("Description : "+reservation.getDes());

        if (reservation.getState().equals("Paid")) {
            payButton.setEnabled(false);

        } else {
            payButton.setEnabled(true);
        }

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayReservationTask payReservationTask = new PayReservationTask(reservation);
                payReservationTask.execute();
                payButton.setEnabled(false);
            }
        });

        return convertView;
    }

    private class PayReservationTask extends AsyncTask<Void, Void, String> {
        private Resfood reservation;

        public PayReservationTask(Resfood reservation) {
            this.reservation = reservation;
        }

        @Override
        protected String doInBackground(Void... voids) {
            double price = reservation.getPrice();
            insertPriceIntoPayresTable(price);
           // updateReservationState(reservation);
            deleteReservation(reservation);

            // Return a dummy result for demonstration purposes
            return "Reservation paid successfully.";
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();

        }
    }

    private void insertPriceIntoPayresTable(double price) {
        String url = "http://10.0.2.2:80/android/foodpay.php";

        // Create a request to insert the price into the "payres" table
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("price", String.valueOf(price));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

//    private void updateReservationState(Resfood reservation ) {
//        String url = "http://10.0.2.2:80/android/updatestatefood.php";
//
//        // Create a request to update the reservation state in the "resroom" table
//        StringRequest request = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("id", String.valueOf(reservation.getId()));
//                return params;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        requestQueue.add(request);
//    }

    private void deleteReservation(Resfood reservation) {
        String url = "http://10.0.2.2:80/android/delete_reservation.php";

        // Create a request to delete the reservation
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(reservation.getId()));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }


}
