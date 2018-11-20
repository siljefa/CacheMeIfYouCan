package no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import no.hiof.stianad.cachemeifyoucan.R;

public class UserAccountOptions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
        Button btnLogin = findViewById(R.id.activty_userAccount_btn_login);
        Button btnSignUp = findViewById(R.id.activity_userAccount_btn_signUp);

        btnSignUp.setOnClickListener(
                v -> signUp()
        );

        btnLogin.setOnClickListener(
                v -> login()
        );
    }

    public void signUp(){
        startActivity(new Intent(this, SignUpActivity.class));
    }

    public void login(){
        startActivity(new Intent(this, LoginActivity.class));
    }
}