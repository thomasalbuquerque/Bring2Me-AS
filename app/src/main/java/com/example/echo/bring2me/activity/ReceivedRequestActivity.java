package com.example.echo.bring2me.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.echo.bring2me.R;

public class ReceivedRequestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_request);
        TextView txtNome, txtValor, txtLink, txtEmail, txtEndereco, txtStatus;
        Button btnAceitar, btnRecusar;

        txtNome = (TextView)findViewById(R.id.txtNomeProd);
        txtValor = (TextView)findViewById(R.id.txtValor);
        txtLink = (TextView)findViewById(R.id.txtLink);
        txtEmail = (TextView)findViewById(R.id.txtEmail);
        txtEndereco = (TextView)findViewById(R.id.txtEndereco);
        txtStatus = (TextView)findViewById(R.id.txtStatus);
        btnAceitar = (Button)findViewById(R.id.btnAceitar);
        btnRecusar = (Button)findViewById(R.id.btnRecusar);

        /*Empacotado
        swtEmpacotado.setChecked(true); se empacotado = 1
        swtEmpacotado.setChecked(false); se empacotado = 0*/

        /*btnAceitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceivedRequestActivity.this, <nome_da_classe>.class);
                startActivity(intent);
                //finish();
            }
        });*/

        /*btnRecusar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceivedRequestActivity.this, <nome_da_classe>.class);
                startActivity(intent);
                //finish();
            }
        });*/

    }
}
