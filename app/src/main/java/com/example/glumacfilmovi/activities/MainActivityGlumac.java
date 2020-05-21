package com.example.glumacfilmovi.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.glumacfilmovi.R;
import com.example.glumacfilmovi.dialog.AboutDialog;
import com.example.glumacfilmovi.settings.SettingsActivity;

import java.util.ArrayList;

public class MainActivityGlumac extends AppCompatActivity {

    private Toolbar toolbar;
    private ArrayList<String> drawerItems;
    private ListView drawerList;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private RelativeLayout drawerPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        setupToolbar();
        fillDrawerData();
        setupDrawer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu, menu );
        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:

                setTitle( "Dodavanje" );
                break;
            case R.id.settings:
                startActivity( new Intent( MainActivityGlumac.this, SettingsActivity.class ) );
                setTitle( "Settings" );
                break;
            case R.id.about_dialog:
                AboutDialog dialog = new AboutDialog( MainActivityGlumac.this );
                dialog.show();
                setTitle( "O aplikaciji" );
                break;
        }

        return super.onOptionsItemSelected( item );
    }

    private void fillDrawerData() {
        drawerItems = new ArrayList<>();
        drawerItems.add( "Lista glumaca" );
        drawerItems.add( "Podesavanja" );
        drawerItems.add( "O aplikaciji" );
    }

    private void setupDrawer() {
        drawerList = findViewById( R.id.left_drawer );
        drawerLayout = findViewById( R.id.drawer_layout );
        drawerPane = findViewById( R.id.drawerPane );

        drawerList.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = "Unknown";
                switch (position) {
                    case 0:
                        title = "Lista glumaca";
                        startActivity( new Intent( MainActivityGlumac.this, MainActivityGlumac.class ) );
                        break;
                    case 1:
                        title = "Podesavanja";
                        startActivity( new Intent( MainActivityGlumac.this, SettingsActivity.class ) );
                        break;
                    case 2:
                        AboutDialog dialog = new AboutDialog( MainActivityGlumac.this );
                        dialog.show();
                        setTitle( "O aplikaciji" );
                        break;
                    default:
                        break;

                }
                drawerList.setItemChecked( position, true );
                setTitle( title );
                drawerLayout.closeDrawer( drawerPane );
            }
        } );
        drawerList.setAdapter( new ArrayAdapter<>( this, android.R.layout.simple_list_item_1, drawerItems ) );
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.app_name,
                R.string.app_name
        ) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };
    }

    public void setupToolbar() {
        toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        toolbar.setTitleTextColor( Color.WHITE );
        toolbar.setSubtitle( "Lista glumaca" );
        toolbar.setLogo( R.drawable.heart );

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled( true );
            actionBar.setHomeAsUpIndicator( R.drawable.drawer );
            actionBar.setHomeButtonEnabled( true );
            actionBar.show();
        }
    }
}
