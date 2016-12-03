package com.example.echo.bring2me.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.echo.bring2me.SessionManager;
import com.example.echo.bring2me.URLRequests;
import com.example.echo.bring2me.util.DateMask;
import com.example.echo.bring2me.R;
import com.example.echo.bring2me.data.RequestSender;
import com.example.echo.bring2me.data.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PagamentoActivity extends Activity{
    private static final String TAG = PagamentoActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private AlertDialog alerta;
    private SessionManager session;
    private String id_pedido;

    private EditText nomeCartao;
    private EditText numeroCartao;
    private EditText cvv;
    private EditText mesAnoExpira;

    private Button btnPagar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_pagamento);
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        final String user_id = user.get("uid");

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        btnPagar = (Button) findViewById(R.id.btn_pagar);
        nomeCartao = (EditText) findViewById(R.id.nomeCartao) ;
        numeroCartao = (EditText) findViewById(R.id.numeroCartao) ;
        numeroCartao.addTextChangedListener(DateMask.insert("####-####-####-####", numeroCartao));
        cvv = (EditText) findViewById(R.id.cvv) ;
        mesAnoExpira = (EditText) findViewById(R.id.mesAnoExpira) ;
        mesAnoExpira.addTextChangedListener(DateMask.insert("##-##", mesAnoExpira));

        db = new SQLiteHandler(getApplicationContext());

        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            id_pedido = extras.getString("id_pedido");
        }

        btnPagar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String nomeNoCartao = nomeCartao.toString().trim();
                String numeroNoCartao = numeroCartao.toString().trim();
                String Cvv = cvv.getText().toString().trim();
                String MesAnoExpira = mesAnoExpira.getText().toString().trim();


                if (!nomeNoCartao.isEmpty() && !numeroNoCartao.isEmpty() && !Cvv.isEmpty() && !MesAnoExpira.isEmpty()) {
                    if(Integer.parseInt(MesAnoExpira.substring(0,2))>31 || Integer.parseInt(MesAnoExpira.substring(0,2))<1)
                        Toast.makeText(getApplicationContext(),
                                "Data Invalida!", Toast.LENGTH_LONG)
                                .show();
                    else
                        Alerta(id_pedido, nomeNoCartao, numeroNoCartao, Cvv, MesAnoExpira);
                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "Dados de pagamento incompletos, por favor insira-os corretamente!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });


    }


    private void pagarPedido(final String id_pedido, final String nomeCartao, final String numeroCartao, final String cvv,final String mesAnoExpira) {
        // Tag used to cancel the request
        String tag_string_req = "req_pagamento";

        pDialog.setMessage("Registrando pagamento ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URLRequests.URL_PAGARPedido, new Response.Listener<String>() {

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
                        String id_pedido= jObj.getString("id_pedido");
                        String nomeCartao = jObj.getString("nomeCartao");
                        String numeroCartao = jObj.getString("numeroCartao");
                        String cvv = jObj.getString("cvv");
                        String mesAnoExpira = jObj.getString("mesAnoExpira");


                        // Inserting row in users table
                        db.addPagamento(id_pedido, nomeCartao, numeroCartao, cvv, mesAnoExpira);

                        Toast.makeText(getApplicationContext(), "Pagamento realizado com sucesso.", Toast.LENGTH_LONG).show();

                        // Launch activity_login activity
                        Intent intent = new Intent(
                                PagamentoActivity.this,
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
                params.put("id_pedido", id_pedido);
                params.put("nomeCartao", nomeCartao);
                params.put("numeroCartao", numeroCartao);
                params.put("cvv", cvv);
                params.put("mesAnoExpira", mesAnoExpira);
                return params;
            }

        };

        RequestSender.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void Alerta(final String id_pedido, final String nomeNoCartao, final String numeroNoCartao, final String Cvv,final String MesAnoExpira ) {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Cadastro de Viagem");
        //define a mensagem
        builder.setMessage("Você tem certeza que deseja cadastrar a viagem?");

        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //Toast.makeText(DetalhesPedidoRecebidoActivity.this, "positivo=" + arg1, Toast.LENGTH_SHORT).show();
                pagarPedido(id_pedido, nomeNoCartao, numeroNoCartao, Cvv, MesAnoExpira);
            }
        });
        //define um botão como negativo.
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //Toast.makeText(DetalhesPedidoRecebidoActivity.this, "negativo=" + arg1, Toast.LENGTH_SHORT).show();
                pDialog.cancel();
            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }
}
