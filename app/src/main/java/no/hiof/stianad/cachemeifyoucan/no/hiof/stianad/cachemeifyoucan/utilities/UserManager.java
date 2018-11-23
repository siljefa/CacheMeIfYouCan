package no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.utilities;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.models.User;

public final class UserManager
{
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

    public static void createUser(String name, String id, String email)
    {
        User newUser = new User(name, id, email);
        addUserToDatabase(newUser);
    }

    public static void setEventListener()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("user");
        ref.addValueEventListener(new ValueEventListener()
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
                        user = snapshot.getValue(User.class);
                        user.setUserId(snapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error)
            {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public static User getUser()
    {
        return user;
    }
}
