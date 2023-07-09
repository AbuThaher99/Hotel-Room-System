package com.example.projectandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class recyclefood
        extends RecyclerView.Adapter<recyclefood.ViewHolder>{
    String username;
    private int[] id;
    private String[] name;
    private String[] description;
    private double[] price;

    private OnReservationClickListener reservationClickListener;


    public recyclefood(int[] id, String[] name, String[] description, double[] price ,String username ,OnReservationClickListener listener) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.username = username;
        this.reservationClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewfood,
                parent,
                false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,  int position) {
        CardView cardView = holder.cardView;
        TextView Name=cardView.findViewById(R.id.namefood);
        TextView Description=cardView.findViewById(R.id.description);
        TextView Price=cardView.findViewById(R.id.pricefood);
        Button add=cardView.findViewById(R.id.recfood);
        add.setTag(holder);
        Name.setText("Name : "+ name[position]);
        Description.setText("Description :"+description[position]);
        Price.setText("Price :"+String.valueOf(price[position]));

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclefood.ViewHolder viewHolder = (recyclefood.ViewHolder) v.getTag();
                int position = viewHolder.getAdapterPosition();

                // get the image that was clicked as String
                String Name = name[position];
                double priceClicked = price[position];
                String des = description[position];
                if (position != RecyclerView.NO_POSITION) {

                    reservationClickListener.onReservationClick( username,Name,priceClicked,des);

                }
            }
        });




    }

    @Override
    public int getItemCount() {
        return id.length;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        public ViewHolder(CardView cardView){
            super(cardView);
            this.cardView = cardView;
        }

    }

    public interface OnReservationClickListener {
        void onReservationClick( String username,String name,double price,String des);
    }
}