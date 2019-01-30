package com.ripani.perren.amherdt.birrapp.modelo;


import android.util.Log;

import com.ripani.perren.amherdt.birrapp.dao.CervezaRepositorio;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CervezaRest {

    public CervezaRest() {
    }

    public void crearEstilo(Cerveza c) {


        try {

            //Variables de conexión y stream de escritura y lectura
            HttpURLConnection urlConnection = null;
            DataOutputStream printout = null;
            InputStream in = null;
            //Crear el objeto json que representa una cerveza


            JSONObject categoriaJson = new JSONObject();
            categoriaJson.put("marca", c.getMarca());
            //categoriaJson.put("estiloId", c.getEstilo().getId());

            //Abrir una conexión al servidor para enviar el POST


            URL url = new URL("http://10.0.2.2:2700/cerveza");


            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setRequestProperty("Content-Type","application/json");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            //Obtener el outputStream para escribir el JSON
            printout = new DataOutputStream(urlConnection.getOutputStream());
            String str = categoriaJson.toString();
            byte[] jsonData = str.getBytes("UTF-8");
            printout.write(jsonData);
            printout.flush();
            //Leer la respuesta
            in = new BufferedInputStream(urlConnection.getInputStream());
            InputStreamReader isw = new InputStreamReader(in);
            StringBuilder sb = new StringBuilder();
            int data = isw.read();
            //Analizar el codigo de lar respuesta
            if (urlConnection.getResponseCode() == 200 ||
                    urlConnection.getResponseCode() == 201) {
                while (data != -1) {
                    char current = (char) data;
                    sb.append(current);

                    data = isw.read();
                }
                //analizar los datos recibidos
                Log.d("TP_FINAL", sb.toString());
            } else {
                // lanzar excepcion o mostrar mensaje de error
                // que no se pudo ejecutar la operacion
            }
            // caputurar todas las excepciones y en el bloque finally
            // cerrar todos los streams y HTTPUrlCOnnection
            if (printout != null) try {
                printout.close();
            }
            finally {
                urlConnection.disconnect();
            }
            if (in != null) try {
                in.close();
            }
            finally {
                urlConnection.disconnect();
            }
        }
        catch(JSONException ex){
            System.out.println("JSONException" +ex);
        }
        catch(MalformedURLException ex){
            System.out.println("MalformedURLException" +ex);
        }
        catch(IOException ex){
            System.out.println("IOException" +ex);
        }



    }

// definir el método

    public List<Cerveza> listarTodas(){
        List<Cerveza> resultado = new ArrayList<>();
        try {
// inicializar variables
            HttpURLConnection urlConnection = null;
            InputStream in =null;
// GESTIONAR LA CONEXION
            URL url = new URL("http://10.0.2.2:2700/cerveza");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept-Type","application/json");
            urlConnection.setRequestMethod("GET");
// Leer la respuesta
            in = new BufferedInputStream(urlConnection.getInputStream());
            InputStreamReader isw = new InputStreamReader(in);
            StringBuilder sb = new StringBuilder();
            int data = isw.read();
// verificar el codigo de respuesta
            if( urlConnection.getResponseCode() ==200 ||
                    urlConnection.getResponseCode()==201){
                while (data != -1) {
                    char current = (char) data;
                    sb.append(current);
                    data = isw.read();
                }
                // ver datos recibidos
                Log.d("TP_FINAL",sb.toString());
                // Transformar respuesta a JSON
                JSONTokener tokener = new JSONTokener(sb.toString());
                JSONArray listaCerveza = (JSONArray) tokener.nextValue();

                // iterar todas las entradas del arreglo
                for(int i=0;i<listaCerveza.length();i++){
                    Cerveza cerveza = new Cerveza();
                    ///////////////////////////
                    cerveza.setMarca(listaCerveza.getJSONObject(i).getString("marca"));
                    /*List<Integer> listaEstilosId = (List<Integer>) listaCerveza.getJSONObject(i).getJSONArray("estiloId");
                    List<Estilo> listaEstilos = new ArrayList<Estilo>();
                    for(Integer id: listaEstilosId) {
                        Estilo estilo = CervezaRepositorio.buscarEstiloPorId(id);
                        listaEstilos.add(estilo);
                    }

                    cerveza.setEstilo(listaEstilos);*/
                    cerveza.setEstilo(CervezaRepositorio.buscarEstiloPorId(listaCerveza.getJSONObject(i).getInt("estiloId")));
                    cerveza.setId(listaCerveza.getJSONObject(i).getInt("id"));
                    // analizar cada element del JSONArray
                    //armar una instancia de categoría y agregarla a la lista
                    resultado.add(cerveza);
                }
            }else{
                // lanzar excepcion o mostrar mensaje de error
                // que no se pudo ejecutar la operacion
            }
//NO OLVIDAR CERRAR inputStream y conexion
            if (urlConnection !=null) try {
                urlConnection.disconnect();
            }
            finally {
                urlConnection.disconnect();
            }
            if (in != null) try {
                in.close();
            }
            finally {
                urlConnection.disconnect();
            }}
        catch(JSONException e){
            System.out.println("JSONException" +e);
        }
        catch(MalformedURLException e){
            System.out.println("MalformedURLException" +e);
        }
        catch(IOException e){
            System.out.println("IOException" +e);
        }

        return resultado;
    }

}



