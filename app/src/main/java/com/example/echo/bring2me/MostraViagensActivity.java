package com.example.echo.bring2me;

/**
 * Created by thomas on 17/09/16.
 */

import com.example.echo.bring2me.listview.adapter.CustomListAdapter;
import com.example.echo.bring2me.AppController;
import com.example.echo.bring2me.listview.model.Viagem;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

public class MostraViagensActivity extends Activity {
    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();

    // Movies json url
    private static final String url = "http://api.androidhive.info/json/movies.json";   //POR AQUI A URL DO PHP DE BUSCA VIAGEM
    private ProgressDialog pDialog;
    private List<Viagem> viagemList = new ArrayList<Viagem>();
    private ListView listView;
    private CustomListAdapter adapter;
    private Button btnBusca;
    private EditText inputOrigem;
    private EditText inputDestino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostraviagens);

        inputOrigem = (EditText) findViewById(R.id.origem);
        inputDestino = (EditText) findViewById(R.id.destino);
        btnBusca = (Button) findViewById(R.id.btnBusca);

        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, viagemList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        // Login button Click Event
        btnBusca.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String origem = inputOrigem.getText().toString().trim();
                String destino = inputDestino.getText().toString().trim();

                // Check for empty data in the form
                if (!origem.isEmpty() && !destino.isEmpty()) {
                    // login user
                    buscar(origem, destino);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Insira seu origem e destino!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });
        // changing action bar color
        getActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#1b1b1b")));

        //
    }
    public void buscar(String origem, String destino){
        // Creating volley request obj
        JsonArrayRequest viagemReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Viagem viagem = new Viagem();
                                viagem.setOrigem(obj.getString("origem"));
                                viagem.setDestino(obj.getString("destino"));
                                viagem.setThumbnailUrl(AppConfig.URL_IMAGEM);
                                viagem.setAvaliacaoViajante(AppConfig.AvaliacaoPadraoDoViajante);
                                viagem.setPrecoBase(obj.getDouble("precobase"));


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(viagemReq);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
*/
}
