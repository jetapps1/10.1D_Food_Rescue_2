package com.example.a101dfoodrescue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a101dfoodrescue.Config.Config;
import com.example.a101dfoodrescue.data.DatabaseHelper;
import com.example.a101dfoodrescue.model.Foods;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MyCart extends AppCompatActivity{

    DatabaseHelper db;
    CartListAdapter cartListAdapter;
    RecyclerView recyclerCartView;
    ArrayList<CheckoutCard> myCards;
    List<Foods> foodList;
    Integer totalAmount;
    TextView txtTotal;
    ImageButton payNow;
    private static final int PAYPAL_REQUEST_CODE = 7171;
    private static final PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        totalAmount = 0;
        myCards = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        db = new DatabaseHelper(this);
        db.getReadableDatabase();

        txtTotal = findViewById(R.id.txtTotal);
        payNow = findViewById(R.id.btnPay);

        Spinner dropdown = findViewById(R.id.spinner1);
        String[] items = new String[]{"Cart", "Home", "Account", "My list"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                if (item.equals("Home")) {
                    Intent intent = new Intent(MyCart.this, HomeView.class);
                    startActivity(intent);
                } else if (item.equals("My List")) {
                    Intent intent = new Intent(MyCart.this, MyList.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        recyclerCartView = findViewById(R.id.recyclerCartView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        cartListAdapter = new CartListAdapter(myCards, getApplicationContext());

        recyclerCartView.setAdapter(cartListAdapter);
        recyclerCartView.setLayoutManager(layoutManager);

        foodList = db.fetchAllFoods();
        for(Foods food:foodList){
            if(food.getCart().equals("yes")){
                totalAmount += Integer.parseInt(food.getPrice());
                CheckoutCard card = new CheckoutCard(food.getTitle(), food.getPrice());
                myCards.add(card);
            }
        }
        txtTotal.setText(String.valueOf(totalAmount));
        
        payNow.setOnClickListener(v-> processPayment());
    }

    private void processPayment() {
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(totalAmount)), "AUD", "PAY NOW", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);

                        startActivity(new Intent(this, PaymentDetails.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", totalAmount)
                        );

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if(resultCode == Activity.RESULT_CANCELED){
                    Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();

                }else if(resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){
                    Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}