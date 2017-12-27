package com.umb.juanferreira.androidtesting;

import android.app.Activity;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Main2Activity extends Activity implements View.OnClickListener{

    private StringBuilder sb;
    TextView ip, cantidad, resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ip = this.findViewById(R.id.ip);
        cantidad =  this.findViewById(R.id.cantidad);
        resultado = this.findViewById(R.id.resultado);

        Button buttonDolares = findViewById(R.id.dolares);
        Button buttonEuros = findViewById(R.id.euros);

        buttonDolares.setOnClickListener(this);
        buttonEuros.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        String moneda = "EUR";
        if (v.getId() == R.id.dolares) moneda = "USD";

        new SendPostRequest().execute(moneda);
    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {
            sb = new StringBuilder();

            String moneda = arg0[0];
            Log.i("Moneda: ", moneda);
            try {
                ContentValues values = new ContentValues();
                values.put("cantidad", cantidad.getText().toString());
                values.put("moneda", moneda);

                String query = getQuery(values);
                Log.i("Query", query);

                URL url = new URL("http://" + ip.getText() + ":8080/WebApplication1/NewServlet");
                Log.i("URL", url.toString());
                URLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);

                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(
                                urlConnection.getOutputStream(), "UTF-8"));

                writer.write(query);
                writer.flush();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                urlConnection.getInputStream()));

                sb = new StringBuilder();
                String lineaLeida = null;
                while ((lineaLeida = reader.readLine()) != null) {
                    sb.append(lineaLeida + "\n");
                }


                //int statusCode = urlConnection.getResponseCode();
                //Log.i("StatusCode", " The status code is " + statusCode);

                writer.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("Exception", e.getMessage());
            } finally {
                return sb.toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("RESUL", result);
            resultado.setText(sb.toString());
        }

        private String getQuery(ContentValues values) throws UnsupportedEncodingException
        {
            StringBuilder result = new StringBuilder();
//        boolean first = true;

            for (Map.Entry<String, Object> entry : values.valueSet())
            {
//            if (first)
//                first = false;
//            else
//                result.append("?");

                result.append("&");
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8"));
            }

            return result.toString();
        }
    }
}
