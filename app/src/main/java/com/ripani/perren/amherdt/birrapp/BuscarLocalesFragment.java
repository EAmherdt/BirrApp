package com.ripani.perren.amherdt.birrapp;


import android.content.Intent;
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
import android.widget.Toast;

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
    private com.ripani.perren.amherdt.birrapp.modelo.Spinner spEstilo;
    private com.ripani.perren.amherdt.birrapp.modelo.Spinner spMarca;
    private LocalDao localDao;
    private List<Local> listaLocales = new ArrayList<Local>();
    private List<Estilo> arrayEstilos = new ArrayList<Estilo>();
    private int countSpinnerListenerEstilo=0;
    private int countSpinnerListenerMarca=0;
    public BuscarLocalesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_buscar_locales, container, false);

        localDao = MyDataBase.getInstance(this.getActivity()).getLocalDao();

        spMarca= (com.ripani.perren.amherdt.birrapp.modelo.Spinner) v.findViewById(R.id.spMarca);
        spEstilo= (com.ripani.perren.amherdt.birrapp.modelo.Spinner) v.findViewById(R.id.spEstilo);
        btnBuscar= (Button) v.findViewById(R.id.btnBuscarLocal);
        nombreLocal = (EditText) v.findViewById(R.id.etNombreLocalBuscar);
        lvLocales = (ListView) v.findViewById(R.id.listaLocales);
        adapterLocales = new ArrayAdapter(getActivity(), android.R.layout.simple_selectable_list_item);
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
                        final Estilo e = new Estilo();
                        e.setNombre("Seleccione");
                        arrayEstilos.add(e);
                        arrayEstilos.addAll(CervezaRepositorio.LISTA_ESTILOS);
                        adapterEstilos = new ArrayAdapter<Estilo>(getActivity(), android.R.layout.simple_spinner_dropdown_item, arrayEstilos);
                        spEstilo.setAdapter(adapterEstilos);
                        spEstilo.setSelection(0);
                        boolean flag=false;
                        final Cerveza c = new Cerveza();
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

                        spMarca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int
                                    position, long id) {

                                Cerveza cerveza = new Cerveza();
                                cerveza = (Cerveza) adapterCervezas.getItem(position);
                                //si selecciono una marca y estilo es "seleccionar"
                                if(!cerveza.equals(c) && spEstilo.getSelectedItem().equals(e)) {
                                    adapterEstilos.clear();
                                    adapterEstilos.add(e);
                                    for (int j = 0; j < CervezaRepositorio.LISTA_CERVEZA.size(); j++) {
                                        if (cerveza.getMarca().equals(CervezaRepositorio.LISTA_CERVEZA.get(j).getMarca())) {
                                            adapterEstilos.add(CervezaRepositorio.LISTA_CERVEZA.get(j).getEstilo());
                                        }
                                    }
                                }
                                //si selecciono "seleccionar" y estilo es "seleccionar"
                                if(cerveza.equals(c) && spEstilo.getSelectedItem().equals(e)){
                                    adapterEstilos.clear();
                                    adapterEstilos.add(e);
                                    arrayEstilos.addAll(CervezaRepositorio.LISTA_ESTILOS);
                                    boolean flag=false;
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
                                }
                                //si selecciono "seleccionar" y tengo un estilo seleccionado
                                if(cerveza.equals(c) && !spEstilo.getSelectedItem().equals(e)){
                                    Estilo estilo = (Estilo) spEstilo.getSelectedItem();
                                    adapterEstilos.clear();
                                    adapterEstilos.add(e);
                                    arrayEstilos.addAll(CervezaRepositorio.LISTA_ESTILOS);
                                    //hago setAdapter porque sino no me toma el setSelection
                                    spEstilo.setAdapter(adapterEstilos);
                                    spEstilo.setSelection(adapterEstilos.getPosition(estilo));
                                }
                                //si selecciono una marca y tengo un estilo seleccionado
                                if(!cerveza.equals(c) && !spEstilo.getSelectedItem().equals(e)){
                                        countSpinnerListenerEstilo=1;
                                        Estilo estilo = (Estilo) spEstilo.getSelectedItem();
                                        adapterEstilos.clear();
                                        adapterEstilos.add(e);
                                        for (int j = 0; j < CervezaRepositorio.LISTA_CERVEZA.size(); j++) {
                                            if (cerveza.getMarca().equals(CervezaRepositorio.LISTA_CERVEZA.get(j).getMarca())) {
                                                adapterEstilos.add(CervezaRepositorio.LISTA_CERVEZA.get(j).getEstilo());
                                            }
                                        }
                                        spEstilo.setAdapter(adapterEstilos);
                                        spEstilo.setSelection(adapterEstilos.getPosition(estilo));


                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        spEstilo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int
                                    position, long id) {
                                Estilo estilo = new Estilo();
                                estilo = (Estilo) adapterEstilos.getItem(position);
                                //si selecciono un estilo y marca es "seleccionar"
                                if(!estilo.equals(e) && spMarca.getSelectedItem().equals(c)) {
                                    adapterCervezas.clear();
                                    adapterCervezas.add(c);
                                    for (int j = 0; j < CervezaRepositorio.LISTA_CERVEZA.size(); j++) {
                                        if (estilo.equals(CervezaRepositorio.LISTA_CERVEZA.get(j).getEstilo())) {
                                            adapterCervezas.add(CervezaRepositorio.LISTA_CERVEZA.get(j));
                                        }
                                    }
                                }
                                //si selecciono "seleccionar" y marca es "seleccionar"
                                if(estilo.equals(e) && spMarca.getSelectedItem().equals(c)){
                                    adapterCervezas.clear();
                                    adapterCervezas.add(c);
                                    boolean flag=false;
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
                                }
                                //si seleeciono "seleccionar" y tengo una marca seleccionada
                                if(estilo.equals(e) && !spMarca.getSelectedItem().equals(c)){
                                    boolean flag=false;
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
                                }
                                //si selecciono un estilo y tengo una marca seleccionada
                                if(!estilo.equals(e) && !spMarca.getSelectedItem().equals(c)) {
                                    if (countSpinnerListenerEstilo == 0) {
                                        countSpinnerListenerMarca=1;
                                        Cerveza cerveza = (Cerveza) spMarca.getSelectedItem();
                                        //!!!
                                        adapterCervezas.clear();
                                        adapterCervezas.add(c);

                                        for (int j = 0; j < CervezaRepositorio.LISTA_CERVEZA.size(); j++) {
                                            if (estilo.equals(CervezaRepositorio.LISTA_CERVEZA.get(j).getEstilo())) {
                                                adapterCervezas.add(CervezaRepositorio.LISTA_CERVEZA.get(j));
                                            }
                                        }
                                        spMarca.setAdapter(adapterCervezas);
                                        for(int q=0;q < CervezaRepositorio.LISTA_CERVEZA.size(); q++){
                                            if(cerveza.getMarca().equals(CervezaRepositorio.LISTA_CERVEZA.get(q).getMarca()))
                                            cerveza=CervezaRepositorio.LISTA_CERVEZA.get(q);
                                            if(adapterCervezas.getPosition(cerveza)!=-1) {
                                                spMarca.setSelection(adapterCervezas.getPosition(cerveza));
                                            }
                                        }




                                    }
                                    else{
                                        countSpinnerListenerEstilo=0;
                                    }
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
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
                int countCerveza=0;
                Cerveza cerveza = new Cerveza();
                cerveza.setMarca(((Cerveza) spMarca.getSelectedItem()).getMarca());
                cerveza.setEstilo(((Estilo) spEstilo.getSelectedItem()));


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
                    for(int i=0;i < adapterLocales.getCount(); i++) {
                        List<Cerveza> listaCervezas = adapterLocales.getItem(i).getCervezas();
                        for (int j = 0; j < listaCervezas.size(); j++) {
                            if (cerveza.getMarca() != "Seleccione" && cerveza.getEstilo().getNombre() != "Seleccione" && listaCervezas.get(j).getMarca().equals(cerveza.getMarca()) && listaCervezas.get(j).getEstilo().equals(cerveza.getEstilo())){
                                countCerveza=1;
                            }
                            if (cerveza.getMarca() != "Seleccione" && cerveza.getEstilo().getNombre() == "Seleccione" && listaCervezas.get(j).getMarca().equals(cerveza.getMarca())){
                                countCerveza=1;
                            }
                            if (cerveza.getMarca() == "Seleccione" && cerveza.getEstilo().getNombre() != "Seleccione" && listaCervezas.get(j).getEstilo().equals(cerveza.getEstilo())){
                                countCerveza=1;
                            }
                            if (cerveza.getMarca() == "Seleccione" && cerveza.getEstilo().getNombre() == "Seleccione"){
                                countCerveza=1;
                            }
                        }
                        if (countCerveza != 1) {
                            adapterLocales.remove(adapterLocales.getItem(i));
                            i=i-1;
                        }
                        countCerveza=0;
                    }

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


        lvLocales.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               Local local = (Local) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getActivity(),PerfilLocal.class);
                intent.putExtra("idlocal", local.getId());
                startActivity(intent);

            }
        });


        return v;
    }




}
