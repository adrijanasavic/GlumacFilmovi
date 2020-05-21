package com.example.glumacfilmovi.activities;

import static com.example.glumacfilmovi.net.MyServiceContract.APIKEY;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.glumacfilmovi.R;
import com.example.glumacfilmovi.db.DatabaseHelper;
import com.example.glumacfilmovi.net.MyService;
import com.example.glumacfilmovi.net.model2.Detail;
import com.example.glumacfilmovi.tools.Tools;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilmDetails extends AppCompatActivity {

    private Toolbar toolbar;

    private DatabaseHelper databaseHelper;

    private Detail detalji;

    private SharedPreferences prefs;

    public static final String NOTIF_CHANNEL_ID = "notif_channel_007";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_film_details );

        setupToolbar();

        createNotificationChannel();
        prefs = PreferenceManager.getDefaultSharedPreferences( this );

    }

    private void getDetail(String imdbKey) {
        HashMap<String, String> queryParams = new HashMap<>();

        queryParams.put( "apikey", APIKEY );
        queryParams.put( "i", imdbKey );


        Call<Detail> call = MyService.apiInterface().getMovieData( queryParams );
        call.enqueue( new Callback<Detail>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Detail> call, Response<Detail> response) {
                if (response.code() == 200) {
                    Log.d( "REZ", "200" );

                    detalji = response.body();
                    if (detalji != null) {
                        fillData( detalji );


                    }
                }
            }



            private void fillData(Detail detalji) {
                ImageView image = FilmDetails.this.findViewById( R.id.detalji_slika );

                Picasso.with( FilmDetails.this ).load( detalji.getPoster() ).into( image );

                //TODO : Ako zelimo da se rating prikazuje pomocu RatingBar-a
//                RatingBar ratingBar = FilmDetails.this.findViewById( R.id.detalji_rating );
//                String rating = detalji.getImdbRating();
//                ratingBar.setRating( Float.parseFloat( rating ) );

                TextView tvRating = FilmDetails.this.findViewById( R.id.detalji_rating );
                tvRating.setText( "IMDB Rating: " + detalji.getImdbRating() + "/10" );


                TextView title = FilmDetails.this.findViewById( R.id.detalji_title );
                title.setText( detalji.getTitle() );

                TextView year = FilmDetails.this.findViewById( R.id.detalji_year );
                year.setText( "(" + detalji.getYear() + ")" );

                TextView runtime = FilmDetails.this.findViewById( R.id.detalji_runtime );
                runtime.setText( detalji.getRuntime() );

                TextView genre = FilmDetails.this.findViewById( R.id.detalji_genre );
                genre.setText( detalji.getGenre() );

                TextView writer = FilmDetails.this.findViewById( R.id.detalji_writer );
                writer.setText( detalji.getWriter() );

                TextView director = FilmDetails.this.findViewById( R.id.detalji_director );
                director.setText( detalji.getDirector() );

                TextView actors = FilmDetails.this.findViewById( R.id.detalji_actors );
                actors.setText( detalji.getActors() );

                TextView plot = FilmDetails.this.findViewById( R.id.detalji_plot );
                plot.setText( detalji.getPlot() );

            }

            @Override
            public void onFailure(Call<Detail> call, Throwable t) {
                Toast.makeText( FilmDetails.this, t.getMessage(), Toast.LENGTH_SHORT ).show();
            }
        } );
    }

    private void deleteFilm() {
        int filmZaBrisanje = getIntent().getExtras().getInt( "id", 0 );
        try {
            getDatabaseHelper().getmFavoriteFilmoviDao().deleteById( filmZaBrisanje );
            finish();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String tekstNotifikacija = "Film je obrisan";

        boolean toast = prefs.getBoolean( getString( R.string.toast_key ), false );
        boolean notif = prefs.getBoolean( getString( R.string.notif_key ), false );

        if (toast) {
            Toast.makeText( FilmDetails.this, tekstNotifikacija, Toast.LENGTH_LONG ).show();
        }

        if (notif) {
            NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
            NotificationCompat.Builder builder = new NotificationCompat.Builder( FilmDetails.this, NOTIF_CHANNEL_ID );

            builder.setSmallIcon( android.R.drawable.ic_menu_delete );
            builder.setContentTitle( "Notifikacija" );
            builder.setContentText( tekstNotifikacija );

            Bitmap bitmap = BitmapFactory.decodeResource( getResources(), R.drawable.heart );

            builder.setLargeIcon( bitmap );
            notificationManager.notify( 1, builder.build() );
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_details_film, menu );
        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                deleteFilm();
                setTitle( "Brisanje" );
                break;
            case android.R.id.home:
                startActivity( new Intent( this, ListaFilmova.class ) );
                break;
        }

        return super.onOptionsItemSelected( item );
    }

    public void setupToolbar() {
        toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        toolbar.setTitleTextColor( Color.WHITE );
        toolbar.setSubtitle( "Detail filma" );
//        toolbar.setLogo( R.mipmap.ic_launcher_foreground );


        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled( true );
            actionBar.setHomeAsUpIndicator( R.drawable.back );
            actionBar.setHomeButtonEnabled( true );
            actionBar.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        String imdbKey = getIntent().getStringExtra( Tools.KEY );
        getDetail( imdbKey );
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
}
