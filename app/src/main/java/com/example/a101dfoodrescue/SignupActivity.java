package com.example.a101dfoodrescue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a101dfoodrescue.data.DatabaseHelper;
import com.example.a101dfoodrescue.model.User;

public class SignupActivity extends AppCompatActivity {
    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        EditText sUsernameEditText = findViewById(R.id.sUsernameEditText);
        EditText sPasswordEditText = findViewById(R.id.sPasswordEditText);
        EditText confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        Button saveButton = findViewById(R.id.saveButton);
        db = new DatabaseHelper(SignupActivity.this);
        db.getWritableDatabase();

        saveButton.setOnClickListener(view -> {
            String username = sUsernameEditText.getText().toString();
            String password = sPasswordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();

            if (password.equals(confirmPassword))
            {
                long result = db.insertUser(new User(username, password));
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