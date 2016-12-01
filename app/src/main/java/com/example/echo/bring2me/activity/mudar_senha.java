package com.example.echo.bring2me.activity;

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
import com.example.echo.bring2me.activity.MainActivity;
import com.example.echo.bring2me.URLRequests;
import com.example.echo.bring2me.data.RequestSender;
import com.example.echo.bring2me.data.SQLiteHandler;
import com.example.echo.bring2me.SessionManager;
import com.example.echo.bring2me.model.User;

public class mudar_senha extends Activity {
    private static final String TAG = mudar_senha.class.getSimpleName();
    private Button btnAtualizar;
    private EditText inputPasswordNew;
    private EditText inputPasswordOld;
    private ProgressDialog pDialog;
    private static SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mudar_senha);

        inputPasswordNew = (EditText) findViewById(R.id.senhaNova);
        inputPasswordOld = (EditText) findViewById(R.id.senhaAtual);
        btnAtualizar = (Button) findViewById(R.id.btnAtualizar);



        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());
        final HashMap<String, String> user = db.getUserDetails();


        btnAtualizar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String passwordOld = inputPasswordOld.getText().toString().trim();
                String passwordNew = inputPasswordNew.getText().toString().trim();
                // Check for empty data in the form
                if (!passwordOld.isEmpty() && !passwordNew.isEmpty()) {
                    mudarSenha(user.get("uid"),user.get("email"), passwordNew,passwordOld);

                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Insira os dados para atualizar!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });}

    public void mudarSenha(final String uid,final String email, final String passwordNew,final String passwordOld) {
        // Tag used to cancel the request


        String tag_string_req = "Mudar Senha";
        StringRequest strReq = new StringRequest(Method.POST,
                URLRequests.URL_MUDARSENHA, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                boolean error;
                Log.d("Mudar Senha Response: " , response.toString());

                try {
                    Log.i("tagconvertstr", "["+response+"]");
                    JSONObject jObj = new JSONObject(response);
                    error = jObj.getBoolean("error");

                    if (!error) {
                        Toast.makeText(getApplicationContext(),
                                "Alteração realizada!", Toast.LENGTH_LONG)
                                .show();
                        Intent intent = new Intent(
                                mudar_senha.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        String errorMsg = jObj.getString("error_msg");

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Mudar senha erro: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("unique_id", uid);
                params.put("password", passwordOld);
                params.put("email", email);
                params.put("new_password", passwordNew);

                return params;
            }

        };

        // Adding request to request queue
        RequestSender.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}

