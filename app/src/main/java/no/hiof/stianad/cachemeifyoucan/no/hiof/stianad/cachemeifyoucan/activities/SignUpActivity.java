package no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.utilities.UserManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import no.hiof.stianad.cachemeifyoucan.R;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText et_email;
    private EditText et_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        mAuth = FirebaseAuth.getInstance();

        et_email = findViewById(R.id.activity_signUp_tv_username);
        et_pass = findViewById(R.id.activity_signUp_tv_password);

        Button btnSignUp = findViewById(R.id.activity_signUp_btn_signUp);
        btnSignUp.setOnClickListener(
                v -> create()
        );

    }

    public void create(){
        String email= et_email.getText().toString();
        String password = et_pass.getText().toString();
        createUser(email,password);
    }

    public void createUser(String email, String password )
    {try
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task ->
                {
                    if (task.isSuccessful())

                    {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(SignUpActivity.this, "Welcome.",
                                Toast.LENGTH_SHORT).show();
                        UserManager.createUser(user.getUid(), user.getEmail());

                        //Just a test to se that user can have Achievements
                        UserManager.userAchievementsListAdd(1);
                        UserManager.userAchievementsListAdd(3);
                        openMainActivity();
                    } else
                    {
                        // error
                        Toast.makeText(SignUpActivity.this, "Registration failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
    catch (Exception e)
    {
        Toast.makeText(SignUpActivity.this, "Registration failed.",
                Toast.LENGTH_SHORT).show();
    }
    }

    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
