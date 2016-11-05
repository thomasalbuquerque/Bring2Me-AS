package com.example.echo.bring2me.MostraViajensDeOutrosViajantes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.echo.bring2me.BD_e_Controle.PopulateArray;
import com.example.echo.bring2me.R;
import com.example.echo.bring2me.BD_e_Controle.SQLiteHandler;

import java.util.ArrayList;

public class BuscaViagensActivity extends Activity {
    private Button btnBusca;
    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private Spinner inputOrigem;
    private Spinner inputDestino;
    private ArrayList<String> paises = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buscaviagens);

        btnBusca = (Button) findViewById(R.id.btnbuscar);
        inputOrigem = (Spinner) findViewById(R.id.origem);
        inputDestino = (Spinner) findViewById(R.id.destino);

        PopulateArray.populatePaises(paises);

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, paises);
        inputOrigem.setAdapter(arrayAdapter1);
        inputDestino.setAdapter(arrayAdapter1);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());
        // Login button Click Event
        btnBusca.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(BuscaViagensActivity.this, MostraViagensActivity.class);
                i.putExtra("inputOrigem", inputOrigem.getSelectedItem().toString().trim());
                i.putExtra("inputDestino", inputDestino.getSelectedItem().toString().trim());
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