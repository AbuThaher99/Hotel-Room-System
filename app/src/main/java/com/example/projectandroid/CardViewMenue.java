package com.example.projectandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class CardViewMenue extends AppCompatActivity implements CardMainAdapter.OnItemClickListener {

    private  CardMainAdapter adapter;
    private RecyclerView recycler ;

    private TextView textView;
    private String username =null;
    private static final String LOGOUT_URL = "http://10.0.2.2:80/android/logutadmin.php";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_card_view_menue);
        textView = findViewById(R.id.MenuetextViewcard);
        username=  getIntent().getStringExtra("username");
        recycler = findViewById(R.id.Menue_recycler);
        if (savedInstanceState != null) {
            username = savedInstanceState.getString("username");
        } else {
            username = getIntent().getStringExtra("username");
        }
        textView.setText("Welcome " + username);
        String [] text = new String[MenueCard.menue.length];
        int [] image = new int[MenueCard.menue.length];
        for (int i = 0 ; i<text.length ; i++){
            text[i] = MenueCard.menue[i].getName();
            image[i] = MenueCard.menue[i].getImage();
        }


        adapter = new CardMainAdapter(text,image,this);
        recycler.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recycler.setLayoutManager(layoutManager);





    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("username", username);
    }

    @Override
    public void onItemClick(int position) {
        if (position == 0){
            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
        }else if (position == 1){
            Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(this, search.class);
            intent2.putExtra("username", username);
            startActivity(intent2);
        }else if (position == 2){
            Toast.makeText(this, "View Rooms", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ViewRooms.class);
            intent.putExtra("username", username);
            startActivity(intent);
        }else if (position == 3){
            Toast.makeText(this, "View Food", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(this, ViewFoodActivity.class);
            intent1.putExtra("username", username);
            startActivity(intent1);
        }else if (position == 4){
            logoutdd();
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
        }
    }


    private void logoutdd() {
        // Make a GET request to the logout URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, LOGOUT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent intent = new Intent(CardViewMenue.this, SignIn.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finishAffinity();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Toast.makeText(CardViewMenue.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("Logout Error", error.getMessage());
                    }
                });

        // Get the RequestQueue and add the request to it
        RequestQueue requestQueue = Volley.newRequestQueue(CardViewMenue.this);
        requestQueue.add(stringRequest);
    }
}