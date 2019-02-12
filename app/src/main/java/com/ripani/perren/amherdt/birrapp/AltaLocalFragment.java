package com.ripani.perren.amherdt.birrapp;


import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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
import static java.lang.Integer.parseInt;


/**
 * A simple {@link Fragment} subclass.
 */
public class AltaLocalFragment extends Fragment {

    public interface OnNuevoLugarListener {
        public void obtenerCoordenadas();
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
    private EditText etTel;
    private RadioButton rbAdmite;
    private RadioButton rbNoAdmite;
    private Local local = new Local();
    private LocalDao localDao;
    private CervezaRepositorio repositorio = new CervezaRepositorio();
    private long idLocal;
    private String pathFoto;
    private String coordenadas;
    static final int REQUEST_IMAGE_SAVE = 2;
    private static final int REQUEST_WRITE_EXTERNAL_PERMISSION = 1;
    Bitmap imageBitmap = null;


    public AltaLocalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.fragment_alta_local, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        localDao = MyDataBase.getInstance(this.getActivity()).getLocalDao();


        btnAñadirCervezas = v.findViewById(R.id.btnAñadirCervezas);
        btnCancelar = v.findViewById(R.id.btnCancelar);
        btnCrear = v.findViewById(R.id.btnCrear);
        btnImagen = v.findViewById(R.id.btnImagen);
        btnBuscarUbicacion = v.findViewById(R.id.btnBuscarUbicacion);
        etNombreLocal = v.findViewById(R.id.etNombreLocal);
        etHoraApertura = v.findViewById(R.id.etHoraApertura);
        etHoraCierre = v.findViewById(R.id.etHoraCierre);
        etCapacidad = v.findViewById(R.id.etCapacidad);
        rbAdmite = v.findViewById(R.id.rbAdmite);
        rbNoAdmite = v.findViewById(R.id.rbNoAdmite);
        etTel = v.findViewById(R.id.etTel);


        coordenadas = "0;0";


        etNombreLocal.setText("test");
        etCapacidad.setText("10");
        etHoraApertura.setText("10:30");
        etHoraCierre.setText("19:30");


        if (getArguments() != null) coordenadas = getArguments().getString("latLng", "0;0");

        btnBuscarUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerLugar.obtenerCoordenadas();

            }
        });



        btnImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Permiso Necesario")
                                .setMessage("Se requieren permisos de almacenamiento para agregar una foto a tu local")
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_PERMISSION);
                                    }
                                })
                                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Toast.makeText(getContext(),
                                                "Permisos no concedidos"
                                                ,
                                                Toast.LENGTH_LONG)
                                                .show();
                                    }
                                });
                        builder.create().show();
                    } else {

                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_WRITE_EXTERNAL_PERMISSION);

                    }
                }else{
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

            }
        });


        btnAñadirCervezas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ListaCervezas.class);
                intent.putStringArrayListExtra("cerveza", arrayCervezas);
                intent.putStringArrayListExtra("cervezaCancel", arrayCervezas);
                startActivityForResult(intent, 1);
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


                if (validar()) {


                    local.setNombre(etNombreLocal.getText().toString());
                    local.setHoraApertura(etHoraApertura.getText().toString());
                    local.setHoraCierre(etHoraCierre.getText().toString());
                    local.setCapacidad(parseInt(etCapacidad.getText().toString()));
                    local.setReservas(rbAdmite.isChecked());//si reservas es 1 admite
                    local.setTelefono(etTel.getText().toString());
                    List<Cerveza> listaCervezas = new ArrayList<Cerveza>();
                    for (String id : arrayCervezas) {
                        listaCervezas.add(repositorio.buscarCervezaPorId(parseInt(id)));
                    }
                    local.setCervezas(listaCervezas);


                    String coor = coordenadas;
                    if (coor.length() > 0 && coor.contains(";")) {
                        String[] coord = coor.split(";");
                        local.setLatitud(Double.valueOf(coord[0]));
                        local.setLongitud(Double.valueOf(coord[1]));
                    }


                    Runnable hiloActualizacion = new Runnable() {
                        @Override
                        public void run() {

                            if (local.getId() > 0) localDao.update(local);
                            else {
                                idLocal = localDao.insert(local);

                            }

                            if (imageBitmap != null) {
                                saveToInternalStorage(imageBitmap, idLocal);

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


                    Thread t2 = new Thread(hiloNotificacion);
                    t2.start();


                    getFragmentManager().beginTransaction().
                            remove(getFragmentManager().findFragmentById(R.id.contenedorFragmento)).commit();

                }
            }

        });


        return v;

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {
                    Toast.makeText(getContext(), "Sin permiso!", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {

            if (data != null) {
                arrayCervezas = (ArrayList<String>) data.getStringArrayListExtra("cervezas");
            }

        }


        if (requestCode == REQUEST_IMAGE_SAVE && resultCode == RESULT_OK) {
            File file = new File(pathFoto);

            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.fromFile(file));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }


    private String saveToInternalStorage(Bitmap bitmapImage, long idlocal) {
        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, idlocal + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);


           // bitmapImage = rotateImage(bitmapImage, 90);

            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 50, fos);
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

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }


    private boolean validar() {


        if (etNombreLocal.getText().length() == 0) {
            Toast.makeText(getContext(),
                    "Ingrese un nombre",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if (etCapacidad.getText().toString().equals("")) {
            Toast.makeText(getContext(),
                    "Ingrese la capacidad del local",
                    Toast.LENGTH_LONG).show();
            return false;

        }


            if (parseInt(etCapacidad.getText().toString()) < 1) {
                Toast.makeText(getContext(),
                        "La capacidad ingresada (" + etCapacidad.getText().toString() + ") es incorrecta",
                        Toast.LENGTH_LONG).show();
                return false;
            }


        if (!etHoraApertura.getText().toString().contains(":") || etHoraApertura.getText().toString().split(":")[0].length() != 2 || etHoraApertura.getText().toString().split(":")[1].length() != 2) {
            Toast.makeText(getContext(),
                    "La hora de apertura debe ser ingresada de la forma HH:MM",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        String[] horaApertura = etHoraApertura.getText().toString().split(":");
        int valorHoraA;
        try {
            valorHoraA = Integer.valueOf(horaApertura[0]);
        } catch (NumberFormatException excepcion) {
            Toast.makeText(getContext(),
                    "La hora de apertura debe ser un número",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        int valorMinutoA;
        try {
            valorMinutoA = Integer.valueOf(horaApertura[1]);
        } catch (NumberFormatException excepcion) {
            Toast.makeText(getContext(),
                    "Los minutos de apertura deben ser un números",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (valorHoraA < 0 || valorHoraA > 23) {
            Toast.makeText(getContext(),
                    "La hora de apertura ingresada (" + valorHoraA + ") es incorrecta",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (valorMinutoA < 0 || valorMinutoA > 59) {
            Toast.makeText(getContext(),
                    "Los minutos de apertura (" + valorMinutoA + ") son incorrectos",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if (!etHoraCierre.getText().toString().contains(":") || etHoraCierre.getText().toString().split(":")[0].length() != 2 || etHoraCierre.getText().toString().split(":")[1].length() != 2) {
            Toast.makeText(getContext(),
                    "La hora de cierre debe ser ingresada de la forma HH:MM",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        String[] horaCierre = etHoraCierre.getText().toString().split(":");
        int valorHora;
        try {
            valorHora = Integer.valueOf(horaCierre[0]);
        } catch (NumberFormatException excepcion) {
            Toast.makeText(getContext(),
                    "La hora de cierre debe ser un número",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        int valorMinuto;
        try {
            valorMinuto = Integer.valueOf(horaCierre[1]);
        } catch (NumberFormatException excepcion) {
            Toast.makeText(getContext(),
                    "Los minutos de cierre deben ser un números",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (valorHora < 0 || valorHora > 23) {
            Toast.makeText(getContext(),
                    "La hora de cierre ingresada (" + valorHora + ") es incorrecta",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (valorMinuto < 0 || valorMinuto > 59) {
            Toast.makeText(getContext(),
                    "Los minutos de cierre (" + valorMinuto + ") son incorrectos",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if(arrayCervezas.isEmpty()){
            Toast.makeText(getContext(),
                    "No ha añadido ninguna cerveza",
                    Toast.LENGTH_LONG).show();
            return false;
        }


        return true;

    }

}
