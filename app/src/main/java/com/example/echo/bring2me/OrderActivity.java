package com.example.echo.bring2me;

import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.example.echo.bring2me.R;
import com.example.echo.bring2me.AppConfig;
import com.example.echo.bring2me.AppController;
import com.example.echo.bring2me.SQLiteHandler;
import com.example.echo.bring2me.SessionManager;

public class OrderActivity extends Activity {
    private static final String TAG = OrderActivity.class.getSimpleName();
    private EditText inputValor;
    private EditText inputLink;
    private EditText inputProduct;
    private Button btnOrder;
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
        btnOrder = (Button) findViewById(R.id.btnOrder);

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

        //Pegando o id da viagem do intent anterior
        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            id_viagem = extras.getString("id_viagem");
        }

        // Order Button Click event
        btnOrder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String valor = inputValor.getText().toString().trim();
                String link = inputLink.getText().toString().trim();
                String product = inputProduct.getText().toString().trim();

                if (!valor.isEmpty() && !product.isEmpty()) {
                    Order(product, valor, link ,email, id_viagem);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Não foi possível iniciar uma negociação. Verifique se os campos estão preenchidos!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    private void Order(final String valor, final String link, final String product, final String email, final String id_viagem) {
        // Tag used to cancel the request
        String tag_string_req = "req_order";

        pDialog.setMessage("Enviando Pedido ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_ORDER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Order Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL

                        // inserir no SQLITE AQUI (precisa criar uma tabela de pedidos no SQLiteHandler)

                        Toast.makeText(getApplicationContext(), "Pedido realizado com sucesso.", Toast.LENGTH_LONG).show();

                        // Launch main activity
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("valor", valor);
                params.put("link", link);
                params.put("product", product);
                params.put("email", email);
                params.put("id_viagem", id_viagem);

                return params;
            }

        };

        // Adding request to request queue
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

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(OrderActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
