package com.ripani.perren.amherdt.birrapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener,
        AltaLocalFragment.OnNuevoLugarListener, MapaFragment.OnMapaListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ImageButton btnBuscar = findViewById(R.id.btnMainBuscar);
        ImageButton btnAgregar = findViewById(R.id.btnMainAdd);
        ImageButton btnBig = findViewById(R.id.btnMainBig);
        setSupportActionBar(toolbar);
        createNotificationChannel();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CargarFragmento(new BuscarLocalesFragment());
            }
        });

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new AltaLocalFragment();
                ((AltaLocalFragment) fragment).setListenerLugar(MainActivity.this);
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.contenedorFragmento, fragment, "nuevoReclamoFragment").commit();

            }
        });

        btnBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "No implementado", Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    public void shouldDisplayHomeUp() {
        boolean canback = getSupportFragmentManager().getBackStackEntryCount() > 0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.navAltaLocal) {
            Fragment fragment = new AltaLocalFragment();
            ((AltaLocalFragment) fragment).setListenerLugar(MainActivity.this);
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.contenedorFragmento, fragment, "nuevoReclamoFragment").commit();


        } else if (id == R.id.navBuscarLocales) {
            CargarFragmento(new BuscarLocalesFragment());
        } else if (id == R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);


        } else if (id == R.id.ajustes) {
            Toast.makeText(getBaseContext(), "No implementado", Toast.LENGTH_LONG).show();
        } else if (id == R.id.contacto) {
            Toast.makeText(getBaseContext(), "No implementado", Toast.LENGTH_LONG).show();

        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }

    private void CargarFragmento(Fragment fragmento) {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contenedorFragmento, fragmento).commit();
    }

    @Override
    public void coordenadasSeleccionadas(LatLng c) {
        String tag = "nuevoReclamoFragment";
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            fragment = new AltaLocalFragment();
            ((AltaLocalFragment) fragment).setListenerLugar(MainActivity.this);
        }
        Bundle bundle = new Bundle();
        bundle.putString("latLng", c.latitude + ";" + c.longitude);
        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contenedorFragmento, fragment, tag)
                .commit();
    }

    @Override
    public void obtenerCoordenadas() {
        String tag = "mapaReclamos";
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            fragment = new MapaFragment();
            ((MapaFragment) fragment).setListener(this);
        }
        Bundle bundle = new Bundle();
        bundle.putInt("tipo_mapa", 1);
        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contenedorFragmento, fragment, tag)
                .addToBackStack(null)
                .commit();
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.canal_estado_nombre);
            String description = getString(R.string.canal_estado_descr);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CANAL01", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}

