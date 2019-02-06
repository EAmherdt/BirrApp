package com.ripani.perren.amherdt.birrapp;


import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ripani.perren.amherdt.birrapp.dao.CervezaRepositorio;
import com.ripani.perren.amherdt.birrapp.modelo.Cerveza;
import com.ripani.perren.amherdt.birrapp.modelo.Local;
import com.ripani.perren.amherdt.birrapp.modelo.LocalDao;
import com.ripani.perren.amherdt.birrapp.modelo.MyDataBase;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AltaLocalFragment extends Fragment {

    public interface OnNuevoLugarListener {
        public void obtenerCoordenadas();
}

    public interface OnImagenListener {
        public void obtenerImagen();
    }


    public void setListenerLugar(OnNuevoLugarListener listenerLugar) {
        this.listenerLugar = listenerLugar;
    }



    private OnNuevoLugarListener listenerLugar;

    private Button btnAñadirCervezas;
    private Button btnBuscarUbicacion;
    private Button btnImagen;
    private Button btnCancelar;
    private Button btnCrear;
    private ArrayList<String> arrayCervezas = new ArrayList<>();
    private EditText etNombreLocal;
    private EditText etHoraApertura;
    private EditText etHoraCierre;
    private TextView tvCoord;
    private RadioButton rbAdmite;
    private RadioButton rbNoAdmite;
    private Local local = new Local();
    private LocalDao localDao;
    private CervezaRepositorio repositorio = new CervezaRepositorio();
    private long idLocal;



    public AltaLocalFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_alta_local, container, false);

        localDao = MyDataBase.getInstance(this.getActivity()).getLocalDao();

        Runnable hiloActualizacion = new Runnable() {
            @Override
            public void run() {
                List<Local> l = new ArrayList<Local>();
                l = localDao.getAll();
                System.out.println(l);

            }
        };
        Thread t1 = new Thread(hiloActualizacion);
        t1.start();

        btnAñadirCervezas= (Button) v.findViewById(R.id.btnAñadirCervezas);
        btnCancelar= (Button) v.findViewById(R.id.btnCancelar);
        btnCrear= (Button) v.findViewById(R.id.btnCrear);
        btnImagen= (Button) v.findViewById(R.id.btnImagen);
        btnBuscarUbicacion= (Button) v.findViewById(R.id.btnBuscarUbicacion);
        etNombreLocal = (EditText) v.findViewById(R.id.etNombreLocal);
        etHoraApertura = (EditText) v.findViewById(R.id.etHoraApertura);
        etHoraCierre = (EditText) v.findViewById(R.id.etHoraCierre);
        rbAdmite = (RadioButton) v.findViewById(R.id.rbAdmite);
        rbNoAdmite = (RadioButton) v.findViewById(R.id.rbNoAdmite);
        tvCoord= (TextView) v.findViewById(R.id.tvCoord);
        Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        String coordenadas = "0;0";
        if(getArguments()!=null) coordenadas = getArguments().getString("latLng","0;0");
        tvCoord.setText(coordenadas);

        btnBuscarUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerLugar.obtenerCoordenadas();

            }
        });

        btnImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), 1);

            }
        });

        btnAñadirCervezas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ListaCervezas.class);
                intent.putStringArrayListExtra("cerveza",arrayCervezas);
                intent.putStringArrayListExtra("cervezaCancel",arrayCervezas);
                startActivityForResult(intent,1);
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().
                        remove(getFragmentManager().findFragmentById(R.id.contenedorFragmento)).commit();
            }
        });

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                local.setNombre(etNombreLocal.getText().toString());
                local.setHoraApertura(etHoraApertura.getText().toString());
                local.setHoraCierre(etHoraCierre.getText().toString());
                local.setReservas(rbAdmite.isChecked());//si reservas es 1 admite
                List<Cerveza> listaCervezas = new ArrayList<Cerveza>();
                for (String id : arrayCervezas) {
                    listaCervezas.add(repositorio.buscarCervezaPorId(Integer.parseInt(id)));
                }
                local.setCervezas(listaCervezas);
                String coor= tvCoord.getText().toString();
                if(coor.length()>0 && coor.contains(";")) {
                    String[] coord = coor.split(";");
                    local.setLatitud(Double.valueOf(coord[0]));
                    local.setLongitud(Double.valueOf(coord[1]));
                }


                Runnable hiloActualizacion = new Runnable() {
                    @Override
                    public void run() {

                        if(local.getId()>0) localDao.update(local);
                        else {
                            idLocal = localDao.insert(local);
                            System.out.println("IDLOCAL"+idLocal);
                        }


                    }
                };

                Runnable hiloNotificacion = new Runnable() {
                    @Override
                    public void run() {


                        System.out.println("IDLOCAL2" + idLocal);
                        Intent intentAceptado = new Intent(getActivity(), NotificationReceiver.class);
                        intentAceptado.putExtra("nombreLocal", local.getNombre());
                        intentAceptado.putExtra("idLocal", idLocal);

                        getActivity().sendBroadcast(intentAceptado);
                    }
                };


                Thread t1 = new Thread(hiloActualizacion);
                t1.start();

                try {
                    t1.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                //El hilo de notificaciones se ejecuta despues de actualizar los dato, ya que
                // habia problemas con pasar el id ya que el objeto no se habia creado aun.

                    Thread t2 = new Thread(hiloNotificacion);
                t2.start();





                getFragmentManager().beginTransaction().
                        remove(getFragmentManager().findFragmentById(R.id.contenedorFragmento)).commit();

            }
        });







        return v;
    }






}
