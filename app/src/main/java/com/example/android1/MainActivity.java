package com.example.android1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;





public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG =
            MainActivity.class.getSimpleName();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference accounts = database.getReference("accounts");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


<<<<<<< Updated upstream
        Button loginButton = findViewById(R.id.button_login);
        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
=======
    public void showCook(View view) {
        Log.d(LOG_TAG, "Cook Button clicked!");//When the cook button is clicked this will send out a log message
        Intent intent = new Intent(this, CookActivity.class);
        startActivity(intent);
    }
>>>>>>> Stashed changes

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "Login Button clicked!");
                    checkPassword(username.getText().toString(), password.getText().toString(),
                            new CompareValueCallback<Boolean>() {
                        @Override
                        public void callback(Boolean data) {
                            if (data) {
                                Intent intent = new Intent(MainActivity.this,
                                        HomePageActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Wrong username or password",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
    }


    private void checkPassword(String username, String password,
                                       @NonNull CompareValueCallback<Boolean> finishedCallback) {
        accounts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ss : snapshot.getChildren()) {
                    Map<String, Object> each_restaurant = (Map<String, Object>) ss.getValue();
                    String db_username = (String) each_restaurant.get("email");
                    String db_password = (String) each_restaurant.get("password");
                    //System.out.println(db_username.equals(username));
                    //System.out.println(db_password.equals(password));
                    if (db_username.equals(username) && db_password.equals(password)) {
                        finishedCallback.callback(true);
                        return;
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}