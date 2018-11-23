package no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.models;

import java.util.ArrayList;
import java.util.List;

public final class User
{
    private static List<Integer> foundCacheIds = new ArrayList<>();
    private static List<Integer> createdCacheIds = new ArrayList<>();
    private static  List<Integer> achievementsIds = new ArrayList<>();

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

    public static List<Integer> getFoundCacheIds()
    {
        return foundCacheIds;
    }
}
