package no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.models;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public final class User
{
    private static List<Integer> foundCacheIds = new ArrayList<>();
    private static List<Integer> createdCacheIds = new ArrayList<>();
    private static  List<Integer> achievementsIds = new ArrayList<>();
    private static String userId;
    private static String userEmail;
    private static String name;

    private User()
    {

    }

    public static List<Integer> getCreatedCacheIds()
    {
        return createdCacheIds;
    }

    public static List<Integer> getAchievementsIds()
    {
        achievementsIds.add(1);
        achievementsIds.add(2);
        return achievementsIds;
    }

    public String getUserId() {
        return userId;
    }

    public static String getUserEmail() {
        return userEmail;
    }

    public static String getName() {
        return name;
    }

    public static List<Integer> getFoundCacheIds()
    {
        return foundCacheIds;
    }

    public static void setUserId(String newuserId) {
        userId = newuserId;
    }

    public static void setUserEmail(String newuserEmail) {
        userEmail = newuserEmail;
        name = userEmail;
    }

    public static void setName(String newname) {
        name = newname;
    }
}
