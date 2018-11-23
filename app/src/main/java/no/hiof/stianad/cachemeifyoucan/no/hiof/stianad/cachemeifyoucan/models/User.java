package no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.models;

import java.util.ArrayList;
import java.util.List;

public class User
{
    private List<Integer> foundCacheIds = new ArrayList<>();
    private List<Integer> createdCacheIds = new ArrayList<>();
    private  List<Integer> achievementsIds = new ArrayList<>();
    private String userId = "";
    private String userEmail = "";
    private String name = "";

    public User(String userId, String userEmail)
    {
        this.userId = userId;
        this.userEmail = userEmail;
        this.name = userEmail;
    }

    public List<Integer> getCreatedCacheIds()
    {
        return createdCacheIds;
    }

    public List<Integer> getAchievementsIds()
    {
        achievementsIds.add(1);
        achievementsIds.add(2);
        return achievementsIds;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserEmail(String newuserEmail) {
        userEmail = newuserEmail;
        name = newuserEmail;
    }

    public String getName() {
        return name;
    }

    public String getUserEmail()
    {
        return userEmail;
    }

    public List<Integer> getFoundCacheIds()
    {
        return foundCacheIds;
    }

    public void setFoundCacheIds(List<Integer> foundCacheIds)
    {
        this.foundCacheIds = foundCacheIds;
    }

    public void setCreatedCacheIds(List<Integer> createdCacheIds)
    {
        this.createdCacheIds = createdCacheIds;
    }

    public void setAchievementsIds(List<Integer> achievementsIds)
    {
        this.achievementsIds = achievementsIds;
    }

    public void setUserId(String newuserId) {
        userId = newuserId;
    }

    public void setName(String newname) {
        name = newname;
    }
}
