package com.ripani.perren.amherdt.birrapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.ripani.perren.amherdt.birrapp.modelo.Local;
import com.ripani.perren.amherdt.birrapp.modelo.LocalDao;

import java.util.ArrayList;
import java.util.List;

/*import ar.edu.utn.frsf.isi.dam.laboratorio05.modelo.MyDatabase;
import ar.edu.utn.frsf.isi.dam.laboratorio05.modelo.Reclamo;
import ar.edu.utn.frsf.isi.dam.laboratorio05.modelo.ReclamoDao;*/

/**
 * A simple {@link Fragment} subclass.
 */
public class MapaFragment extends SupportMapFragment implements OnMapReadyCallback {

    private GoogleMap miMapa;
    private int tipoMapa;
    private int idLocalSeleccionado;
    private OnMapaListener listener;

    private LocalDao localDao;
    private TileOverlay mOverlay;

    private List<Local> locales;

    public MapaFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup
            container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container,
                savedInstanceState);
        tipoMapa=1;
        //Bundle argumentos = getArguments();
       /* if(argumentos !=null) {
            tipoMapa = argumentos.getInt("tipo_mapa",0);
            idLocalSeleccionado = argumentos.getInt("idLocalSeleccionado",0);
        }*/
        getMapAsync(this);
        return rootView;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        miMapa = googleMap;
        actualizarMapa();
        switch (tipoMapa){
            case 1:
                miMapa.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                        listener.coordenadasSeleccionadas(latLng);
                    }
                });
                break;
            case 2:
                cargarMapaConTodosLocales();
                break;
            case 3:
                cargarMapaLocalSeleccionado(idLocalSeleccionado);
                break;
            default:
                break;
        }
    }

    private void cargarMapaLocalSeleccionado(final int idLocal){

        //localDao = MyDatabase.getInstance(getContext()).getReclamoDao();

        Runnable r = new Runnable() {
                @Override
                public void run() {
                final Local local = localDao.getById(idLocal);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        Marker marker = miMapa.addMarker(new MarkerOptions()
                                .position(new LatLng(local.getLatitud(),local.getLongitud()))
                                .title(local.getId() + "[" + local.getNombre().toString() + "]"));
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(new LatLng(local.getLatitud(),local.getLongitud()))
                                .zoom(15)
                                .build();
                        CircleOptions circleOptions = new CircleOptions()
                                .center(new LatLng(local.getLatitud(),local.getLongitud()))
                                .radius(500)
                                .strokeColor(Color.RED)
                                .fillColor(0x5500ff00)
                                .strokeWidth(5);
                        Circle circle = miMapa.addCircle(circleOptions);
                        miMapa.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }
                });
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    private void cargarMapaConTodosLocales(){

        //localDao = MyDatabase.getInstance(getContext()).getReclamoDao();

        Runnable r = new Runnable() {
            @Override
            public void run() {
                locales = localDao.getAll();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        for(Local l : locales){
                            Marker marker = miMapa.addMarker(new MarkerOptions()
                                    .position(new LatLng(l.getLatitud(),l.getLongitud()))
                                    .title(l.getId() + "[" + l.getNombre().toString() + "]"));
                            builder.include(marker.getPosition());
                        }
                        LatLngBounds limite = builder.build();
                        miMapa.moveCamera(CameraUpdateFactory.newLatLngBounds(limite, 0));
                    }
                });
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    private void actualizarMapa() {
        if (ActivityCompat.checkSelfPermission(this.getContext(),Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    9999);
            return;
        }
        miMapa.setMyLocationEnabled(true);

    }

    public void setListener(OnMapaListener listener) {
        this.listener = listener;
    }

    public interface OnMapaListener {
        public void coordenadasSeleccionadas(LatLng c);
    }
}
