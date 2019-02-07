package com.ripani.perren.amherdt.birrapp;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ripani.perren.amherdt.birrapp.dao.CervezaRepositorio;
import com.ripani.perren.amherdt.birrapp.modelo.Cerveza;
import com.ripani.perren.amherdt.birrapp.modelo.Local;
import com.ripani.perren.amherdt.birrapp.modelo.LocalDao;
import com.ripani.perren.amherdt.birrapp.modelo.MyDataBase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;


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
    private EditText etCapacidad;
    private TextView tvCoord;
    private RadioButton rbAdmite;
    private RadioButton rbNoAdmite;
    private Local local = new Local();
    private LocalDao localDao;
    private CervezaRepositorio repositorio = new CervezaRepositorio();
    private long idLocal;
    private String pathFoto;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_SAVE = 2;
    Bitmap imageBitmap = null;



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
        etCapacidad = (EditText) v.findViewById(R.id.etCapacidad);
        rbAdmite = (RadioButton) v.findViewById(R.id.rbAdmite);
        rbNoAdmite = (RadioButton) v.findViewById(R.id.rbNoAdmite);
        tvCoord= (TextView) v.findViewById(R.id.tvCoord);


        etCapacidad.setText("10");

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
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(getActivity().getApplicationContext(), "com.ripani.perren.amherdt.birrapp.provider", photoFile);
                        takePictureIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_SAVE);
                    }

                }
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
                local.setCapacidad(Integer.parseInt(etCapacidad.getText().toString()));
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

                        }

                        if (imageBitmap != null) {

                            saveToInternalStorage(imageBitmap,idLocal);

                        }


                    }
                };

                Runnable hiloNotificacion = new Runnable() {
                    @Override
                    public void run() {


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

                //El hilo de notificaciones se ejecuta despues de actualizar los datos, ya que
                // habia problemas con pasar el id ya que el objeto no se habia creado aun.

                    Thread t2 = new Thread(hiloNotificacion);
                t2.start();





                getFragmentManager().beginTransaction().
                        remove(getFragmentManager().findFragmentById(R.id.contenedorFragmento)).commit();

            }
        });









        return v;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==1){
            arrayCervezas=(ArrayList<String>) data.getStringArrayListExtra("cervezas");
        }
//creo que nunca entra
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            System.out.println("test");
            Bitmap imageBitmap = (Bitmap) extras.get("data");
           // local.setImagen(imageBitmap);


        }

        if (requestCode == REQUEST_IMAGE_SAVE && resultCode == RESULT_OK) {
            File file = new File(pathFoto);

            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.fromFile(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (imageBitmap != null) {

                //local.setImagen(imageBitmap);


            }

        }



    }


    private String saveToInternalStorage(Bitmap bitmapImage, long idlocal){
        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,idlocal+".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }


    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        pathFoto = image.getAbsolutePath();
        return image;
    }



}
