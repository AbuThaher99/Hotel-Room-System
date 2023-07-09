package com.example.projectandroid;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Map;

public class RecycleRoom
        extends RecyclerView.Adapter<RecycleRoom.ViewHolder>{
    private int id[];
    private String[] image;
    private int[] numfloor;
    private double[] price;
    private int[] type;
    private String username;
    private String [] state;
    private OnReservationClickListener reservationClickListener;

    public RecycleRoom(int id[],String[] image, int[] numfloor, double[] price, int[] type,String [] state,String username, OnReservationClickListener listener) {
        this.image = image;
        this.numfloor = numfloor;
        this.price = price;
        this.type = type;
        this.id = id;
        this.username = username;
        this.reservationClickListener = listener;
        this.state = state;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview,
                parent,
                false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,  int position) {

        CardView cardView = holder.cardView;
        ImageView imageView = cardView.findViewById(R.id.image);
        TextView numFloorTextView = cardView.findViewById(R.id.numfloor);
        TextView priceTextView = cardView.findViewById(R.id.price);
        TextView typeTextView = cardView.findViewById(R.id.type);
        TextView stateTextView = cardView.findViewById(R.id.state1);
        Button button = cardView.findViewById(R.id.reserve);
        button.setTag(holder);
        Glide.with(cardView.getContext())
                .load(image[position])
                .into(imageView);

        numFloorTextView.setText("Floor Number :  "+String.valueOf(numfloor[position]));
        priceTextView.setText("Price :  "+String.valueOf(price[position]));
        typeTextView.setText("Type :  "+String.valueOf(type[position]));
        stateTextView.setText("State :  "+state[position]);

        if(state[position].equals("Not Available")){
            button.setEnabled(false);
        }
        else{
            button.setEnabled(true);
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewHolder viewHolder = (ViewHolder) v.getTag();
                int position = viewHolder.getAdapterPosition();

                // get the image that was clicked as String
                String imageClicked = image[position];
                double priceClicked = price[position];
                if (position != RecyclerView.NO_POSITION) {
                    int roomId = id[position];
                    reservationClickListener.onReservationClick(roomId, username,imageClicked,priceClicked);
                    button.setEnabled(false);
                    updateRoomState(roomId,cardView);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return image.length;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        public ViewHolder(CardView cardView){
            super(cardView);
            this.cardView = cardView;
        }

    }


    private void updateRoomState(int roomId, CardView cardView) {
        UpdateRoomStateTask task = new UpdateRoomStateTask(roomId, cardView);
        task.execute();
    }

    private class UpdateRoomStateTask extends AsyncTask<Void, Void, String> {
        private int roomId;
        private CardView cardView;

        public UpdateRoomStateTask(int roomId, CardView cardView) {
            this.roomId = roomId;
            this.cardView = cardView;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String url = "http://10.0.2.2:80/android/updateAval.php";

            StringRequest request = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", String.valueOf(roomId));
                    return params;
                }
            };

            // Create a request queue
            RequestQueue requestQueue = Volley.newRequestQueue(cardView.getContext());

            // Add the request to the queue
            requestQueue.add(request);

            return "Room state updated successfully.";
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(cardView.getContext(), result, Toast.LENGTH_SHORT).show();
        }
    }



    public interface OnReservationClickListener {
        void onReservationClick(int roomId, String username,String name,double price);
    }
}