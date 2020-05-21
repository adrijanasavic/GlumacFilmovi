package com.example.glumacfilmovi.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.glumacfilmovi.R;
import com.example.glumacfilmovi.adapters.AdapterListaFilmova;
import com.example.glumacfilmovi.db.DatabaseHelper;
import com.example.glumacfilmovi.db.model.FavoriteFIlmovi;
import com.example.glumacfilmovi.db.model.Glumac;
import com.example.glumacfilmovi.net.MyService;
import com.example.glumacfilmovi.net.model1.Search;
import com.example.glumacfilmovi.net.model1.SearchResult;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.glumacfilmovi.net.MyServiceContract.APIKEY;

public class ListaFilmova extends AppCompatActivity implements AdapterListaFilmova.OnItemClickListener {

    private Toolbar toolbar;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private AdapterListaFilmova adapter;

    private Button btnSearch;
    private EditText movieName;
    int position = 0;

    private Glumac glumac;

    private DatabaseHelper databaseHelper;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_lista_filmova );

        setupToolbar();
        fillData();
    }

    private void fillData() {
        int position = getIntent().getExtras().getInt( "position", 0 );

        btnSearch = findViewById( R.id.btn_search );
        movieName = findViewById( R.id.ime_filma );
        recyclerView = findViewById( R.id.rvListaFilmova );

        try {
            glumac = getDatabaseHelper().getmGlumacDao().queryForId( position );
        } catch (SQLException e) {
            e.printStackTrace();
        }


        btnSearch.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMovieByName( movieName.getText().toString() );
            }
        } );
    }

    private void getMovieByName(String name) {
        Map<String, String> query = new HashMap<>();

        query.put( "apikey", APIKEY );
        query.put( "s", name.trim() );

        Call<SearchResult> call = MyService.apiInterface().getMovieByName( query );
        call.enqueue( new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {

                if (response.code() == 200) {
                    try {
                        SearchResult searches = response.body();

                        ArrayList<Search> search = new ArrayList<>();

                        for (Search e : searches.getSearch()) {

                            if (e.getType().equals( "movie" ) || e.getType().equals( "series" )) {
                                search.add( e );
                            }
                        }

                        layoutManager = new LinearLayoutManager( ListaFilmova.this );
                        recyclerView.setLayoutManager( layoutManager );

                        adapter = new AdapterListaFilmova( ListaFilmova.this, search, ListaFilmova.this );
                        recyclerView.setAdapter( adapter );

                        Toast.makeText( ListaFilmova.this, "Prikaz filmova/serija.", Toast.LENGTH_SHORT ).show();

                    } catch (NullPointerException e) {
                        Toast.makeText( ListaFilmova.this, "Ne postoji film/serija sa tim nazivom", Toast.LENGTH_SHORT ).show();
                    }

                } else {

                    Toast.makeText( ListaFilmova.this, "Greska sa serverom", Toast.LENGTH_SHORT ).show();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                Toast.makeText( ListaFilmova.this, t.getMessage(), Toast.LENGTH_SHORT ).show();
            }
        } );
    }


    public void setupToolbar() {
        toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        toolbar.setTitleTextColor( Color.WHITE );
        toolbar.setSubtitle( "Lista filmova" );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
//        toolbar.setLogo( R.mipmap.ic_launcher_foreground );


        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled( true );
            actionBar.setHomeAsUpIndicator( R.drawable.back );
            actionBar.setHomeButtonEnabled( true );
            actionBar.show();
        }
    }

    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper( this, DatabaseHelper.class );
        }
        return databaseHelper;
    }

    @Override
    public void onItemClick(int position) {

        Search movie = adapter.get( position );

        FavoriteFIlmovi favoriteFIlmovi = new FavoriteFIlmovi();
        favoriteFIlmovi.setmNaziv( movie.getTitle() );
        favoriteFIlmovi.setmImdbId( movie.getImdbID() );
        favoriteFIlmovi.setmGodina( movie.getYear() );
        favoriteFIlmovi.setmImage( movie.getPoster() );
        favoriteFIlmovi.setmGlumac( glumac );

        try {
            getDatabaseHelper().getmFavoriteFilmoviDao().create( favoriteFIlmovi );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Toast.makeText( getApplicationContext(), " \"" + movie.getTitle() + "\"" + " je dodat u listu", Toast.LENGTH_LONG ).show();

    }
}

