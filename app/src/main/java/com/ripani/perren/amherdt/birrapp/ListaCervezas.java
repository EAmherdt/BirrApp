package com.ripani.perren.amherdt.birrapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.Activity;
import android.widget.Toast;

import com.ripani.perren.amherdt.birrapp.dao.CervezaRepositorio;
import com.ripani.perren.amherdt.birrapp.modelo.Cerveza;
import com.ripani.perren.amherdt.birrapp.modelo.Estilo;


public class ListaCervezas extends AppCompatActivity {

    private Spinner spinner;
    private ArrayAdapter<Estilo> adapterCategoria;
    private CervezaRepositorio product = new CervezaRepositorio();
    private ArrayAdapter<Cerveza> adapterProductos;
    private TextView tvProducto;
    private ListView listaProductos;
    private EditText edtCantidad;
    private TextView tvCantidad;
    private Button aceptar;
    private Cerveza producto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_lista_cervezas);
        spinner = findViewById(R.id.spinnerCategoria);
        edtCantidad = findViewById(R.id.edtCantidad);
        aceptar = findViewById(R.id.btnAceptar);
        adapterCategoria = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, product.getCategorias());
        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterCategoria);
        tvCantidad = findViewById(R.id.tvCantidad);
        listaProductos = findViewById(R.id.listaProductos);
        tvProducto = findViewById(R.id.productos);
        adapterProductos = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, product.getLista());
        listaProductos.setAdapter(adapterProductos);
        this.aceptar.setOnClickListener(listenerBtnAceptar);

        /*if (this.getIntent().getStringExtra("requestCode").equals("2")) {
            aceptar.setVisibility(View.INVISIBLE);
            tvCantidad.setVisibility(View.INVISIBLE);
            edtCantidad.setVisibility(View.INVISIBLE);
            
        }*/


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Estilo cat = (Estilo) adapterView.getItemAtPosition(i);
                adapterProductos = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_single_choice, product.buscarPorCategoria(cat));
                listaProductos.setAdapter(adapterProductos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        listaProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                producto = (Cerveza) adapterView.getItemAtPosition(i);
            }
        });
    }

    private View.OnClickListener listenerBtnAceptar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intentResultado = new Intent();
            if (edtCantidad.getText().toString().equals("0")) {
                Toast.makeText(ListaCervezas.this,
                        "La cantidad ingresada (" + edtCantidad.getText().toString() + ") es incorrecta",
                        Toast.LENGTH_LONG).show();
                return;
            }
            intentResultado.putExtra("cantidad", edtCantidad.getText().toString());
            Log.d("TEST", "Eleccion: " + producto);
            intentResultado.putExtra("producto", (String) producto.getId().toString());
            setResult(Activity.RESULT_OK, intentResultado);
            finish();
        }
    };


}