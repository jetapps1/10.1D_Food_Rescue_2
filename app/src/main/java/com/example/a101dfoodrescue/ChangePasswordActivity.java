package com.example.a101dfoodrescue;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a101dfoodrescue.data.DatabaseHelper;
import com.example.a101dfoodrescue.model.User;

public class ChangePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        EditText sUsernameEditText2 = findViewById(R.id.sUsernameEditText2);
        EditText sPasswordEditText2 = findViewById(R.id.sPasswordEditText2);
        EditText confirmPasswordEditText2 = findViewById(R.id.confirmPasswordEditText2);
        Button updateButton = findViewById(R.id.updateButton);
        DatabaseHelper db = new DatabaseHelper(this);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = sUsernameEditText2.getText().toString();
                String password = sPasswordEditText2.getText().toString();
                String confirmPassword = confirmPasswordEditText2.getText().toString();

                if (password.equals(confirmPassword))
                {
                    int updateRow  = db.updatePassword(new User(username, password));
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