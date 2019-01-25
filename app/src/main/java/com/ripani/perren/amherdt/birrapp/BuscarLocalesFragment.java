package com.ripani.perren.amherdt.birrapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.ripani.perren.amherdt.birrapp.dao.CervezaRepositorio;
import com.ripani.perren.amherdt.birrapp.modelo.Cerveza;
import com.ripani.perren.amherdt.birrapp.modelo.CervezaRest;
import com.ripani.perren.amherdt.birrapp.modelo.Estilo;
import com.ripani.perren.amherdt.birrapp.modelo.EstiloRest;
import com.ripani.perren.amherdt.birrapp.modelo.Local;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BuscarLocalesFragment extends Fragment {

   ;
    private EditText nombreLocal;
    private Button btnBuscar;
    private ArrayAdapter<Cerveza> adapterCervezas;
    private ArrayAdapter<Estilo> adapterEstilos;
    private CervezaRepositorio repositorio = new CervezaRepositorio();
    private Spinner spEstilo;
    private Spinner spMarca;

    public BuscarLocalesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_buscar_locales, container, false);

        spMarca= (Spinner) v.findViewById(R.id.spMarca);
        spEstilo= (Spinner) v.findViewById(R.id.spEstilo);
        final Local.Marca arrayMarca[]=Local.Marca.values();
        List<String> arrayMarcaSt=new ArrayList<>();
        for(int i=0;i<arrayMarca.length;i++){
            arrayMarcaSt.add(arrayMarca[i].getNombreMarca());
        }

        spMarca.setAdapter(adapterCervezas);
        spEstilo.setAdapter(adapterEstilos);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                EstiloRest catRest = new EstiloRest();
                CervezaRepositorio.LISTA_ESTILOS = catRest.listarTodas();

                CervezaRest cervezaRest = new CervezaRest();
                CervezaRepositorio.LISTA_CERVEZA = cervezaRest.listarTodas();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        adapterEstilos = new ArrayAdapter<Estilo>(getActivity(), android.R.layout.simple_spinner_dropdown_item, CervezaRepositorio.LISTA_ESTILOS);
                        spEstilo.setAdapter(adapterEstilos);
                        spEstilo.setSelection(0);

                        adapterCervezas = new ArrayAdapter<Cerveza>(getActivity(), android.R.layout.simple_spinner_dropdown_item, CervezaRepositorio.LISTA_CERVEZA);
                        spMarca.setAdapter(adapterCervezas);
                        spMarca.setSelection(0);
                    }
                });
            }
        };
        Thread hiloCargarCombo = new Thread(r);
        hiloCargarCombo.start();


        /*adapterCervezas = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item,arrayMarcaSt);
        adapterCervezas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        marcas.setAdapter(adapterCervezas);*/

                spMarca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*List<Local.Estilo> arrayEstilos=arrayMarca[marcas.getSelectedItemPosition()].getEstilos();
                List<String> arrayEstilosSt=new ArrayList<>();
                for(int i=0;i<arrayEstilos.size();i++){
                    arrayEstilosSt.add(arrayEstilos[i]);
                }*/
                /*adapterEstilos = new ArrayAdapter<Local.Estilo>(getActivity(),android.R.layout.simple_spinner_item,(arrayMarca[spMarca.getSelectedItemPosition()]).getEstilos());
                adapterEstilos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spEstilo.setAdapter(adapterEstilos);*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });
        //return inflater.inflate(R.layout.fragment_buscar_locales, container, false);
        return v;
    }

}
