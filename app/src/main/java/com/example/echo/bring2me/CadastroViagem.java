package com.example.echo.bring2me;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.util.ArraySet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class CadastroViagens extends AppCompatActivity {
    private ArrayList paises = new ArrayList();
    private Spinner spPaisesOri;
    private Spinner spPaisesDes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_viagens);

        paises.add("Brasil");
        paises.add("Estados Unidos");
        paises.add("Franças");
        paises.add("Itália");
        paises.add("Canadá");
        paises.add("Japão");
        paises.add("Canadá");

        spPaisesOri = (Spinner) findViewById(R.id.spOrigem);
        spPaisesDes = (Spinner) findViewById(R.id.spDestino);
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this,R.layout.activity_cadastro_viagens, paises);
        spPaisesOri.setAdapter(arrayAdapter1);
        spPaisesDes.setAdapter(arrayAdapter1);




    }
}
