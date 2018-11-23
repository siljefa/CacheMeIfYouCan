package no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.models;

public class Achievement {
    public String name;
    public String description;
    public int id;

    public Achievement(String name, String description)
    {
        this.description = description;
        this.name = name;
    }

    public Achievement(){

    }

    //public void setDescription(String description){this.description = description;}
    //public void setName(String name){this.name = name;}
}
