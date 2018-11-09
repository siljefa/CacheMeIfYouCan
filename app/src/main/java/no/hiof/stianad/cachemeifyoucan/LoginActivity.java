package no.hiof.stianad.cachemeifyoucan;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private Button btnSignup;//For user signup
    private Button btnExistingUser;//For existing user accounts

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //When button is clicked it takes the user to the signup activity
        btnSignup = (Button) findViewById(R.id.activity_login_btn_signUp);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpActivity();
            }
        });

        //When button is clicked it takes user to existing user activity
        btnExistingUser = (Button) findViewById(R.id.activity_existingUser_btn_login);
        btnExistingUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExiStingUserActivity();
            }
        });
    }

    //Method that start signUpActivity
    public void openSignUpActivity(){
        Intent intent1 = new Intent(this, SignUpActivity.class);
        startActivity(intent1);
    }

    //Method that starts existingUseractivity
    public void openExiStingUserActivity(){
        Intent intent2 = new Intent(this, ExistingUserActivity.class);
        startActivity(intent2);
    }
}