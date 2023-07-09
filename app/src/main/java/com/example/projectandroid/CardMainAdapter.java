package com.example.projectandroid;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class CardMainAdapter extends RecyclerView.Adapter<CardMainAdapter.ViewHolder> {
    private String[] name;
    private int[] image;

    private static OnItemClickListener listener;

    public CardMainAdapter(String[] name, int[] image, OnItemClickListener listener) {
        this.name = name;
        this.image = image;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewmenue,
                parent,
                false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        CardView cardView = holder.cardView;
        ImageView imageView = cardView.findViewById(R.id.imageViewMenue);
        Drawable drawable = ContextCompat.getDrawable(cardView.getContext(), image[position]);
        imageView.setImageDrawable(drawable);
        TextView textView = cardView.findViewById(R.id.textViewMenue);
        textView.setText(name[position]);









    }

    public interface OnItemClickListener {
        void onItemClick(int position); // Callback method for item click
    }

    @Override
    public int getItemCount() {
        return name.length;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;

            // Set click listener on the card view
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position); // Notify the click listener
                    }
                }
            });
        }
    }

}
