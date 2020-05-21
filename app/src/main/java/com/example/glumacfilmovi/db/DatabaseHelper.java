package com.example.glumacfilmovi.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import com.example.glumacfilmovi.db.model.FavoriteFIlmovi;
import com.example.glumacfilmovi.db.model.Filmovi;
import com.example.glumacfilmovi.db.model.Glumac;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "ormlite.db";

    private static final int DATABASE_VERSION = 1;

    private Dao<Glumac, Integer> mGlumacDao = null;
    private Dao<Filmovi, Integer> mFilmoviDao = null;
    private Dao<FavoriteFIlmovi, Integer> mFilmDao = null;

    public DatabaseHelper(Context context) {
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

        try {
            TableUtils.createTable( connectionSource, Glumac.class );
            TableUtils.createTable(connectionSource, Filmovi.class);
            TableUtils.createTable(connectionSource, FavoriteFIlmovi.class);
        } catch (SQLException e) {
            throw new RuntimeException( e );
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {


        try {
            TableUtils.dropTable( connectionSource, Glumac.class, true );
            TableUtils.createTable( connectionSource, Filmovi.class );
            TableUtils.createTable( connectionSource, FavoriteFIlmovi.class );
        } catch (SQLException e) {
            throw new RuntimeException( e );
        }
    }


    public Dao<Glumac, Integer> getmGlumacDao() throws SQLException {
        if (mGlumacDao == null) {
            mGlumacDao = getDao( Glumac.class );
        }
        return mGlumacDao;
    }

    public Dao<FavoriteFIlmovi, Integer> getmFavoriteFilmoviDao() throws SQLException {
        if (mFilmDao == null) {
            mFilmDao = getDao( FavoriteFIlmovi.class );
        }
        return mFilmDao;
    }

    public Dao<Filmovi, Integer> getFilmoviDao() throws SQLException {
        if (mFilmoviDao == null) {
            mFilmoviDao = getDao( Filmovi.class );
        }

        return mFilmoviDao;
    }

    @Override
    public void close() {
        mGlumacDao = null;
        mFilmDao = null;
        mFilmoviDao = null;

        super.close();
    }
}
