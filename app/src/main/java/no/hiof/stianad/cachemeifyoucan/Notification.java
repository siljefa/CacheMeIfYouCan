package no.hiof.stianad.cachemeifyoucan;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class Notification extends Application {
    public static final String CHANNEL_ID = "channel";

    @Override
    public void onCreate() {
        super.onCreate();

        //Creates notifications
        createNotificationChannels();
    }

    /*Checks for Android version because notification channels are only for
    android oreo 26 and higher
    */
    private void createNotificationChannels(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "Channel",
                    //Notification has highest priority
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
