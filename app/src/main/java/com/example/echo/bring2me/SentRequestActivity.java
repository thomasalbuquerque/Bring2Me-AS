package com.example.echo.bring2me;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;

public class SentRequestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);

        TextView txtNome, txtValor, txtLink, txtEmail, txtEndereco, txtStatus;
        Switch swtEmpacotado;

        txtNome = (TextView)findViewById(R.id.txtNomeProd);
        txtValor = (TextView)findViewById(R.id.txtValor);
        txtLink = (TextView)findViewById(R.id.txtLink);
        txtEmail = (TextView)findViewById(R.id.txtEmail);
        txtEndereco = (TextView)findViewById(R.id.txtEndereco);
        txtStatus = (TextView)findViewById(R.id.txtStatus);
        swtEmpacotado = (Switch)findViewById(R.id.swtEmpacotado);

        /*Empacotado
        swtEmpacotado.setChecked(true); se empacotado = 1
        swtEmpacotado.setChecked(false); se empacotado = 0*/

        /*Status
        o text do txtStatus deve exibir:
        "Entrega" se entrega = 1
        "Avaliado" se avaliado = 1
        "Aceito" se aceito = 1
        Será trocado por algo mais visualmente agradável no futuro
         */
    }
}
