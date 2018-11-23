package no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import no.hiof.stianad.cachemeifyoucan.R;
import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.models.User;
//firebase login
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.quickstart.auth.R;

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

    public void createUser(String email, String password ){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(SignUpActivity.this, "Welcome.",
                                    Toast.LENGTH_SHORT).show();
                            User.setUserId(user.getUid());
                            User.setUserEmail(user.getEmail());
                            openMainActivity();
                        } else {
                            // error
                            Toast.makeText(SignUpActivity.this, "Registration failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
