package no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.utilities;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class AppNotification extends Application {
    public static final String CHANNEL_ID = "Geocaching";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }
    //Creates notification channel
    private void createNotificationChannel(){
        //Checks version of Android. If API is 26 or higher notification channel group is implemented.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                     "Geocaching",
                    //Sets the importance to the highest level, however a lower level can work for this app
                    NotificationManager.IMPORTANCE_HIGH
            );
            //Gives input to the NotificationManager
            NotificationManager manager = getSystemService(NotificationManager.class);

            //NotificatonChannel is created when app is started
            manager.createNotificationChannel(serviceChannel);
        }

    }
}
