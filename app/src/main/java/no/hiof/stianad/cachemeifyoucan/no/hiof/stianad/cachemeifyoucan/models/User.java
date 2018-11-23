package no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.models;

import java.util.ArrayList;
import java.util.List;

public final class User
{
    private static List<Integer> CacheIds = new ArrayList<>();
    private static  List<Integer> achievementsIds = new ArrayList<>();

    private User()
    {

    }

    public static List<Integer> getAchievementsIds()
    {
        achievementsIds.add(1);
        achievementsIds.add(2);
        return achievementsIds;
    }

    public static List<Integer> getCacheIds()
    {
        return CacheIds;
    }
}
