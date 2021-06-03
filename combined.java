package com.example.a101dfoodrescue;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a101dfoodrescue.model.Foods;
import com.example.a101dfoodrescue.model.User;
import com.example.a101dfoodrescue.util.Util;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.example.a101dfoodrescue.util.Util.FOOD_TABLE_NAME;
import static com.example.a101dfoodrescue.util.Util.TABLE_NAME;

public class Config {
    public static final String PAYPAL_CLIENT_ID = "AYXzOWpTBCxluzJJPQMu0J6iBHgGhP3r0_GEGY1lyPLsTHF2-MtQk3Lpit7ae0F8pVpx7gFX6F5wz-aK";

}

import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;

        import androidx.annotation.Nullable;

        import com.example.a101dfoodrescue.util.Util;
        import com.example.a101dfoodrescue.model.Foods;
        import com.example.a101dfoodrescue.model.User;

        import java.util.ArrayList;
        import java.util.List;

        import static com.example.a101dfoodrescue.util.Util.FOOD_TABLE_NAME;
        import static com.example.a101dfoodrescue.util.Util.TABLE_NAME;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + Util.USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Util.USERNAME + " TEXT," + Util.PASSWORD + " TEXT)";

    public static final String CREATE_FOOD_TABLE = "CREATE TABLE " + FOOD_TABLE_NAME + "(" + Util.FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Util.IMAGE + " TEXT, " + Util.TITLE + " TEXT, " + Util.DESC + " TEXT, " + Util.USERSUBMITTED + " TEXT, " + Util.ADDEDTOCART + " TEXT, " + Util.PRICE + " TEXT)";

    public DatabaseHelper(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(CREATE_FOOD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FOOD_TABLE_NAME);

        onCreate(sqLiteDatabase);
    }

    public long insertUser (User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.USERNAME, user.getUsername());
        contentValues.put(Util.PASSWORD, user.getPassword());
        long newRowId = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return newRowId;
    }

    public boolean fetchUser(String username, String password)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{Util.USER_ID}, Util.USERNAME + "=?",
                new String[] {username}, null, null, null);
        int numberOfRows = cursor.getCount();
        db.close();

        return numberOfRows > 0;
    }

    public int updatePassword(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.PASSWORD, user.getPassword());

        return db.update(TABLE_NAME, contentValues, Util.USERNAME + "=?", new String[]{String.valueOf(user.getUsername())});
    }

    public long insertFood (Foods foods)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.IMAGE, foods.getFoodImage());
        contentValues.put(Util.TITLE, foods.getTitle());
        contentValues.put(Util.DESC, foods.getDesc());
        contentValues.put(Util.USERSUBMITTED, foods.getUser());
        contentValues.put(Util.ADDEDTOCART, foods.getCart());
        contentValues.put(Util.PRICE, foods.getPrice());

        long newRowId = db.insert(FOOD_TABLE_NAME, null, contentValues);
        db.close();
        return newRowId;
    }

    public List<Foods> fetchAllFoods (){
        List<Foods> foodsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectAll = " SELECT * FROM " + FOOD_TABLE_NAME;
        Cursor cursor = db.rawQuery(selectAll, null);

        if (cursor.moveToNext()) {
            do {
                Foods foods = new Foods();
                foods.setFood_id(cursor.getInt(0));
                foods.setImage(cursor.getString(1));
                foods.setTitle(cursor.getString(2));
                foods.setDesc(cursor.getString(3));
                foods.setUser(cursor.getString(4));
                foods.setCart(cursor.getString(5));
                foods.setPrice(cursor.getString(6));
                foodsList.add(foods);
            } while (cursor.moveToNext());
        }
        return foodsList;
    }
}

import android.os.Parcel;
        import android.os.Parcelable;

public class Foods implements Parcelable {
    private int food_id;
    private String image;
    private String title;
    private String desc;
    private String user;
    private String cart;
    private String price;

    public Foods(String image, String title, String desc, String user, String cart, String price){
        this.image = image;
        this.title = title;
        this.desc = desc;
        this.user = user;
        this.cart = cart;
        this.price = price;
    }

    public Foods(){}

    protected Foods(Parcel in) {
        food_id = in.readInt();
        image = in.readString();
        title = in.readString();
        desc = in.readString();
        user = in.readString();
        cart = in.readString();
        price = in.readString();
    }

    public static final Creator<com.example.a101dfoodrescue.model.Foods> CREATOR = new Creator<com.example.a101dfoodrescue.model.Foods>() {
        @Override
        public com.example.a101dfoodrescue.model.Foods createFromParcel(Parcel in) {
            return new com.example.a101dfoodrescue.model.Foods(in);
        }

        @Override
        public com.example.a101dfoodrescue.model.Foods[] newArray(int size) {
            return new com.example.a101dfoodrescue.model.Foods[size];
        }
    };

    public int getFood_id() {
        return food_id;
    }

    public void setFood_id(int food_id) {
        this.food_id = food_id;
    }

    public String getFoodImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUser(){ return this.user; }
    public void setUser(String user){this.user = user; }

    public String getCart() {
        return cart;
    }

    public void setCart(String cart) {
        this.cart = cart;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(food_id);
        dest.writeString(image);
        dest.writeString(title);
        dest.writeString(desc);
        dest.writeString(user);
        dest.writeString(cart);
        dest.writeString(price);
    }
}

public class User {

    private int user_id;
    private String username;
    private String password;

    public User( String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

public class Util {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "user_db";
    public static final String TABLE_NAME = "users";
    public static final String FOOD_TABLE_NAME = "foods";

    public static final String USER_ID= "user_id";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    public static final String FOOD_ID = "food_id";
    public static final String TITLE = "title";
    public static final String DESC = "desc";
    public static final String IMAGE = "image";
    public static final String USERSUBMITTED = "user";
    public static final String ADDEDTOCART = "cart";
    public static final String PRICE = "price";
}

public class Card {
    private int id;
    private String title;
    private String desc;
    private Uri image;

    public Card(int id, Uri image, String title, String desc) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Uri getImage() { return image; }

    public void setImage(Uri image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private final List<Card> cardList;
    private final Context context;
    private final OnRowClickListener listener;

    public CardAdapter(List<Card> cardList, Context context, OnRowClickListener clickListener) {
        this.cardList = cardList;
        this.context = context;
        this.listener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.card, parent, false);
        return new ViewHolder(itemView, listener);
    }

    public interface OnRowClickListener {
        void onItemClick (int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public android.widget.ImageView ImageView;
        public TextView title;
        public TextView desc;
        public OnRowClickListener onRowClickListener;
        public Button share;

        public ViewHolder(@NonNull View itemView, OnRowClickListener onRowClickListener) {
            super(itemView);
            ImageView = itemView.findViewById(R.id.destinationImageView);
            title = itemView.findViewById(R.id.titleTextView);
            desc = itemView.findViewById(R.id.descTextView);
            share = itemView.findViewById(R.id.button);

            this.onRowClickListener = onRowClickListener;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            onRowClickListener.onItemClick(getAdapterPosition());
        }
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ImageView.setImageURI(cardList.get(position).getImage());
        holder.title.setText(cardList.get(position).getTitle());
        holder.desc.setText(cardList.get(position).getDesc());
        holder.share.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, holder.title.getText().toString());
            sendIntent.putExtra(Intent.EXTRA_TEXT, holder.desc.getText().toString());
            sendIntent.setType("text/plain");
        });
    }

    @Override
    public int getItemCount() {
        return this.cardList.size();
    }
}

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {

    private List<CheckoutCard> cardList;
    private Context context;

    public CartListAdapter(List<CheckoutCard> cardList, Context context) {
        this.cardList = cardList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.checkout_card, parent, false);
        return new CartListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartListAdapter.ViewHolder holder, int position) {
        holder.title.setText(cardList.get(position).getTitle());
        holder.price.setText("$" + cardList.get(position).getPrice());
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public TextView price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtName);
            price = itemView.findViewById(R.id.txtPrice);
        }
    }
    @Override
    public int getItemCount() {
        return cardList.size();
    }
}

public class ChangePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        EditText sUsernameEditText2 = findViewById(R.id.sUsernameEditText2);
        EditText sPasswordEditText2 = findViewById(R.id.sPasswordEditText2);
        EditText confirmPasswordEditText2 = findViewById(R.id.confirmPasswordEditText2);
        Button updateButton = findViewById(R.id.updateButton);
        com.example.a101dfoodrescue.data.DatabaseHelper db = new com.example.a101dfoodrescue.data.DatabaseHelper(this);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = sUsernameEditText2.getText().toString();
                String password = sPasswordEditText2.getText().toString();
                String confirmPassword = confirmPasswordEditText2.getText().toString();

                if (password.equals(confirmPassword))
                {
                    int updateRow  = db.updatePassword(new com.example.a101dfoodrescue.model.User(username, password));
                    if (updateRow > 0)
                    {
                        Toast.makeText(ChangePasswordActivity.this, "Updated successfully!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(ChangePasswordActivity.this, "No row found!", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(ChangePasswordActivity.this, "Two passwords do not match!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}

public class CheckoutCard {
    private String title;
    private String price;

    public CheckoutCard(String title, String price) {
        this.title = title;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

public class food_clicked extends Fragment {

    private static final int PAYPAL_REQUEST_CODE = 7171;
    private static final PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);
    Integer totalAmount;

    com.example.a101dfoodrescue.model.Foods food;

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

public class HomeView extends AppCompatActivity implements CardAdapter.OnRowClickListener {

    CardAdapter cardAdapter;
    RecyclerView recyclerView;
    com.example.a101dfoodrescue.data.DatabaseHelper db;
    List<com.example.a101dfoodrescue.model.Foods> foodList;
    ArrayList<Card> cards = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_view);
        Button newButton = findViewById(R.id.newButton);
        db = new com.example.a101dfoodrescue.data.DatabaseHelper(this);
        db.getWritableDatabase();

        Spinner dropdown = findViewById(R.id.spinner1);
        String[] items = new String[]{"Home", "Account", "My list", "Cart"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                switch (item) {
                    case "Account":
                        break;
                    case "My list": {
                        Intent intent = new Intent(HomeView.this, MyList.class);
                        startActivity(intent);
                        break;
                    }
                    case "Cart": {
                        Intent intent = new Intent(HomeView.this, MyCart.class);
                        startActivity(intent);
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        newButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeView.this, newFoodItem.class);
            startActivity(intent);
        });

        recyclerView = findViewById(R.id.recyclerViewList);
        cardAdapter = new CardAdapter(cards, getApplicationContext(), this);
        recyclerView.setAdapter(cardAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        foodList = db.fetchAllFoods();
        for(com.example.a101dfoodrescue.model.Foods food:foodList){
            Card card = new Card(food.getFood_id(), Uri.parse(food.getFoodImage()), food.getTitle(), food.getDesc());
            cards.add(card);
        }

    }
    @Override
    public void onItemClick(int position) {
        food_clicked fragment = new food_clicked();

        Bundle bundle = new Bundle();
        bundle.putString("IMAGE", foodList.get(position).getFoodImage());
        bundle.putString("TITLE", foodList.get(position).getTitle());
        bundle.putString("DESC", foodList.get(position).getDesc());
        bundle.putParcelable("food", foodList.get(position));

        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.layoutContainer, fragment).commit();
    }
}

public class MainActivity extends AppCompatActivity {
    com.example.a101dfoodrescue.data.DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText usernameEditText = findViewById(R.id.usernameEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);
        Button signupButton = findViewById(R.id.signUpButton);

        db = new com.example.a101dfoodrescue.data.DatabaseHelper(this);
        db.getWritableDatabase();
        db.insertFood(new com.example.a101dfoodrescue.model.Foods("android.resource://com.example.a101dfoodrescue/drawable/parma", "Parma", "Description", "computer", "yes", "10"));
        db.insertFood(new com.example.a101dfoodrescue.model.Foods("android.resource://com.example.a101dfoodrescue/drawable/parma", "Parma1", "Description", "computer", "no", "10"));

        loginButton.setOnClickListener(view -> {
            boolean result = db.fetchUser(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            if (result == true) {
                Toast.makeText(MainActivity.this, "Successfully logged in!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, HomeView.class);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "The user does not exist.", Toast.LENGTH_SHORT).show();
            }
        });

        signupButton.setOnClickListener(view -> {
            Intent signupIntent = new Intent(MainActivity.this, SignupActivity.class);
            startActivity(signupIntent);
        });

    }
}

public class MyCart extends AppCompatActivity{

    com.example.a101dfoodrescue.data.DatabaseHelper db;
    CartListAdapter cartListAdapter;
    RecyclerView recyclerCartView;
    ArrayList<CheckoutCard> myCards;
    List<com.example.a101dfoodrescue.model.Foods> foodList;
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
        db = new com.example.a101dfoodrescue.data.DatabaseHelper(this);
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
        for(com.example.a101dfoodrescue.model.Foods food:foodList){
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

public class MyList extends AppCompatActivity implements CardAdapter.OnRowClickListener {

    CardAdapter cardAdapter;
    RecyclerView recyclerView;

    ArrayList<Card> myCards = new ArrayList<>();
    com.example.a101dfoodrescue.data.DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
        Button newButton = findViewById(R.id.newButton);
        db = new com.example.a101dfoodrescue.data.DatabaseHelper(this);
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

        List<com.example.a101dfoodrescue.model.Foods> foodList = db.fetchAllFoods();
        for(com.example.a101dfoodrescue.model.Foods food:foodList){
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

public class newFoodItem extends AppCompatActivity {
    Button btnSave;
    ImageButton uploadImage;
    EditText  editTextTitle, editTextDesc, editTextPickTime, editTextQuant, editTextLocation;
    CalendarView calView;
    com.example.a101dfoodrescue.data.DatabaseHelper db;
    int SELECT_PICTURE = 200;
    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new com.example.a101dfoodrescue.data.DatabaseHelper(this);
        db.getWritableDatabase();
        setContentView(R.layout.activity_new_food_item);
        uploadImage = findViewById(R.id.btnLoadImage);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDesc = findViewById(R.id.editTextDesc);
        calView = findViewById(R.id.calView);
        editTextPickTime = findViewById(R.id.editTextPickTime);
        editTextQuant = findViewById(R.id.editTextQuant);
        editTextLocation = findViewById(R.id.editTextLocation);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {
            // User = 1
            // Computer = 0
            db.insertFood(new com.example.a101dfoodrescue.model.Foods(String.valueOf(selectedImageUri), editTextTitle.getText().toString(), editTextDesc.getText().toString(), "user", "no", "50"));

            Intent intent = new Intent(newFoodItem.this, HomeView.class);
            startActivity(intent);
        });

        uploadImage.setOnClickListener(v -> {
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
        });
    }

    public void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    uploadImage.setImageURI(selectedImageUri);
                }
            }
        }
    }
}

public class PaymentDetails extends AppCompatActivity {

    TextView txtId, txtAmount, txtStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        txtId = findViewById(R.id.txtId);
        txtAmount = findViewById(R.id.txtAmount);
        txtStatus = findViewById(R.id.txtStatus);

        Intent intent = getIntent();

        try{
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymetnDetails"));
            showDetails(jsonObject.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));

        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    private void showDetails(JSONObject response, String paymentAmount) {
        try {
            txtId.setText(response.getString("id"));
            txtAmount.setText(response.getString(String.format("$%s", paymentAmount)));
            txtStatus.setText(response.getString("state"));
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
}

public class SignupActivity extends AppCompatActivity {
    private com.example.a101dfoodrescue.data.DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        EditText sUsernameEditText = findViewById(R.id.sUsernameEditText);
        EditText sPasswordEditText = findViewById(R.id.sPasswordEditText);
        EditText confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        Button saveButton = findViewById(R.id.saveButton);
        db = new com.example.a101dfoodrescue.data.DatabaseHelper(SignupActivity.this);
        db.getWritableDatabase();

        saveButton.setOnClickListener(view -> {
            String username = sUsernameEditText.getText().toString();
            String password = sPasswordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();

            if (password.equals(confirmPassword))
            {
                long result = db.insertUser(new com.example.a101dfoodrescue.model.User(username, password));
                if (result > 0)
                {
                    Toast.makeText(SignupActivity.this, "Registered successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(SignupActivity.this, "Registration error!", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(SignupActivity.this, "Two passwords do not match!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
