package com.example.echo.bring2me.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.echo.bring2me.R;

public class ConfiguracoesActivity extends Activity{

    private Button btnEditaPerfil;
    private Button btnMudarSenha;
    private Button btnRemoveConta;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuracoes);

        instanciarIniciar();
    }
    private void instanciarIniciar(){
        btnRemoveConta= (Button)findViewById(R.id.btnDeletarConta);
        btnEditaPerfil = (Button)findViewById(R.id.btnEditarPerfil);
        btnMudarSenha = (Button)findViewById(R.id.btnMudarSenha);



        btnRemoveConta.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Construct the Intent you want to end up at
                Intent intent = new Intent(ConfiguracoesActivity.this, RemoveContaActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        btnMudarSenha.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Construct the Intent you want to end up at
                Intent intent = new Intent(ConfiguracoesActivity.this, mudar_senha.class);
                startActivity(intent);
                //finish();
            }
        });

        btnEditaPerfil.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Construct the Intent you want to end up at
                Intent intent = new Intent(ConfiguracoesActivity.this, editarPerfil.class);
                startActivity(intent);
                //finish();
            }
        });
    }

}
