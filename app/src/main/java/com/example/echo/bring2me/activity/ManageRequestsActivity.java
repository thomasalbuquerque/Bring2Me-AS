package com.example.echo.bring2me.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.echo.bring2me.R;

public class ManageRequestsActivity extends AppCompatActivity {

    Button btnFeitos, btnRecebidos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_requests);

        btnFeitos = (Button)findViewById(R.id.btnPedidosFeitos);
        btnRecebidos = (Button)findViewById(R.id.btnPedidosRecebidos);

        btnFeitos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(ManageRequestsActivity.this, <nome_da_classe>.class);
                //startActivity(intent);
            }
        });

        btnRecebidos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(ManageRequestsActivity.this, <nome_da_classe>.class);
                //startActivity(intent);
            }
        });
    }
}
