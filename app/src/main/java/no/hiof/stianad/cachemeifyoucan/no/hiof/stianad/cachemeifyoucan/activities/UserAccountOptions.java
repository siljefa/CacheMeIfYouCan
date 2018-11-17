package no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import no.hiof.stianad.cachemeifyoucan.R;

public class UserAccountOptions extends AppCompatActivity {
    private Button btnSignUp;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        //For signup
        Button btnSignUp = (Button) findViewById(R.id.activity_userAccount_btn_signUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        //For login
        Button btnLogin = (Button) findViewById(R.id.activty_userAccount_btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    //Starts signUpActivity
    public void signUp(){
        Intent intent1 = new Intent(this, SignUpActivity.class);
        startActivity(intent1);
    }

    //Starts loginActivity
    public void login(){
        Intent intent2 = new Intent(this, LoginActivity.class);
        startActivity(intent2);
    }
}