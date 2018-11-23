package no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.utilities;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    public static void setEventListener(String newUserId)
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

    public static User getUser()
    {
        if (user == null)
        {
            throw new IllegalArgumentException ("Did not get user from database before this methodeCall");
        }
        return user;
    }
}
