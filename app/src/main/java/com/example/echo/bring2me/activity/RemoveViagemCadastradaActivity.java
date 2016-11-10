package com.example.echo.bring2me.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.echo.bring2me.AppController;
import com.example.echo.bring2me.R;
import com.example.echo.bring2me.SQLiteHandler;
import com.example.echo.bring2me.data.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by thomas on 27/10/16.
 */

public class RemoveViagemCadastradaActivity extends Activity{
    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private String userIDfromAnterior;
    private String paisAtual;
    private String paisDestino;

    private Button btn_remove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacao_de_remocao_da_viagem);



        pDialog = new ProgressDialog(this);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        btn_remove = (Button) findViewById(R.id.btnRemove);

        /*// Check for empty data in the form
        if (!userIDfromAnterior.isEmpty() && !paisAtual.isEmpty() && !paisDestino.isEmpty()) {
            // busca viagem
            buscar(origem, destino);
        } else {
            // diga para digitar origem e destino
            Toast.makeText(getApplicationContext(),
                    "Insira sua origem e destino!", Toast.LENGTH_LONG).show();
        }*/

        btn_remove.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                pDialog.setMessage("Loading...");
                pDialog.show();
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    userIDfromAnterior = extras.getString("user_id");
                    paisAtual = extras.getString("paisAtual");
                    paisDestino = extras.getString("paisDestino");
                }
                StringRequest viagemReq = new StringRequest(Request.Method.POST, AppConfig.URL_REMOVEVIAGEMCadastrada,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                Log.d(TAG, response);
                                hidePDialog();
                                try {
                                    JSONObject res = new JSONObject(response);

                                    boolean error = res.getBoolean("error");
                                    if (!error) {
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else    // Error in deleting. Get the error message
                                    {
                                        String errorMsg = res.getString("error_msg");
                                        Toast.makeText(getApplicationContext(),
                                                errorMsg, Toast.LENGTH_LONG).show();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        hidePDialog();

                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() {
                        // Posting parameters to activity_login url
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("user_id", userIDfromAnterior);
                        params.put("paisAtual", paisAtual);
                        params.put("paisDestino", paisDestino);
                        return params;
                    }

                };
                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(viagemReq);
            }
        });
    }

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

}

