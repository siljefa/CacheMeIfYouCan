package no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import no.hiof.stianad.cachemeifyoucan.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = findViewById(R.id.activity_login_btn_login);
        btnLogin.setOnClickListener(
                v -> openMainActivity()
        );
    }

    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
