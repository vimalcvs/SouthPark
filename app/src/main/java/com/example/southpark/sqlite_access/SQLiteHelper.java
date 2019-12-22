package com.example.southpark.sqlite_access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.southpark.model.Episode;
import com.example.southpark.model.Series;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.getExternalStorageDirectory;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "southPark.db";
    private static final int DB_VERSION = 1; //required for the constructor
    private static final String TABLE_NAME = "episodes";

    private String fileName = "southPark.json";
    private String appName = "southPark";
    private String path = getExternalStorageDirectory() + "/" + appName + "/" + fileName;
    private String jsonString = null;

    private Context context;

    SQLiteHelper(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        SQLiteDatabase db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d(TAG, "At SQLiteHelper");

        createSQLIteTable(db);
        try {
            parseJsonAndInsertToSQLIte(db);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean tableIsEmpty(SQLiteDatabase db) {

        Cursor cur = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
        cur.moveToFirst();
        int count = cur.getInt(0);

        if (count > 0) {

            Log.i(TAG, "The table is not empty");
            cur.close();
            return false;

        } else {

            Log.i(TAG, "The table is empty");
            cur.close();
            return true;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private void createSQLIteTable(SQLiteDatabase db) {

        //creating a table for SQLite
        String CREATE_SQL_TABLE_STRING = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + " ("
                + Episode.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,"
                + Episode.NAME + " TEXT,"
                + Episode.SUMMARY + " TEXT,"
                + Episode.IMAGE + " TEXT"
                + ")";

        Log.i(TAG, "created sql table: " + CREATE_SQL_TABLE_STRING);

        db.execSQL(CREATE_SQL_TABLE_STRING);

        String CREATE_SQL_TABLE_STRING1 = "CREATE TABLE IF NOT EXISTS " + "seriesData"
                + " ("
                + "id" + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,"
                + Series.SUMMARY + " TEXT,"
                + Series.IMAGE + " TEXT"
                + ")";

        db.execSQL(CREATE_SQL_TABLE_STRING1);
        Log.i(TAG, "created sql table: " + CREATE_SQL_TABLE_STRING1);
    }

    private void parseJsonAndInsertToSQLIte(SQLiteDatabase db) throws JSONException {
        // parsing the json
        JSONObject object = new JSONObject(getJsonFileData());

        String summary = object.getString("summary");

        String element1 = Jsoup.parse(summary).text();
        System.out.println("element:"+element1);

        if (tableIsEmpty(db)) {
            jsonString = getJsonFileData();
            JSONObject seriesObject = new JSONObject(jsonString);

            ContentValues seriesInsert = new ContentValues();

            String imageString = seriesObject.getJSONObject("image").getString("original");
            String summaryString = seriesObject.getString("summary");
            String parsedSummary = Jsoup.parse(summaryString).text();

            seriesInsert.put(Series.IMAGE, imageString);
            seriesInsert.put(Series.SUMMARY, parsedSummary);

            long res = db.insert("seriesData",null, seriesInsert);

            ContentValues episodesInsert = new ContentValues();

            JSONArray episodesArray = seriesObject.getJSONObject("_embedded")
                    .getJSONArray("episodes");

            for (int i = 0; i < episodesArray.length(); i++) {
                JSONObject episode = episodesArray.getJSONObject(i);
                String episodeName = episode.getString("name");
                String episodeImage = episode.getJSONObject("image").getString("original");
                String episodeSummary = episode.getString("summary");

                String parsed = Jsoup.parse(episodeSummary).text();

                episodesInsert.put(Episode.NAME, episodeName);
                episodesInsert.put(Episode.IMAGE, episodeImage);
                episodesInsert.put(Episode.SUMMARY, parsed);

                long res1 = db.insert(TABLE_NAME,null, episodesInsert);
            }
        }
    }

    private String getJsonFileData() {
        //loading the jsonString
        try {
            InputStream in = new FileInputStream(new File(path));
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder output = new StringBuilder();
            while ((jsonString = reader.readLine()) != null) {
                output.append(jsonString);
            }

            in.close();
            jsonString = output.toString();
            Log.d(ContentValues.TAG, "the jsonString was loaded");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return jsonString;
    }
}