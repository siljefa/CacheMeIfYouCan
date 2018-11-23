package no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.models;

import java.sql.Statement;
import java.util.ArrayList;

public final class User
{
    private static String userId;
    private static String userEmail;
    private static String name = userEmail;
    private static ArrayList<Integer> CacheIds = new ArrayList<>();

    private User(int i)
    {

    }

    public String getUserId() {
        return userId;
    }

    public static void setUserId(String newuserId) {
        userId = newuserId;
    }

    public static String getUserEmail() {
        return userEmail;
    }

    public static void setUserEmail(String newuserEmail) {
        userEmail = newuserEmail;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String newname) {
        name = newname;
    }

    public static ArrayList<Integer> getCacheIds()
    {
        return CacheIds;
    }
}
