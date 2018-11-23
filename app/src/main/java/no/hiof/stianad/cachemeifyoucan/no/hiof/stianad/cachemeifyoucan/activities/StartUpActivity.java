package no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import no.hiof.stianad.cachemeifyoucan.R;

public class StartUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
        Button btnLogin = findViewById(R.id.activty_userAccount_btn_login);
        Button btnSignUp = findViewById(R.id.activity_userAccount_btn_signUp);

        mAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(
                v -> signUp()
        );

        btnLogin.setOnClickListener(
                v -> login()
        );
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
        if(currentUser != null){
            String userEmail = currentUser.getEmail();
            Toast tLogedIn = Toast.makeText(this,"Already signed in as: " + userEmail,Toast.LENGTH_SHORT);
            tLogedIn.show();
            //gå til main
            openMainActivity();
        }

    }
    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void signUp(){
        startActivity(new Intent(this, SignUpActivity.class));
    }

    public void login(){
        startActivity(new Intent(this, LoginActivity.class));
    }
}