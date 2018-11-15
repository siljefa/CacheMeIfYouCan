package no.hiof.stianad.cachemeifyoucan;

import android.app.Notification;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static no.hiof.stianad.cachemeifyoucan.Notification.CHANNEL_ID;

public class TestingNotificationActivity extends AppCompatActivity {
    private NotificationManagerCompat notificationManager;
    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        notificationManager = NotificationManagerCompat.from(this);

        editText = findViewById(R.id.edit_text_title);

    }

    public void sendOnChannel(View v){
        String title = editText.getText().toString();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                //Notification type
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);

    }
}
