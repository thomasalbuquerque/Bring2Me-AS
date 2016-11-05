package com.example.echo.bring2me.MinhasViagensCadastradas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.echo.bring2me.BD_e_Controle.AppConfig;
import com.example.echo.bring2me.BD_e_Controle.AppController;
import com.example.echo.bring2me.MainActivity;
import com.example.echo.bring2me.BD_e_Controle.PopulateArray;
import com.example.echo.bring2me.R;
import com.example.echo.bring2me.BD_e_Controle.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CadastroViagem extends Activity{
    private static final String TAG = CadastroViagem.class.getSimpleName();
    private ArrayList<String> paises = new ArrayList<String>();
    private Spinner spPaisesOri;
    private Spinner spPaisesDes;
    private Button btnRegister;
    private EditText maxVal;
    private EditText mintax;
    private EditText retorno;
    private ProgressDialog pDialog;
    private SQLiteHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_viagens);
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        final String id = user.get("uid");

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        btnRegister = (Button) findViewById(R.id.btnRegistrarViagem);
        maxVal = (EditText) findViewById(R.id.txtValor) ;
        mintax = (EditText) findViewById(R.id.txtMinTax) ;
        retorno = (EditText) findViewById(R.id.txtData) ;
        retorno.addTextChangedListener(Mask.insert("####-##-##", retorno));
        db = new SQLiteHandler(getApplicationContext());

        PopulateArray.populatePaises(paises);
        spPaisesOri = (Spinner) findViewById(R.id.spOrigem);
        spPaisesDes = (Spinner) findViewById(R.id.spDestino);
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, paises);
        spPaisesOri.setAdapter(arrayAdapter1);
        spPaisesDes.setAdapter(arrayAdapter1);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String paisOrigem = spPaisesOri.getSelectedItem().toString();
                String paisDestino = spPaisesDes.getSelectedItem().toString();
                String maxValor = maxVal.getText().toString().trim();
                String minTax = mintax.getText().toString().trim();
                String data = retorno.getText().toString().trim();


                if (!paisOrigem.isEmpty() && !paisDestino.isEmpty() && !maxValor.isEmpty() && !data.isEmpty()) {
                    if(Integer.parseInt(data.substring(5,7))>12 || Integer.parseInt(data.substring(5,7))<1)
                        Toast.makeText(getApplicationContext(),
                                "Data Invalida!", Toast.LENGTH_LONG)
                                .show();
                    else
                        registrarViagem(id, paisOrigem, paisDestino, maxValor, minTax, data);
                }
                 else {
                    Toast.makeText(getApplicationContext(),
                            "Cadastro incompleto, Por favor insira seus dados!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });


    }


    private void registrarViagem(final String user_id, final String paisOrigem, final String paisDestino, final String maxVal,final String mintax , final String retorno) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registrando ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_VIAGENS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String user_id= jObj.getString("id");
                        String paisOrigem = jObj.getString("paisAtual");
                        String paisDestino = jObj.getString("paisDestino");
                        String maxVal = jObj.getString("maxval");
                        String mintax = jObj.getString("mintax");
                        String retorno  = jObj.getString("retorno");


                        // Inserting row in users table
                        db.addViagem(user_id, paisOrigem, paisDestino, maxVal, mintax, retorno);

                        Toast.makeText(getApplicationContext(), "Cadastro realizado com sucesso.", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                CadastroViagem.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        })
        {
        @Override
        protected Map<String, String> getParams() {
            // Posting params to register url
            Map<String, String> params = new HashMap<String, String>();
            params.put("user_id", user_id);
            params.put("paisAt", paisOrigem);
            params.put("paisDest", paisDestino);
            params.put("maxval", maxVal);
            params.put("mintax", mintax);
            params.put("retorno", retorno);

            return params;
        }

    };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
