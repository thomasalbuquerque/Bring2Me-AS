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
        import com.example.echo.bring2me.activity.LoginActivity;
        import com.example.echo.bring2me.activity.URLRequests;
        import com.example.echo.bring2me.data.RequestSender;
        import com.example.echo.bring2me.data.SQLiteHandler;
        import com.example.echo.bring2me.SessionManager;
        import com.example.echo.bring2me.model.User;

public class RemoveContaActivity extends Activity{
    private static final String TAG = RemoveContaActivity.class.getSimpleName();
    private Button btnExlcluirConta;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private static SQLiteHandler db;
    private static SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deletar_conta);


        inputPassword = (EditText) findViewById(R.id.txtSenha);
        btnExlcluirConta = (Button) findViewById(R.id.btnExcluir);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());
        final HashMap<String, String> user = db.getUserDetails();



        btnExlcluirConta.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!password.isEmpty()) {
                    removeConta(user.get("email"), password);

                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Insira a senha para excluir!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });}

        public void removeConta(final String email, final String password) {
            // Tag used to cancel the request


            String tag_string_req = "Check_Password";
            StringRequest strReq = new StringRequest(Method.POST,
                    URLRequests.URL_REMOVECONTA, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    boolean error;
                    Log.d("Remove Response: " , response.toString());

                    try {
                        Log.i("tagconvertstr", "["+response+"]");
                        JSONObject jObj = new JSONObject(response);
                        error = jObj.getBoolean("error");

                    if (!error) {
                        session.setLogin(false);

                        db.deleteUsers();
                        Context context = getApplicationContext();
                         //Launching the login activity
                        Intent intent = new Intent(RemoveContaActivity.this,
                                LoginActivity.class);
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
                    Log.e(TAG, "Remove erro: " + error.getMessage());
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", email);
                    params.put("password", password);
                    return params;
                }

            };

            // Adding request to request queue
            RequestSender.getInstance().addToRequestQueue(strReq, tag_string_req);
        }

    }

