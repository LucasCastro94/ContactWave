package com.example.contactwave.DB;


import android.content.Context;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.contactwave.Pessoa;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = Pessoa.class, version = 1,exportSchema = false)
public abstract class DataBase extends RoomDatabase {

    public abstract UserDAO userDAO();

    private static volatile DataBase INSTANCE;

    public static DataBase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    DataBase.class, "ContactWave.db")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }




}














/*

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.contactwave.Pessoa;


@Database(entities = Pessoa.class,version = 1)
public abstract class DataBase extends RoomDatabase {

    private static final String DB_NAME = "contactDB.db";
    private static DataBase instance;

    public abstract UserDAO userDAO();
    public static synchronized DataBase getInstance(Context context)
    {
        if (instance == null)
        {

            instance = Room.databaseBuilder(context,DataBase.class,DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;

    }

}

 instance = (instance == null)? Room.databaseBuilder(context.getApplicationContext(),DataBase.class
        ,DB_NAME).fallbackToDestructiveMigration().build():instance;
         return instance;
 */