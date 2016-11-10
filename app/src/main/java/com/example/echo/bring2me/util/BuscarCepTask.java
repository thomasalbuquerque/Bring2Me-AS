package com.example.echo.bring2me.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;

public class BuscarCepTask extends AsyncTask<String, Void, String> {
    URL url = null;
    HttpURLConnection httpURLConnection = null;
    private EditText bairroview;
    private EditText logradouroview;
    private EditText ufview;
    private EditText localidadeview;
    private Context context;

    private String bairro;
    private String logradouro;
    private String uf;
    private String localidade;


    public BuscarCepTask(Context c, EditText bairroview, EditText logradouroview, EditText ufview, EditText localidadeview) {
        this.context = c;
        this.bairroview = bairroview;
        this.logradouroview = logradouroview;
        this.ufview = ufview;
        this.localidadeview = localidadeview;

    }

    @Override
    protected String doInBackground(String... params) {
        StringBuilder result = null;
        int respCode = -1;

        try {
            url = new URL("http://viacep.com.br/ws/" + params[0] + "/json/");
            Log.d("buscacep", "Order Response: " + "http://viacep.com.br/ws/" + params[0] + "/json/");
            httpURLConnection = (HttpURLConnection) url.openConnection();

            do {
                if (httpURLConnection != null) {
                    respCode = httpURLConnection.getResponseCode();
                }
            } while (respCode == -1);

            if (respCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                result = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
                httpURLConnection = null;
            }
        }
        Log.d("buscacep", "Order Response: " + result.toString());
        return(result != null) ? result.toString() : null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {
            JSONObject object = new JSONObject(s);
            //object.
            if(!object.isNull("erro"))
            Toast.makeText(context,"CEP inválido ou não encontrado.",Toast.LENGTH_LONG).show();
            else {
                bairro = object.getString("bairro");
                logradouro = object.getString("logradouro");
                uf = object.getString("uf");
                localidade = object.getString("localidade");
                bairroview.setText(bairro);
                logradouroview.setText(logradouro);
                ufview.setText(uf);
                localidadeview.setText(localidade);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}