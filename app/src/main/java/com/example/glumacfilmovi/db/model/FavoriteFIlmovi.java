package com.example.glumacfilmovi.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = FavoriteFIlmovi.TABLE_NAME_USERS)

public class FavoriteFIlmovi {

    public static final String TABLE_NAME_USERS = "film";

    public static final String FIELD_NAME_ID = "id";
    public static final String FIELD_NAME_NAZIV = "mNaziv";
    public static final String FIELD_NAME_IMDB_ID = "imdbID";
    public static final String FIELD_NAME_GODINE = "mGodine";
    public static final String FIELD_NAME_IMAGE = "mImage";
    public static final String FIELD_NAME_TYPE = "mType";

    public static final String FIELD_NAME_USERS = "glumac";




    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = FIELD_NAME_NAZIV)
    private String mNaziv;

    @DatabaseField(columnName = FIELD_NAME_IMDB_ID)
    private String mImdbId;

    @DatabaseField(columnName = FIELD_NAME_GODINE)
    private String mGodina;

    @DatabaseField(columnName = FIELD_NAME_IMAGE)
    private String mImage;

    @DatabaseField(columnName = FIELD_NAME_TYPE)
    private String mType;

    @DatabaseField(columnName = FIELD_NAME_USERS, foreign = true, foreignAutoRefresh = true)
    private Glumac mGlumac;

    public FavoriteFIlmovi() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getmNaziv() {
        return mNaziv;
    }

    public void setmNaziv(String mNaziv) {
        this.mNaziv = mNaziv;
    }

    public String getmImdbId() {
        return mImdbId;
    }

    public void setmImdbId(String mImdbId) {
        this.mImdbId = mImdbId;
    }

    public String getmGodina() {
        return mGodina;
    }

    public void setmGodina(String mGodina) {
        this.mGodina = mGodina;
    }

    public Glumac getmGlumac() {
        return mGlumac;
    }

    public void setmGlumac(Glumac mGlumac) {
        this.mGlumac = mGlumac;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    @Override
    public String toString() {
        return mNaziv + " (" + mGodina + ") ";
    }
}
