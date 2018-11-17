package no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import no.hiof.stianad.cachemeifyoucan.R;

public class SignUpActivity extends AppCompatActivity {
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //For signup
        Button btnSignUp = (Button) findViewById(R.id.activity_signUp_btn_signUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });

    }

    //Starts MainActivity
    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
