package com.ripani.perren.amherdt.birrapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.ripani.perren.amherdt.birrapp.dao.CervezaRepositorio;
import com.ripani.perren.amherdt.birrapp.modelo.Cerveza;
import com.ripani.perren.amherdt.birrapp.modelo.Local;
import com.ripani.perren.amherdt.birrapp.modelo.LocalDao;
import com.ripani.perren.amherdt.birrapp.modelo.MyDataBase;

import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;


/**
 * A simple {@link Fragment} subclass.
 */
public class AltaLocalFragment extends Fragment {

    public interface OnNuevoLugarListener {
        public void obtenerCoordenadas();
    }

    public void setListener(OnNuevoLugarListener listener) {
        this.listener = listener;
    }

    private OnNuevoLugarListener listener;
    private Button btnAñadirCervezas;
    private Button btnBuscarUbicacion;
    private Button btnCancelar;
    private Button btnCrear;
    private ArrayList<String> arrayCervezas = new ArrayList<>();
    private EditText etNombreLocal;
    private EditText etHoraApertura;
    private EditText etHoraCierre;
    private RadioButton rbAdmite;
    private RadioButton rbNoAdmite;
    private Local local = new Local();
    private LocalDao localDao;
    private CervezaRepositorio repositorio = new CervezaRepositorio();

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
        btnBuscarUbicacion= (Button) v.findViewById(R.id.btnBuscarUbicacion);
        etNombreLocal = (EditText) v.findViewById(R.id.etNombreLocal);
        etHoraApertura = (EditText) v.findViewById(R.id.etHoraApertura);
        etHoraCierre = (EditText) v.findViewById(R.id.etHoraCierre);
        rbAdmite = (RadioButton) v.findViewById(R.id.rbAdmite);
        rbNoAdmite = (RadioButton) v.findViewById(R.id.rbNoAdmite);


        btnBuscarUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.obtenerCoordenadas();
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
                Runnable hiloActualizacion = new Runnable() {
                    @Override
                    public void run() {

                        if(local.getId()>0) localDao.update(local);
                        else localDao.insert(local);

                    }
                };
                Thread t1 = new Thread(hiloActualizacion);
                t1.start();
                getFragmentManager().beginTransaction().
                        remove(getFragmentManager().findFragmentById(R.id.contenedorFragmento)).commit();

            }
        });


        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Don't forget to check requestCode before continuing your job
            if(requestCode==1){
            arrayCervezas=(ArrayList<String>) data.getStringArrayListExtra("cervezas");
            }
        }




}
