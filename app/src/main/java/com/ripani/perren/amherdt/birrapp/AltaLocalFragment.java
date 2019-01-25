package com.ripani.perren.amherdt.birrapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AltaLocalFragment extends Fragment {

    private Button btnAñadirCervezas;
    private ArrayList<String> arrayCervezas = new ArrayList<>();

    public AltaLocalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_alta_local, container, false);

        btnAñadirCervezas= (Button) v.findViewById(R.id.btnAñadirCervezas);


        btnAñadirCervezas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("click");
                Intent intent = new Intent(getActivity(), ListaCervezas.class);
                intent.putStringArrayListExtra("cerveza",arrayCervezas);
                intent.putStringArrayListExtra("cervezaCancel",arrayCervezas);
                startActivityForResult(intent,1);
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
