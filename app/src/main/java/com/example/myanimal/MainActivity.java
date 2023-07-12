package com.example.myanimal;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView coinCountTextView;
    private int coinCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coinCountTextView = findViewById(R.id.coinCountTextView);
        updateCoinCount(); // Update the initial coin count

        // Example: Increase the coin count by 1 when the toolbar is clicked
        toolbar.setOnClickListener(v -> {
            coinCount++;
            updateCoinCount();
        });
    }

    private void updateCoinCount() {
        coinCountTextView.setText(String.valueOf(coinCount));
    }
}
