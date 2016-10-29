package com.example.echo.bring2me;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BuscaViagensActivity extends Activity {
    private Button btnBusca;
    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private EditText inputOrigem;
    private EditText inputDestino;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buscaviagens);

        btnBusca = (Button) findViewById(R.id.btnbuscar);
        inputOrigem = (EditText) findViewById(R.id.origem);
        inputDestino = (EditText) findViewById(R.id.destino);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());
        // Login button Click Event
        btnBusca.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(BuscaViagensActivity.this, MostraViagensActivity.class);
                i.putExtra("inputOrigem", inputOrigem.getText().toString().trim());
                i.putExtra("inputDestino", inputDestino.getText().toString().trim());
                startActivity(i);
                //finish();
            }
        });
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