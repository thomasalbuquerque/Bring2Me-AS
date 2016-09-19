package com.example.echo.bring2me;

/**
 * Created by thomas on 17/09/16.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.echo.bring2me.listview.adapter.CustomListAdapter;
import com.example.echo.bring2me.listview.model.Viagem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MostraViagensActivity extends Activity {
    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private List<Viagem> viagemList = new ArrayList<Viagem>();
    private ListView listView;
    private CustomListAdapter adapter;
    private SQLiteHandler db;
    String origem;
    String destino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostraviagens);

        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, viagemList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        pDialog.setMessage("Loading...");
        pDialog.show();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            origem = extras.getString("inputOrigem");
            destino = extras.getString("inputDestino");
        }
        // Check for empty data in the form
        if (!origem.isEmpty() && !destino.isEmpty()) {
            // busca viagem
            buscar(origem, destino);
        } else {
            // diga para digitar origem e destino
            Toast.makeText(getApplicationContext(),
                    "Insira seu origem e destino!", Toast.LENGTH_LONG).show();
        }

        // changing action bar color
        getActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#1b1b1b")));
    }


    public void buscar(final String origem, final String destino){
        // Creating volley request obj
        JsonArrayRequest viagemReq = new JsonArrayRequest(AppConfig.URL_BUSCAVIAGENS,
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

                                // adding viagem to viagens array
                                viagemList.add(viagem);

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
        }){

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("origem", origem);
                params.put("destino", destino);

                return params;
            }

        };

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
