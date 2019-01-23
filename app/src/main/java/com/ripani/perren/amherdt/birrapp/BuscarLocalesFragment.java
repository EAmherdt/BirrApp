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

import com.ripani.perren.amherdt.birrapp.modelo.Local;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BuscarLocalesFragment extends Fragment {

    private Spinner marcas;
    private Spinner estilos;
    private EditText nombreLocal;
    private Button btnBuscar;


    private ArrayAdapter<String> marcaAdapter;
    private ArrayAdapter<Local.Estilo> estiloAdapter;

    public BuscarLocalesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_buscar_locales, container, false);

        marcas= (Spinner) v.findViewById(R.id.spMarca);
        estilos= (Spinner) v.findViewById(R.id.spEstilo);
        final Local.Marca arrayMarca[]=Local.Marca.values();
        List<String> arrayMarcaSt=new ArrayList<>();
        for(int i=0;i<arrayMarca.length;i++){
            arrayMarcaSt.add(arrayMarca[i].getNombreMarca());
        }

        marcaAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item,arrayMarcaSt);
        marcaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        marcas.setAdapter(marcaAdapter);

        marcas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*List<Local.Estilo> arrayEstilos=arrayMarca[marcas.getSelectedItemPosition()].getEstilos();
                List<String> arrayEstilosSt=new ArrayList<>();
                for(int i=0;i<arrayEstilos.size();i++){
                    arrayEstilosSt.add(arrayEstilos[i]);
                }*/
                estiloAdapter = new ArrayAdapter<Local.Estilo>(getActivity(),android.R.layout.simple_spinner_item,(arrayMarca[marcas.getSelectedItemPosition()]).getEstilos());
                estiloAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                estilos.setAdapter(estiloAdapter);
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
