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
import android.widget.ListView;
import android.widget.Spinner;

import com.ripani.perren.amherdt.birrapp.dao.CervezaRepositorio;
import com.ripani.perren.amherdt.birrapp.modelo.Cerveza;
import com.ripani.perren.amherdt.birrapp.modelo.CervezaRest;
import com.ripani.perren.amherdt.birrapp.modelo.Estilo;
import com.ripani.perren.amherdt.birrapp.modelo.EstiloRest;
import com.ripani.perren.amherdt.birrapp.modelo.Local;
import com.ripani.perren.amherdt.birrapp.modelo.LocalDao;
import com.ripani.perren.amherdt.birrapp.modelo.MyDataBase;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BuscarLocalesFragment extends Fragment {


    private EditText nombreLocal;
    private Button btnBuscar;
    private ArrayAdapter<Cerveza> adapterCervezas;
    private ArrayAdapter<Estilo> adapterEstilos;
    private ArrayAdapter<Local> adapterLocales;
    private ListView lvLocales;
    private CervezaRepositorio repositorio = new CervezaRepositorio();
    private ArrayList<Cerveza> arrayCervezas = new ArrayList<>();
    private Spinner spEstilo;
    private Spinner spMarca;
    private LocalDao localDao;
    private List<Local> listaLocales = new ArrayList<Local>();
    private List<Estilo> arrayEstilos = new ArrayList<Estilo>();

    public BuscarLocalesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_buscar_locales, container, false);

        localDao = MyDataBase.getInstance(this.getActivity()).getLocalDao();

        spMarca= (Spinner) v.findViewById(R.id.spMarca);
        spEstilo= (Spinner) v.findViewById(R.id.spEstilo);
        btnBuscar= (Button) v.findViewById(R.id.btnBuscarLocal);
        nombreLocal = (EditText) v.findViewById(R.id.etNombreLocalBuscar);
        lvLocales = (ListView) v.findViewById(R.id.listaLocales);
        adapterLocales = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_multiple_choice);
        final Local.Marca arrayMarca[]=Local.Marca.values();
        List<String> arrayMarcaSt=new ArrayList<>();
        for(int i=0;i<arrayMarca.length;i++){
            arrayMarcaSt.add(arrayMarca[i].getNombreMarca());
        }

        spMarca.setAdapter(adapterCervezas);
        spEstilo.setAdapter(adapterEstilos);

        Runnable hiloActualizacion = new Runnable() {
            @Override
            public void run() {
                listaLocales = localDao.getLocalesPorNombre("");

            }
        };
        Thread t1 = new Thread(hiloActualizacion);
        t1.start();
        while(t1.isAlive());
        adapterLocales.addAll(listaLocales);

        adapterLocales.notifyDataSetChanged();
        lvLocales.setAdapter(adapterLocales);

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
                        Estilo e = new Estilo();
                        e.setNombre("Seleccione");
                        arrayEstilos.add(e);
                        arrayEstilos.addAll(CervezaRepositorio.LISTA_ESTILOS);
                        adapterEstilos = new ArrayAdapter<Estilo>(getActivity(), android.R.layout.simple_spinner_dropdown_item, arrayEstilos);
                        spEstilo.setAdapter(adapterEstilos);
                        spEstilo.setSelection(0);
                        boolean flag=false;
                        Cerveza c = new Cerveza();
                        c.setMarca("Seleccione");
                        arrayCervezas.add(c);

                        for(int i=0;i<CervezaRepositorio.LISTA_CERVEZA.size();i++){
                            for(int j=0;j<arrayCervezas.size();j++){
                            if(arrayCervezas.get(j).getMarca().equals(CervezaRepositorio.LISTA_CERVEZA.get(i).getMarca())){
                                flag=true;
                            }
                            }if(flag==false){
                                arrayCervezas.add(CervezaRepositorio.LISTA_CERVEZA.get(i));
                            }
                            flag=false;
                        }

                        adapterCervezas = new ArrayAdapter<Cerveza>(getActivity(), android.R.layout.simple_spinner_dropdown_item, arrayCervezas);
                        spMarca.setAdapter(adapterCervezas);
                        spMarca.setSelection(0);
                    }
                });
            }
        };
        Thread hiloCargarCombo = new Thread(r);
        hiloCargarCombo.start();

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String cadena= nombreLocal.getText().toString();
                adapterLocales.clear();


                    Runnable hiloActualizacion = new Runnable() {
                        @Override
                        public void run() {
                            listaLocales = localDao.getLocalesPorNombre(cadena);

                        }
                    };
                    Thread t1 = new Thread(hiloActualizacion);
                    t1.start();
                    while(t1.isAlive());
                    adapterLocales.addAll(listaLocales);

                    adapterLocales.notifyDataSetChanged();
                    lvLocales.setAdapter(adapterLocales);

            }
        });


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
