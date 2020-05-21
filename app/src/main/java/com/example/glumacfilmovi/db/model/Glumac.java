package com.example.glumacfilmovi.db.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Glumac.TABLE_NAME_USERS)
public class Glumac {

    public static final String TABLE_NAME_USERS = "glumac";

    public static final String FIELD_NAME_ID = "id";
    public static final String FIELD_NAME_IME = "mIme";
    public static final String FIELD_NAME_PREZIME = "mPrezime";
    public static final String FIELD_NAME_BIOGRAFIJA = "mBiografija";
    public static final String FIELD_NAME_RATING = "mOcena";
    public static final String FIELD_NAME_DATUM = "mDatum";
    public static final String FIELD_NAME_IMAGE = "mSlika";
    public static final String FIELD_NAME_FILMOVI = "filmovi";


    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int mId;

    @DatabaseField(columnName = FIELD_NAME_IME)
    private String mIme;

    @DatabaseField(columnName = FIELD_NAME_PREZIME)
    private String mPrezime;

    @DatabaseField(columnName = FIELD_NAME_BIOGRAFIJA)
    private String mBiografija;

    @DatabaseField(columnName = FIELD_NAME_RATING)
    private float mOcena;

    @DatabaseField(columnName = FIELD_NAME_DATUM)
    private String mDatum;

    @DatabaseField(columnName = FIELD_NAME_IMAGE)
    private String mSlika;

    @ForeignCollectionField(columnName = Glumac.FIELD_NAME_FILMOVI, eager = true)
    private ForeignCollection<FavoriteFIlmovi> mFilmovi;

    public Glumac() {
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmIme() {
        return mIme;
    }

    public void setmIme(String mIme) {
        this.mIme = mIme;
    }

    public String getmPrezime() {
        return mPrezime;
    }

    public void setmPrezime(String mPrezime) {
        this.mPrezime = mPrezime;
    }

    public String getmBiografija() {
        return mBiografija;
    }

    public void setmBiografija(String mBiografija) {
        this.mBiografija = mBiografija;
    }

    public float getmOcena() {
        return mOcena;
    }

    public void setmOcena(float mOcena) {
        this.mOcena = mOcena;
    }

    public String getmDatum() {
        return mDatum;
    }

    public void setmDatum(String mDatum) {
        this.mDatum = mDatum;
    }

    public String getmSlika() {
        return mSlika;
    }

    public void setmSlika(String mSlika) {
        this.mSlika = mSlika;
    }

    public ForeignCollection<FavoriteFIlmovi> getmFilmovi() {
        return mFilmovi;
    }

    public void setmFilmovi(ForeignCollection<FavoriteFIlmovi> mFilmovi) {
        this.mFilmovi = mFilmovi;
    }

    @Override
    public String toString() {
        return mIme + " " + mPrezime;
    }
}

