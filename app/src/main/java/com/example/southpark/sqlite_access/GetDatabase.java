package com.example.southpark.sqlite_access;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.southpark.model.Episode;
import com.example.southpark.model.Series;

import java.util.ArrayList;

public class GetDatabase {

    private static final int DB_VERSION = 1; //required for the constructor
    private static final String dbName = "southPark";

    private SQLiteOpenHelper sqLiteOpenHelper;
    private SQLiteDatabase db ;

    public GetDatabase(Context context) {
        Log.d("GetDatabase", "Cont.");

        this.sqLiteOpenHelper = new SQLiteHelper(context, dbName, null, DB_VERSION);
    }

    public void open() {
        db = sqLiteOpenHelper.getWritableDatabase();
    }

    public void close() {
        if (sqLiteOpenHelper != null) {
            sqLiteOpenHelper.close();
        }
    }

    public ArrayList<String> getSeriesArray() {
        String[] columns = {
                Series.IMAGE,
                Series.SUMMARY
        };
        ArrayList<String> seriesData = new ArrayList<>();

//        String [] seriesArray = new String[2];
        Cursor cursor = db.query("seriesData", //Table to query
                columns,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {

            String image = cursor.getString(cursor.getColumnIndex("image"));
            String summary = cursor.getString(cursor.getColumnIndex("summary"));
            seriesData.add(image);
            seriesData.add(summary);
//            seriesArray[0] = image;
//            seriesArray[0] = summary;
        }
        cursor.close();

        return seriesData;
    }
    public ArrayList<Episode> getEpisodes() {
        String[] columns = {
                Episode.ID,
                Episode.NAME,
                Episode.SUMMARY,
                Episode.IMAGE
        };

        ArrayList<Episode> episodesList = new ArrayList<>();

        Cursor cursor = db.query("episodes", //Table to query
                columns,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {

            do {
                Episode episode = new Episode();
                episode.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Episode.ID))));
                episode.setName(cursor.getString(cursor.getColumnIndex("name")));
                episode.setImage(cursor.getString(cursor.getColumnIndex("image")));
                episode.setSummary(cursor.getString(cursor.getColumnIndex("summary")));

                episodesList.add(episode);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return episodesList;
    }

//    public List<String> getTimeZones(Cursor cursor) {
//        return Arrays.asList((cursor.getString(cursor.getColumnIndex(Country.TIME_ZONES))).split(",",0));
//    }

//    public List<String> getLatLng(Cursor cursor) {
//        return Collections.singletonList((cursor.getString(cursor.getColumnIndex(LATLNG))));
//    }

    public Cursor getData(int id) {
        return db.rawQuery( "select * from countries where id="+id+"", null );
    }
}