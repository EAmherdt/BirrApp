package com.ripani.perren.amherdt.birrapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.Activity;

import com.ripani.perren.amherdt.birrapp.dao.CervezaRepositorio;
import com.ripani.perren.amherdt.birrapp.modelo.Cerveza;
import com.ripani.perren.amherdt.birrapp.modelo.CervezaRest;
import com.ripani.perren.amherdt.birrapp.modelo.Estilo;
import com.ripani.perren.amherdt.birrapp.modelo.EstiloRest;

import java.util.ArrayList;


public class ListaCervezas extends AppCompatActivity {

    private Spinner spinner;
    private ArrayAdapter<Estilo> adapterEstilos;
    private CervezaRepositorio repositorio = new CervezaRepositorio();
    private ArrayAdapter<Cerveza> adapterCervezas;
    private TextView tvProducto;
    private ListView listaCervezas;
    private Button aceptar;
    private Cerveza cerveza;
    private ArrayList<String> arrayCervezas = new ArrayList<>();
    static Estilo[] estilos;
    static Cerveza[] cervezas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lista_cervezas);
        spinner = findViewById(R.id.spinnerCategoria);
        aceptar = findViewById(R.id.btnAceptar);
        //adapterEstilos = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, repositorio.getEstilos());
        adapterCervezas = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice);
        //adapterEstilos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterEstilos);
        listaCervezas = findViewById(R.id.listaProductos);
        tvProducto = findViewById(R.id.productos);
        listaCervezas.setAdapter(adapterCervezas);
        this.aceptar.setOnClickListener(listenerBtnAceptar);

        /*if (this.getIntent().getStringExtra("requestCode").equals("2")) {
            aceptar.setVisibility(View.INVISIBLE);
            tvCantidad.setVisibility(View.INVISIBLE);
            edtCantidad.setVisibility(View.INVISIBLE);
            
        }*/

        listaCervezas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cerveza = (Cerveza) adapterView.getItemAtPosition(i);
                if(listaCervezas.isItemChecked(i)){
                    arrayCervezas.add(cerveza.getId().toString());
                }else{
                    arrayCervezas.remove(cerveza.getId().toString());
                }
            }
        });


        Runnable r = new Runnable() {
            @Override
            public void run() {
                EstiloRest catRest = new EstiloRest();
                CervezaRepositorio.LISTA_ESTILOS = catRest.listarTodas();


                CervezaRest cervezaRest = new CervezaRest();
                CervezaRepositorio.LISTA_CERVEZA = cervezaRest.listarTodas();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        adapterEstilos = new ArrayAdapter<Estilo>(ListaCervezas.this, android.R.layout.simple_spinner_dropdown_item, CervezaRepositorio.LISTA_ESTILOS);

                        repositorio.getEstilos();

                        spinner.setAdapter(adapterEstilos);
                        spinner.setSelection(0);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int
                                    position, long id) {
                                adapterCervezas.clear();

                                adapterCervezas.addAll(repositorio.buscarPorEstilo((Estilo) parent.getItemAtPosition(position))
                                );

                                adapterCervezas.notifyDataSetChanged();

                                listaCervezas.setAdapter(adapterCervezas);

                                for(int i=0;i<adapterCervezas.getCount();i++){
                                    if(arrayCervezas.contains(adapterCervezas.getItem(i).getId().toString())){
                                        listaCervezas.setItemChecked(i,true);
                                    }
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        })
                        ;

                        listaCervezas.setAdapter(adapterCervezas);
                    }
                });
            }
        };
        Thread hiloCargarCombo = new Thread(r);
        hiloCargarCombo.start();
    }

    private View.OnClickListener listenerBtnAceptar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intentResultado = new Intent();

                /*Toast.makeText(ListaCervezas.this,
                        "Cervezas agregadas con éxito",
                        Toast.LENGTH_LONG).show();*/

            //intentResultado.putExtra("cerveza", (String) cerveza.getId().toString());
            intentResultado.putStringArrayListExtra("cervezas",arrayCervezas);
            Log.d("TEST", "Eleccion: " + cerveza);
            setResult(Activity.RESULT_OK, intentResultado);
            finish();
        }
    };
}