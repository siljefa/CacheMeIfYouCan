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
    private Button btnSignup;
    private Button btnExistingUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnSignup = (Button) findViewById(R.id.activity_login_btn_signUp);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginActivity();
            }
        });

        btnExistingUser = (Button) findViewById(R.id.activity_existingUser_btn_login);
        btnExistingUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExiStingUserActivity();
            }
        });
    }

    public void openLoginActivity(){
        Intent intent1 = new Intent(this, SignUpActivity.class);
        startActivity(intent1);
    }

    public void openExiStingUserActivity(){
        Intent intent2 = new Intent(this, ExistingUserActivity.class);
        startActivity(intent2);
    }
}