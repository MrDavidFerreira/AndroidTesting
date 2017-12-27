package com.umb.juanferreira.androidtesting;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity implements View.OnClickListener{

    private StringBuffer sb;
    private EditText editTextURL, editTextSalida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextURL = findViewById(R.id.url);
        editTextSalida = findViewById(R.id.salida);

        Button button = findViewById(R.id.enviar);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.d("JuanDa", "Dio CLIC!");
        new SendPostRequest().execute();
    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {
            sb = new StringBuffer();

            try {

                URL url = new URL(editTextURL.getText().toString());
                Log.i("JuanDa", url.toString());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setInstanceFollowRedirects(false); /* added line */
                urlConnection.setRequestMethod("GET");
//                urlConnection.setRequestProperty("Content-length", "0");
//                urlConnection.setUseCaches(false);
//                urlConnection.setAllowUserInteraction(false);
//                urlConnection.setConnectTimeout(100000);
//                urlConnection.setReadTimeout(100000);
                urlConnection.connect();

                int responseCode = urlConnection.getResponseCode();

                Log.i("JuanDa", "RESPONSE: " + responseCode);

                if( responseCode == HttpURLConnection.HTTP_OK) {
                    Log.i("JuanDa", "HTTP OK");
                    InputStream inputStream = urlConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    Log.i("JuanDa", "Aquí bien!");
                    //StringBuffer sb = new StringBuffer();
                    String lineaLeida;
                    Log.i("JuanDa", "Aquí bien!");
                    while ((lineaLeida = bufferedReader.readLine()) != null) {
                        sb.append(lineaLeida + "\n");
                    }
                    //editTextSalida.setText(sb);
                    bufferedReader.close();
                    Log.i("JuanDa", "Aquí bien!1");

                    Log.i("JuanDa", "Aquí bien!2");
                    return "OK";
                } else {
                    Log.i("JuanDa", "HTTP NOT OK");
                    return "Error";
                }
            } catch (Exception e) {
                Log.e("JuanDa", "Error: " + e.toString());
                editTextSalida.setText("Error: " + e.toString());
                Log.i("JuanDa", "EXCEPCIOOOOOOOOON");
                return "Exception";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            editTextSalida.setText(sb);
        }
    }
}