package com.ananth.androidvolley;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.ananth.androidvolley.utils.PrefUtils;
import com.rey.material.app.ThemeManager;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ThemeManager.init(this, 1, 0, null);
        mUsername = (TextInputEditText) findViewById(R.id.username);
        if (!TextUtils.isEmpty(PrefUtils.getData("username", LoginActivity.this))) {
            startActivity(new Intent(LoginActivity.this, ProfileActivity.class).putExtra("username", PrefUtils.getData("username", LoginActivity.this)));
            finish();
        }
        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mUsername.getText().toString().trim();
                if (username.length() > 0) {
                    startActivity(new Intent(LoginActivity.this, ProfileActivity.class).putExtra("username", username));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.need_username), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
