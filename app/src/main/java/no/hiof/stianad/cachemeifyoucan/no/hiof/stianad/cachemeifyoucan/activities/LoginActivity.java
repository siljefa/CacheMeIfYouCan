package no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import no.hiof.stianad.cachemeifyoucan.R;
import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.utilities.UserManager;

public class LoginActivity extends AppCompatActivity {

    LoginActivity thisActivity;
    private FirebaseAuth mAuth;
    EditText et_email;
    EditText et_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisActivity = this;
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        et_email = findViewById(R.id.activity_login_tv_username);
        et_password = findViewById(R.id.activity_login_tv_password);

        Button btnLogin = findViewById(R.id.activity_login_btn_login);
        btnLogin.setOnClickListener(
                v -> checkValues()
        );
    }

    public void checkValues(){
        //code for checking if the values are the right type email etc
        String email= et_email.getText().toString();
        String password = et_password.getText().toString();
        login(email, password);
    }

    public void login(String email, String password){

        try
        {
            //check email and pword here
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task ->
            {
                if (task.isSuccessful())
                {
                    // Sign in success
                    FirebaseUser user = mAuth.getCurrentUser();
                    UserManager.setEventListener(user.getUid(), null, thisActivity);
                } else
                {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }

                if (!task.isSuccessful())
                {
                    //error message here
                    // mStatusTextView.setText(R.string.auth_failed);
                }

            });
        }
        catch (Exception e)
        {
            Toast.makeText(LoginActivity.this, "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
