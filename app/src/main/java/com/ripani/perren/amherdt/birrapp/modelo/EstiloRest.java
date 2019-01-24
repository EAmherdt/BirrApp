package com.ripani.perren.amherdt.birrapp.modelo;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EstiloRest {

    public EstiloRest() {
    }

    // realiza el POST de una categoría al servidor REST
    public void crearEstilo(Estilo e) {


        try {

            //Variables de conexión y stream de escritura y lectura
            HttpURLConnection urlConnection = null;
            DataOutputStream printout = null;
            InputStream in = null;
            //Crear el objeto json que representa una categoria


            JSONObject categoriaJson = new JSONObject();
            categoriaJson.put("nombre", e.getNombre());


            //Abrir una conexión al servidor para enviar el POST


            URL url = new URL("http://10.0.2.2:2700/estilo");


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

    public List<Estilo> listarTodas(){
        List<Estilo> resultado = new ArrayList<>();
        try {
// inicializar variables
            HttpURLConnection urlConnection = null;
            InputStream in =null;
// GESTIONAR LA CONEXION
            URL url = new URL("http://10.0.2.2:2700/estilo");
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
                JSONArray listaEstilo = (JSONArray) tokener.nextValue();

                // iterar todas las entradas del arreglo
                for(int i=0;i<listaEstilo.length();i++){
                    Estilo estilo = new Estilo();
                    ///////////////////////////
                    estilo.setNombre(listaEstilo.getJSONObject(i).getString("nombre"));
                    estilo.setId(listaEstilo.getJSONObject(i).getInt("id"));
                    // analizar cada element del JSONArray
                    //armar una instancia de categoría y agregarla a la lista
                    resultado.add(estilo);
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





