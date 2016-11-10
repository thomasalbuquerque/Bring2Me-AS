package com.example.echo.bring2me.activity;

/**
 * Created by thomas on 17/09/16.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.echo.bring2me.data.RequestSender;
import com.example.echo.bring2me.R;
import com.example.echo.bring2me.data.SQLiteHandler;
import com.example.echo.bring2me.adapter.ViagensCadastradasListAdapter;
import com.example.echo.bring2me.model.Viagem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ViagensCadastradasActivity extends Activity {
    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private List<Viagem> viagemList = new ArrayList<Viagem>();
    private ListView listView;
    private ViagensCadastradasListAdapter adapter;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_deleta_viagem);

        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        final String userViagemID = user.get("uid");

        listView = (ListView) findViewById(R.id.deletaViagens);
        adapter = new ViagensCadastradasListAdapter(this, viagemList);
        listView.setAdapter(adapter);

        // Showing progress dialog before making http request
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        buscar(userViagemID);

    }

    public void buscar(final String userID){
        // Creating volley request obj
        StringRequest viagemReq = new StringRequest(Request.Method.POST, URLRequests.URL_BUSCAVIAGENSCadastradas,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        hidePDialog();
                        try {
                            JSONObject res = new JSONObject(response);
                            // Parsing json

                            for (int i = 0; i < res.length()-1; i++) {


                                JSONObject array = new JSONObject(response);
                                JSONObject obj = (JSONObject) array.get(Integer.toString(i));
                                Viagem viagem = new Viagem();
                                viagem.setOrigem(obj.getString("cidadeorigem"));
                                viagem.setDestino(obj.getString("cidadedestino"));
                                viagem.setPrecoBase(obj.getDouble("precobase"));
                                viagem.setId(obj.getString("id"));

                                // adding viagem to viagens array
                                viagemList.add(viagem);
                            }

                        }catch (JSONException e) {
                            e.printStackTrace();
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
                // Posting parameters to activity_login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", userID);

                return params;
            }

        };

        // Adding request to request queue
        RequestSender.getInstance().addToRequestQueue(viagemReq);

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
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
*/
}
