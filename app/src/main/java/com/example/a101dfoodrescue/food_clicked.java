package com.example.a101dfoodrescue;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a101dfoodrescue.Config.Config;
import com.example.a101dfoodrescue.model.Foods;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class food_clicked extends Fragment {

    private static final int PAYPAL_REQUEST_CODE = 7171;
    private static final PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);
    Integer totalAmount;

    Foods food;

    public food_clicked() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            food = getArguments().getParcelable("food");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View rootView = inflater.inflate(R.layout.fragment_food_clicked, container, false);

        ImageView image1 = rootView.findViewById(R.id.imgImage);
        TextView Title = rootView.findViewById(R.id.txtTitle);
        TextView Desc = rootView.findViewById(R.id.txtDesc);
        Button addtoCart = rootView.findViewById(R.id.btnAddCart);
        Button buyNow = rootView.findViewById(R.id.btnBuyNow);

        addtoCart.setOnClickListener(v-> food.setCart("yes"));

        buyNow.setOnClickListener(v-> processPayment());

        image1.setImageURI(Uri.parse(food.getFoodImage()));
        Title.setText(food.getTitle());
        Desc.setText(food.getDesc());

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        LinearLayout linearLayout = view.findViewById(R.id.fragmentLayout);
        linearLayout.setOnClickListener(v -> getView().setVisibility(View.GONE));
    }

    private void processPayment() {
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(food.getPrice())), "AUD", "PAY NOW", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(getActivity(), PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == -1) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);

                        startActivity(new Intent(getActivity(), PaymentDetails.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", totalAmount)
                        );

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if(resultCode == Activity.RESULT_CANCELED){
                    Toast.makeText(getContext(), "Cancel", Toast.LENGTH_SHORT).show();

                }else if(resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){
                    Toast.makeText(getContext(), "Invalid", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}