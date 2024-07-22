package com.www233.uitest.flowtest;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

@Database(version = 1, entities = {TextDataItem.class}, exportSchema=false)
abstract class TextDatabase extends RoomDatabase {
    public abstract TextDataDao textDataDao();

    private static volatile TextDatabase instance;

    static public TextDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (TextDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), TextDatabase.class, "db")
                            .addMigrations().allowMainThreadQueries().build();
                }
            }
        }
        return instance;
    }
}