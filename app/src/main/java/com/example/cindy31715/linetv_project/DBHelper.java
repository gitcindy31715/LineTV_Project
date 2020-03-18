package com.example.cindy31715.linetv_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by cindy31715 on 2020/3/19.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String TableName = "DramaList";
    private static final String crTBsql =
            "CREATE TABLE "+TableName+"("+
            "drama_id integer not null,"+
            "name varchar(30) not null,"+
            "total_views integer,"+
            "`created_at` varchar(30) not null,"+
            "thumb binary,"+
            "rating real, primary key(drama_id));";
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "drama.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(crTBsql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists "+TableName);
        onCreate(db);
    }

    public long insertDrama(ClassDramaInfo classDramaInfo){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues rec = new ContentValues();
        rec.put("drama_id",classDramaInfo.getDrama_id());
        rec.put("name",classDramaInfo.getName());
        rec.put("total_views",classDramaInfo.getTotal_views());
        rec.put("created_at",classDramaInfo.getCreated_at());
        Log.d("iii",""+classDramaInfo.getCreated_at());
//        rec.put("thumb",);
        rec.put("rating",classDramaInfo.getRating());
        long rowID = db.insert(TableName,null,rec);
        db.close();
        return rowID;
    }
}
