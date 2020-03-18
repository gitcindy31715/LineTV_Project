package com.example.cindy31715.linetv_project;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by cindy31715 on 2020/3/16.
 */

public class ClassDramaInfo implements Serializable{
    private int drama_id;
    private String name, created_at, thumb;
    private long total_views;
    private double rating;

    public ClassDramaInfo(int drama_id, String name, long total_views, String created_at, String thumb, double rating){
        this.drama_id=drama_id; this.name=name; this.created_at=created_at;
        this.thumb=thumb; this.total_views=total_views; this.rating=rating;
    }

    public int getDrama_id(){return drama_id;}
    public String getName(){return name;}
    public String getCreated_at(){return created_at;}
    public String getThumb(){return thumb;}
    public long getTotal_views(){return total_views;}
    public double getRating (){return rating;}


}
