package com.example.glumacfilmovi.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.glumacfilmovi.R;
import com.example.glumacfilmovi.db.DatabaseHelper;
import com.example.glumacfilmovi.db.model.Glumac;
import com.example.glumacfilmovi.dialog.AboutDialog;
import com.example.glumacfilmovi.settings.SettingsActivity;
import com.example.glumacfilmovi.tools.Tools;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivityGlumac extends AppCompatActivity {

    private Toolbar toolbar;
    private ArrayList<String> drawerItems;
    private ListView drawerList;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private RelativeLayout drawerPane;

    private static final int SELECT_PICTURE = 1;
    private static final String TAG = "PERMISSIONS";

    private String imagePath = null;
    private ImageView preview;

    private SharedPreferences prefs;
    public static final String NOTIF_CHANNEL_ID = "notif_channel_007";

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        setupToolbar();
        fillDrawerData();
        setupDrawer();
    }


    private void addGlumac() {
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

                EditText glumacIme = dialog.findViewById( R.id.glumac_ime );
                EditText glumacPrezime = dialog.findViewById( R.id.glumac_prezime );
                EditText glumacBiografija = dialog.findViewById( R.id.glumac_biografija );
                EditText glumacDatum = dialog.findViewById( R.id.glumac_datum );
                EditText glumacRating = dialog.findViewById( R.id.glumac_rating );

                if (preview == null || imagePath == null) {
                    Toast.makeText( MainActivityGlumac.this, "Slika mora biti izabrana", Toast.LENGTH_SHORT ).show();
                    return;
                }
                if (Tools.validateInput( glumacIme )
                        && Tools.validateInput( glumacPrezime )
                        && Tools.validateInput( glumacBiografija )
                        && Tools.validateInput( glumacDatum )
                        && Tools.validateInput( glumacRating )

                ) {

                    Glumac glumac = new Glumac();
                    glumac.setmIme( glumacIme.getText().toString() );
                    glumac.setmPrezime( glumacPrezime.getText().toString() );
                    glumac.setmBiografija( glumacBiografija.getText().toString() );
                    glumac.setmDatum( glumacDatum.getText().toString() );
                    glumac.setmOcena( Float.parseFloat( glumacRating.getText().toString() ) );
                    glumac.setmSlika( imagePath );

                    try {
                        getDatabaseHelper().getmGlumacDao().create( glumac );

                        String tekstNotifikacija = "Unet je novi glumac";

                        boolean toast = prefs.getBoolean( getString( R.string.toast_key ), false );
                        boolean notif = prefs.getBoolean( getString( R.string.notif_key ), false );

                        if (toast) {
                            Toast.makeText( MainActivityGlumac.this, tekstNotifikacija, Toast.LENGTH_LONG ).show();

                        }

                        if (notif) {
                            NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
                            NotificationCompat.Builder builder = new NotificationCompat.Builder( MainActivityGlumac.this, NOTIF_CHANNEL_ID );
                            builder.setSmallIcon( android.R.drawable.ic_input_add );
                            builder.setContentTitle( "Notifikacija" );
                            builder.setContentText( tekstNotifikacija );

                            Bitmap bitmap = BitmapFactory.decodeResource( getResources(), R.drawable.heart );


                            builder.setLargeIcon( bitmap );
                            notificationManager.notify( 1, builder.build() );
                        }


                    } catch (NumberFormatException e) {
                        Toast.makeText( MainActivityGlumac.this, "Rating mora biti broj", Toast.LENGTH_SHORT ).show();
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
        getMenuInflater().inflate( R.menu.menu, menu );
        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                addGlumac();
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


    private void
    selectPicture() {
        if (isStoragePermissionGranted()) {
            Intent i = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
            startActivityForResult( i, SELECT_PICTURE );
        }
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

}
