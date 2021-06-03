package com.example.a101dfoodrescue.data;

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
