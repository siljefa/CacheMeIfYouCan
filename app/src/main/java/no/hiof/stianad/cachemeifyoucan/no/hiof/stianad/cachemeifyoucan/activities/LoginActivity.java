package no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import no.hiof.stianad.cachemeifyoucan.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //For new sign up profiles
        Button btnSignup = (Button) findViewById(R.id.activity_login_btn_signUp);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpActivity();
            }
        });

        //For existing user profiles
        Button btnExistingUser = (Button) findViewById(R.id.activty_login_btn_login);
        btnExistingUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExiStingUserActivity();
            }
        });
    }

    //Starts signUpActivity
    public void openSignUpActivity(){
        Intent intent1 = new Intent(this, SignUpActivity.class);
        startActivity(intent1);
    }

    //Starts existingUseractivity
    public void openExiStingUserActivity(){
        Intent intent2 = new Intent(this, ExistingUserActivity.class);
        startActivity(intent2);
    }
}