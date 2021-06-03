package com.example.a101dfoodrescue;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a101dfoodrescue.data.DatabaseHelper;
import com.example.a101dfoodrescue.model.Foods;

public class newFoodItem extends AppCompatActivity {
    Button btnSave;
    ImageButton uploadImage;
    EditText  editTextTitle, editTextDesc, editTextPickTime, editTextQuant, editTextLocation;
    CalendarView calView;
    DatabaseHelper db;
    int SELECT_PICTURE = 200;
    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(this);
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
            db.insertFood(new Foods(String.valueOf(selectedImageUri), editTextTitle.getText().toString(), editTextDesc.getText().toString(), "user", "no", "50"));

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