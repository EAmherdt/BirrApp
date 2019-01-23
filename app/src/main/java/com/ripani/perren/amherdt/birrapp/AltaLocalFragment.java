package com.ripani.perren.amherdt.birrapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 */
public class AltaLocalFragment extends Fragment {

    private Button btnAñadirCervezas;

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
                startActivity(intent);
            }
        });
        return v;
    }



}
