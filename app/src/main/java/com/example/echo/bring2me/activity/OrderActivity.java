package com.example.echo.bring2me.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.echo.bring2me.URLRequests;
import com.example.echo.bring2me.util.BuscarCepTask;
import com.example.echo.bring2me.R;
import com.example.echo.bring2me.data.RequestSender;
import com.example.echo.bring2me.data.SQLiteHandler;
import com.example.echo.bring2me.SessionManager;
import com.example.echo.bring2me.util.ToggleableRadioButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OrderActivity extends Activity {
    private static final String TAG = OrderActivity.class.getSimpleName();
    private EditText inputValor;
    private EditText inputLink;
    private EditText inputProduct;
    private EditText inputCep;
    private EditText inputnumresid;
    private EditText inputcomplemento;
    private EditText bairroview;
    private EditText logradouroview;
    private EditText ufview;
    private EditText localidadeview;
    private Button btnOrder;
    private Button buscacep;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private String id_viagem;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_order);

        inputValor = (EditText) findViewById(R.id.valor);
        inputLink = (EditText) findViewById(R.id.link);
        inputProduct = (EditText) findViewById(R.id.produto);
        inputCep = (EditText) findViewById(R.id.cep);
        inputnumresid = (EditText) findViewById(R.id.numresidenciaedit);
        inputcomplemento = (EditText) findViewById(R.id.complementoedit);
        btnOrder = (Button) findViewById(R.id.btnOrder);
        buscacep = (Button) findViewById(R.id.buscarcep);
        bairroview = (EditText) findViewById(R.id.bairroview);
        logradouroview = (EditText) findViewById(R.id.logradouroview);
        ufview = (EditText) findViewById(R.id.ufview);
        localidadeview = (EditText) findViewById(R.id.localidadeview);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        HashMap<String, String> user = db.getUserDetails();

        final String email = user.get("email");

        final RadioGroup radioGroup1 = (RadioGroup)findViewById(R.id.radioGroup);
        final ToggleableRadioButton lC1Embalagem = (ToggleableRadioButton)findViewById(R.id.toggleableRadioButtonEmbalagem);
        lC1Embalagem.toggle();
        final ToggleableRadioButton lC2Correio = (ToggleableRadioButton)findViewById(R.id.toggleableRadioButtonCorreio);
        lC2Correio.toggle();
        final ToggleableRadioButton lC3Pessoalmente = (ToggleableRadioButton)findViewById(R.id.toggleableRadioButtonPessoalmente);
        lC3Pessoalmente.toggle();


        buscacep.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String cep = inputCep.getText().toString().trim();
                BuscarCepTask buscarCep = new BuscarCepTask(getApplicationContext(),bairroview, logradouroview, ufview, localidadeview);
                buscarCep.execute(cep);

                }
        });


        //Pegando o id da viagem do intent anterior
        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            id_viagem = extras.getString("id_viagem");
        }

        btnOrder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String valor = inputValor.getText().toString().trim();
                String link = inputLink.getText().toString().trim();
                String product = inputProduct.getText().toString().trim();
                String cep = inputCep.getText().toString().trim();
                String numresid = inputnumresid.getText().toString().trim();
                String complemento= inputcomplemento.getText().toString().trim();
                String caixa, entrega;
                if(lC1Embalagem.isChecked()){
                    caixa = "1";
                }
                else caixa = "0";
                if(lC2Correio.isChecked()){
                    entrega = "0";
                }
                else if(lC3Pessoalmente.isChecked()){
                    entrega = "1";
                }
                else entrega="0";

                String bairro = bairroview.getText().toString().trim();
                String logradouro = logradouroview.getText().toString().trim();
                String uf = ufview.getText().toString().trim();
                String localidade = localidadeview.getText().toString().trim();
                String endereco = cep + ", " + logradouro + ", " +  bairro +
                        ", "  + uf + ", "  + localidade + ", " + numresid + ", " + complemento;
                if (!valor.isEmpty() && !product.isEmpty() && !cep.isEmpty() && !bairro.isEmpty() &&
                        !logradouro.isEmpty() && !uf.isEmpty() && !localidade.isEmpty() && !numresid.isEmpty()) {
                    Order( valor, link , product, email, id_viagem, caixa, endereco, entrega);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Não foi possível iniciar uma negociação. Verifique se os campos estão preenchidos!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    private void Order(final String valor, final String link, final String product, final String email, final String id_viagem,
                       final String caixa, final String endereco, final String entrega) {
        // Tag used to cancel the request
        String tag_string_req = "req_order";

        pDialog.setMessage("Enviando Pedido ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                URLRequests.URL_ORDER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Order Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        JSONObject pedido = jObj.getJSONObject("order");
                        int id_p = pedido.getInt("id_pedido");

                        // inserir no SQLITE AQUI (precisa criar uma tabela de pedidos no SQLiteHandler)

                        Toast.makeText(getApplicationContext(), "Pedido realizado com sucesso.", Toast.LENGTH_LONG).show();

                        // Launch activity_main activity
                        Intent intent = new Intent(
                                OrderActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
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
                Log.e(TAG, "Order Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to order url
                Map<String, String> params = new HashMap<>();
                params.put("valor", valor);
                params.put("link", link);
                params.put("product", product);
                params.put("email", email);
                params.put("id_viagem", id_viagem);
                params.put("empacotado", caixa);
                params.put("adress", endereco);
                params.put("entrega", entrega);
                return params;
            }

        };

        // Adding request to request queue
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

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the activity_login activity
        Intent intent = new Intent(OrderActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
