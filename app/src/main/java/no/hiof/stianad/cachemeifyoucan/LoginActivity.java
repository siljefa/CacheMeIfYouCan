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

    private static EditText username;
    private static EditText password;
    private static Button login_button;
    private static Button createAccount_button;  //In beta version this will have functionality. In alpha version this merely included for structure.
    int loginAttempts = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Runs loginButton method
        LoginButton();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    //Method that runs when login button is clicked
    public void LoginButton(){
        username = (EditText)findViewById(R.id.editText_username);  //Username
        password = (EditText)findViewById(R.id.editText_password);  //Password
        login_button = (Button)findViewById(R.id.button_login);     //Loginbutton
        createAccount_button = (Button)findViewById(R.id.button_createUser);    //Create User

        login_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*Checks if username and password matches. In beta version this will have functionality that checks against a database of stored user information.
                         In alpha version this is merely set up to show the structure of the app.
                         */
                        if (username.getText().toString().equals("user")
                                && password.getText().toString().equals("pass")) {
                            //When user information input is correct this opens up the geocache app to the main page of it
                            startApp();
                        }
                        else{
                            //Displays message about incorrect user input
                            Toast.makeText(LoginActivity.this, "Username and password does not match", Toast.LENGTH_SHORT).show();
                            //if more than 3 attempts login button is disabled
                            loginAttempts--;
                            if(loginAttempts ==0)
                                login_button.setEnabled(false);
                        }
                    }
                });
    }

    public void startApp(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}