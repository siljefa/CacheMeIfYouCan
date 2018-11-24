package no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.utilities;

import android.app.Activity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.List;

import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.activities.LoginActivity;
import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.activities.StartUpActivity;
import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.models.User;

public final class UserManager
{
    private static String userId;
    private static User user;
    private UserManager()
    {
    }

    private static void addUserToDatabase(User user)
    {
        //FireBase reference and instance
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseUser = database.getReference("user");
        databaseUser.child(user.getUserId()).setValue(user);
    }

    public static void createUser(String id, String email)
    {
        user = new User(id, email);
        addUserToDatabase(user);
    }

    public static void setEventListener(String newUserId, StartUpActivity startUpActivity, LoginActivity loginActivity)
    {
        userId = newUserId;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("user");
        ref.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot != null)
                {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        String snapshotUserId = snapshot.getKey();
                        if(snapshotUserId.equals(userId))
                        {
                            user = snapshot.getValue(User.class);
                            user.setUserId(snapshot.getKey());
                            if(loginActivity != null)
                            loginActivity.openMainActivity();
                            if(startUpActivity != null)
                            startUpActivity.openMainActivity();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error)
            {
                //Should trow exception and give message to user that login failed. Also move the user to the sign in page.
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public static void userFoundCacheListAdd(int id)
    {
        user.getFoundCacheIds().add(id);
        userLists(id, "foundCacheIds");
    }

    public static void userCreatedCacheListAdd(int id)
    {
        user.getFoundCacheIds().add(id);
        userLists(id, "foundCacheIds");
    }

    public static void userAchievementsListAdd(int id)
    {
        user.getFoundCacheIds().add(id);
        userLists(id, "foundCacheIds");
    }

    private static void userLists(int id, String name)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseUser = database.getReference("user");
        databaseUser.child(user.getUserId()).child(name).setValue(user.getFoundCacheIds());
    }

    /*public static User getUser()
    {
        if (user == null)
        {
            throw new IllegalArgumentException ("Did not get user from database before this methodeCall");
        }
        return user;
    }*/

    public static List<Integer> getCreatedCacheIds()
    {
            return Collections.unmodifiableList(user.getCreatedCacheIds());
    }

    public static List<Integer> getAchievementsIds()
    {
            return Collections.unmodifiableList(user.getAchievementsIds());
    }

    public static List<Integer> getFoundCacheIds()
    {
            return Collections.unmodifiableList(user.getFoundCacheIds());
    }

    public static String getUserId() {
        return user.getUserId();
    }

    public static String getName() {
        return user.getName();
    }

    public static String getUserEmail()
    {
        return user.getUserEmail();
    }

}
