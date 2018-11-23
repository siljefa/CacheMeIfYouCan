package no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.models;

import java.util.ArrayList;

public final class User
{
    private static ArrayList<Integer> CacheIds = new ArrayList<>();
    private static  ArrayList<Integer> achivementIds = new ArrayList<>();

    private User(int i)
    {

    }

    public static ArrayList<Integer> getCacheIds()
    {
        return CacheIds;
    }
}
