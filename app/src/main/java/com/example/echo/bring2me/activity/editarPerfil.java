package com.example.echo.bring2me.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.example.echo.bring2me.activity.URLRequests;
import com.example.echo.bring2me.data.RequestSender;
import com.example.echo.bring2me.data.SQLiteHandler;
import com.example.echo.bring2me.SessionManager;
import com.example.echo.bring2me.model.User;

public class editarPerfil extends Activity {
    private static final String TAG = mudar_senha.class.getSimpleName();
    private Button btnAtualizar;
    private EditText inputName;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private static SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        inputName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);

        btnAtualizar = (Button) findViewById(R.id.btnAtualizar);



        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());
        final HashMap<String, String> user = db.getUserDetails();

        inputEmail.setText(user.get("email"));
        inputName.setText(user.get("name"));

        btnAtualizar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String new_email = inputEmail.getText().toString().trim();
                String nome = inputName.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!new_email.isEmpty() && !nome.isEmpty() && !password.isEmpty()) {
                    editarPerfil(user.get("uid"),nome,new_email,user.get("email"), password);

                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Insira os dados para atualizar!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });}
    public void editarPerfil(final String uid, final String nome, final String new_email, final String email, final String password) {
        // Tag used to cancel the request


        String tag_string_req = "Editar perfil";
        StringRequest strReq = new StringRequest(Method.POST,
                URLRequests.URL_EDITAPERFIL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                boolean error;
                Log.d("Editar Response: " , response.toString());

                try {
                    Log.i("tagconvertstr", "["+response+"]");
                    JSONObject jObj = new JSONObject(response);
                    error = jObj.getBoolean("error");;

                    if (!error) {

                        Toast.makeText(getApplicationContext(),
                                "Atualização realizada!", Toast.LENGTH_LONG)
                                .show();

                        Intent intent = new Intent(editarPerfil.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        String errorMsg = jObj.getString("error_msg");
                        Log.d("Editar Response: " , errorMsg);

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Editar erro: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("unique_id",uid );
                params.put("nome", nome);
                params.put("new_email", new_email);
                params.put("email", email);
                params.put("password", password);
                return params;
            }

        };

        // Adding request to request queue
        RequestSender.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}
