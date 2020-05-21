package com.example.glumacfilmovi.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.glumacfilmovi.R;
import com.example.glumacfilmovi.adapters.AdapterFavoritFilm;
import com.example.glumacfilmovi.db.DatabaseHelper;
import com.example.glumacfilmovi.db.model.FavoriteFIlmovi;
import com.example.glumacfilmovi.db.model.Glumac;
import com.example.glumacfilmovi.dialog.AboutDialog;
import com.example.glumacfilmovi.tools.Tools;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

public class DetailsActivityGlumac extends AppCompatActivity implements AdapterFavoritFilm.OnItemClickListener {

    private Toolbar toolbar;

    private static final int SELECT_PICTURE = 1;
    private static final String TAG = "PERMISSIONS";

    private String imagePath = null;
    private ImageView preview;

    private SharedPreferences prefs;
    public static final String NOTIF_CHANNEL_ID = "notif_channel_007";

    private DatabaseHelper databaseHelper;

    public static String KEY = "KEY";

    private Glumac glumac;

    private AdapterFavoritFilm adapter;

    private List<FavoriteFIlmovi> list;
    private RecyclerView rec_list;


    private AlertDialog dijalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_details_glumac );

        setupToolbar();

        fillData();

        createNotificationChannel();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void addListaFilmova() {
        Intent i = new Intent( DetailsActivityGlumac.this, ListaFilmova.class);
        i.putExtra("position", glumac.getmId());
        startActivity(i);
    }

    private void fillData(){
        int key = getIntent().getExtras().getInt( MainActivityGlumac.KEY);


        try {
            glumac = getDatabaseHelper().getmGlumacDao().queryForId(key);

            TextView ime = findViewById( R.id.detalji_ime);
            TextView prezime = findViewById(R.id.detalji_prezime);
            TextView godinaRodjenja = findViewById(R.id.detalji_godinaRodjenja);
            TextView biografija = findViewById(R.id.detalji_biografija);
            RatingBar ocena = findViewById(R.id.detalji_ocena);
            ImageView slika = findViewById( R.id.slikaGlumca);

            Uri mUri = Uri.parse(glumac.getmSlika());
            slika.setImageURI(mUri);

            ime.setText(glumac.getmIme());
            prezime.setText(glumac.getmPrezime());
            godinaRodjenja.setText(glumac.getmDatum());
            biografija.setText(glumac.getmBiografija());
            ocena.setRating(glumac.getmOcena());

            InputStream is = null;
            try {
                is = getApplicationContext().getAssets().open(glumac.getmSlika());
                Drawable drawable = Drawable.createFromStream(is, null);
                slika.setImageDrawable(drawable);
            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        rec_list = findViewById(R.id.second_rView);


        try {
            list = getDatabaseHelper().getmFavoriteFilmoviDao().queryBuilder()
                    .where()
                    .eq(FavoriteFIlmovi.FIELD_NAME_USERS, glumac.getmId())
                    .query();


            adapter = new AdapterFavoritFilm( DetailsActivityGlumac.this, DetailsActivityGlumac.this,list);

            rec_list.setHasFixedSize(true);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( DetailsActivityGlumac.this);
            rec_list.setLayoutManager(layoutManager);

            rec_list.setAdapter(adapter);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void deleteGlumac() {
        try {
            getDatabaseHelper().getmGlumacDao().delete( glumac );

            String tekstNotifikacija = "Glumac je obrisan";

            boolean toast = prefs.getBoolean( getString( R.string.toast_key ), false );
            boolean notif = prefs.getBoolean( getString( R.string.notif_key ), false );

            if (toast) {
                Toast.makeText( this, tekstNotifikacija, Toast.LENGTH_LONG ).show();
            }

            if (notif) {
                NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
                NotificationCompat.Builder builder = new NotificationCompat.Builder( this, NOTIF_CHANNEL_ID );

                builder.setSmallIcon( android.R.drawable.ic_menu_delete );
                builder.setContentTitle( "Notifikacija" );
                builder.setContentText( tekstNotifikacija );

                Bitmap bitmap = BitmapFactory.decodeResource( getResources(), R.drawable.heart );

                builder.setLargeIcon( bitmap );
                notificationManager.notify( 1, builder.build() );
            }
            finish();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void refreshGlumac() {
        int key = getIntent().getExtras().getInt( MainActivityGlumac.KEY );

        try {
            glumac = getDatabaseHelper().getmGlumacDao().queryForId( key );

            TextView ime = findViewById( R.id.detalji_ime );
            TextView prezime = findViewById( R.id.detalji_prezime );
            TextView godinaRodjenja = findViewById( R.id.detalji_godinaRodjenja );
            TextView biografija = findViewById( R.id.detalji_biografija );
            RatingBar ocena = findViewById( R.id.detalji_ocena );
            ImageView imageSlika = findViewById( R.id.slikaGlumca );

            ime.setText( glumac.getmIme() );
            prezime.setText( glumac.getmPrezime() );
            godinaRodjenja.setText( glumac.getmDatum() );
            biografija.setText( glumac.getmBiografija() );
            ocena.setRating( glumac.getmOcena() );

            Uri mUri = Uri.parse( glumac.getmSlika() );
            imageSlika.setImageURI( mUri );


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void refresh() {

        List<FavoriteFIlmovi> list = null;
        try {
            list = getDatabaseHelper().getmFavoriteFilmoviDao().queryBuilder()
                    .where()
                    .eq( FavoriteFIlmovi.FIELD_NAME_USERS, glumac.getmId() )
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (adapter != null) {
            adapter.clear();
            adapter.addAll( list );
            adapter.notifyDataSetChanged();
        } else {
            adapter = new AdapterFavoritFilm( DetailsActivityGlumac.this, DetailsActivityGlumac.this, list );
            rec_list.setHasFixedSize( true );

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( DetailsActivityGlumac.this );
            rec_list.setLayoutManager( layoutManager );

            rec_list.setAdapter( adapter );
        }

    }

    private void editGlumac() {
        final Dialog dialog = new Dialog( this );
        dialog.setContentView( R.layout.add_layout );
        dialog.setTitle( "Unesite podatke" );
        dialog.setCanceledOnTouchOutside( false );

        Button chooseBtn = dialog.findViewById( R.id.choose1 );
        chooseBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preview = dialog.findViewById( R.id.preview_image1 );
                selectPicture();
            }
        } );

        Button add = dialog.findViewById( R.id.add_glumac );
        add.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText glumacPrezime = dialog.findViewById( R.id.glumac_prezime );
                EditText glumacIme = dialog.findViewById( R.id.glumac_ime );
                EditText glumacBiografija = dialog.findViewById( R.id.glumac_biografija );
                EditText glumacDatum = dialog.findViewById( R.id.glumac_datum );
                EditText glumacRating = dialog.findViewById( R.id.glumac_rating );

                if (preview == null || imagePath == null) {
                    Toast.makeText( DetailsActivityGlumac.this, "Slika mora biti izabrana", Toast.LENGTH_SHORT ).show();
                    return;

                }
                if (Tools.validateInput( glumacIme )
                        && Tools.validateInput( glumacPrezime )
                        && Tools.validateInput( glumacBiografija )
                        && Tools.validateInput( glumacDatum )
                        && Tools.validateInput( glumacRating )

                ) {

                    glumac.setmIme( glumacIme.getText().toString() );
                    glumac.setmPrezime( glumacPrezime.getText().toString() );
                    glumac.setmDatum( glumacDatum.getText().toString() );
                    glumac.setmOcena( Float.parseFloat( glumacRating.getText().toString() ) );
                    glumac.setmBiografija( glumacBiografija.getText().toString() );
                    glumac.setmSlika( imagePath );

                    try {
                        getDatabaseHelper().getmGlumacDao().update( glumac );


                        String tekstNotifikacija = "Izmenjeni su podaci";

                        boolean toast = prefs.getBoolean( getString( R.string.toast_key ), false );
                        boolean notif = prefs.getBoolean( getString( R.string.notif_key ), false );

                        if (toast) {
                            Toast.makeText( DetailsActivityGlumac.this, tekstNotifikacija, Toast.LENGTH_LONG ).show();

                        }

                        if (notif) {
                            NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
                            NotificationCompat.Builder builder = new NotificationCompat.Builder( DetailsActivityGlumac.this, NOTIF_CHANNEL_ID );

                            builder.setSmallIcon( android.R.drawable.ic_menu_edit );
                            builder.setContentTitle( "Notifikacija" );
                            builder.setContentText( tekstNotifikacija );

                            Bitmap bitmap = BitmapFactory.decodeResource( getResources(), R.drawable.heart );

                            builder.setLargeIcon( bitmap );
                            notificationManager.notify( 1, builder.build() );

                        }
                        reset();
                        refreshGlumac();


                    } catch (NumberFormatException e) {
                        Toast.makeText( DetailsActivityGlumac.this, "Rating mora biti broj", Toast.LENGTH_SHORT ).show();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    dialog.dismiss();


                }
            }
        } );

        Button cancel = dialog.findViewById( R.id.cancel );
        cancel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        } );

        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_details_glumac, menu );
        return super.onCreateOptionsMenu( menu );
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                editGlumac();
                setTitle( "Izmena" );
                break;
            case R.id.delete:
                deleteGlumac();
                setTitle( "Brisanje" );
                break;
            case R.id.search:
                addListaFilmova();
                setTitle( "Pretraga" );
                break;
            case android.R.id.home:
                startActivity( new Intent( this, MainActivityGlumac.class ) );
                break;

        }

        return super.onOptionsItemSelected( item );
    }

    public void setupToolbar() {
        toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        toolbar.setTitleTextColor( Color.WHITE );
        toolbar.setSubtitle( "Detalji glumca" );
        //toolbar.setLogo( R.mipmap.ic_launcher );


        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled( true );
            actionBar.setHomeAsUpIndicator( R.drawable.back );
            actionBar.setHomeButtonEnabled( true );
            actionBar.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            if (selectedImage != null) {
                Cursor cursor = getContentResolver().query( selectedImage, filePathColumn, null, null, null );
                if (cursor != null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex( filePathColumn[0] );
                    imagePath = cursor.getString( columnIndex );
                    cursor.close();

                    if (preview != null) {
                        preview.setImageBitmap( BitmapFactory.decodeFile( imagePath ) );
                    }
                }
            }
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission( Manifest.permission.WRITE_EXTERNAL_STORAGE )
                    == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission( Manifest.permission.READ_EXTERNAL_STORAGE )
                            == PackageManager.PERMISSION_GRANTED) {

                return true;
            } else {


                ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 1 );
                return false;
            }
        } else {

            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Log.v( TAG, "Permission: " + permissions[0] + "was " + grantResults[0] );
        }
    }

    private void selectPicture() {
        if (isStoragePermissionGranted()) {
            Intent i = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
            startActivityForResult( i, SELECT_PICTURE );
        }
    }

    private void reset() {
        imagePath = "";
        preview = null;
    }

    @Override
    protected void onResume() {
        refresh();
        super.onResume();

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }

    }

    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper( this, DatabaseHelper.class );
        }
        return databaseHelper;
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Channel";
            String description = "Description of My Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel( NOTIF_CHANNEL_ID, name, importance );
            channel.setDescription( description );

            NotificationManager notificationManager = getSystemService( NotificationManager.class );
            notificationManager.createNotificationChannel( channel );
        }
    }

    private void showDialog() {
        if (dijalog == null) {
            dijalog = new AboutDialog( DetailsActivityGlumac.this ).prepareDialog();
        } else {
            if (dijalog.isShowing()) {
                dijalog.dismiss();
            }
        }
        dijalog.show();
    }

    @Override
    public void myOnItemClick(int position) {
        FavoriteFIlmovi f =  list.get(position);

        Intent i = new Intent( DetailsActivityGlumac.this, FilmDetails.class);
        i.putExtra(KEY, f.getmImdbId());
        i.putExtra("id", f.getId());
        startActivity(i);
    }
}
