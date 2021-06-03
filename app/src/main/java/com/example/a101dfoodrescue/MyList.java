package com.example.a101dfoodrescue;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a101dfoodrescue.data.DatabaseHelper;
import com.example.a101dfoodrescue.model.Foods;

import java.util.ArrayList;
import java.util.List;

public class MyList extends AppCompatActivity implements CardAdapter.OnRowClickListener {

    CardAdapter cardAdapter;
    RecyclerView recyclerView;

    ArrayList<Card> myCards = new ArrayList<>();
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
        Button newButton = findViewById(R.id.newButton);
        db = new DatabaseHelper(this);
        db.getWritableDatabase();

        Spinner dropdown = findViewById(R.id.spinner1);
        String[] items = new String[]{"My list", "Home", "Account", "Cart"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                if(item.equals("Home")){
                    Intent intent = new Intent(MyList.this, HomeView.class);
                    startActivity(intent);
                }else if(item.equals("Cart")){
                    Intent intent = new Intent(MyList.this, MyCart.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        newButton.setOnClickListener(v -> {
            Intent intent = new Intent(MyList.this, newFoodItem.class);
            startActivity(intent);
        });

        recyclerView = findViewById(R.id.recyclerViewList);
        cardAdapter = new CardAdapter(myCards, getApplicationContext(), this);
        recyclerView.setAdapter(cardAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        List<Foods> foodList = db.fetchAllFoods();
        for(Foods food:foodList){
            if(food.getUser().equals("user")){
                Card card = new Card(food.getFood_id(), Uri.parse(food.getFoodImage()), food.getTitle(), food.getDesc());
                myCards.add(card);
            }
        }
    }

    @Override
    public void onItemClick(int position) {

    }
}