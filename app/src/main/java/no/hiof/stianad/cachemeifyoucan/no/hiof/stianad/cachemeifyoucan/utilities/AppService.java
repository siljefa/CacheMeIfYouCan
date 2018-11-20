package no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.utilities;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import no.hiof.stianad.cachemeifyoucan.R;
import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.activities.MainActivity;

import static no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.utilities.AppNotification.CHANNEL_ID;

public class AppService extends Service {
    public String headline = "Cache MeIfYouCan";
    public String showTitle = "Discover a world of Geocaching";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    //Starts when service starts
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

       Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
               //Headline of notification
               .setContentTitle(headline)
               //Text of notification
               .setContentText(showTitle)
               //Displays map icon in notification
               .setSmallIcon(R.drawable.ic_map)
               //Opens MainActivity when notification is clicked
               .setContentIntent(pendingIntent)
               .build();

       //Starts foreground service
       startForeground(1, notification);

       //Stops service
        //stopSelf();

       /* After system is killed, it displays it again. Highest value of redisplay however
        a lower value would work for the app. */
       return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
