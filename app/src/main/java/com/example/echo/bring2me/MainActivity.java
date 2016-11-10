package com.example.echo.bring2me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.echo.bring2me.BD_e_Controle.SQLiteHandler;
import com.example.echo.bring2me.ControleDeSessao.LoginActivity;
import com.example.echo.bring2me.ControleDeSessao.SessionManager;
import com.example.echo.bring2me.MinhasViagensCadastradas.CadastroViagem;
import com.example.echo.bring2me.MinhasViagensCadastradas.MinhasViagensCadastradasActivity;
import com.example.echo.bring2me.MostraViajensDeOutrosViajantes.BuscaViagensActivity;
import com.example.echo.bring2me.PedidosRecebidos.PedidosRecebidosActivity;

import java.util.HashMap;

public class MainActivity extends Activity {

    private TextView txtName;
    private TextView txtEmail;
    private Button btnLogout;

    private TabHost tabHost;

    private Button btnRegViagem;
    private Button btnProcViagem;
    private Button btnPedido;
    private Button btnMinhasViagens;
    private Button btnPedidosRecebidos;

    private SQLiteHandler db;
    private SessionManager session;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        instanciarTabHost();
        instanciarIniciar();
        instanciarAtividades();

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");

        // Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    private void instanciarTabHost(){
        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        //adicionando tabs no tabHost (é necessário fazer isso mesmo com as tabs no xml)
        TabHost.TabSpec ts = tabHost.newTabSpec("tag1");
        ts.setContent(R.id.Perfil);
        ts.setIndicator("Perfil");
        tabHost.addTab(ts);

        ts = tabHost.newTabSpec("tag2");
        ts.setContent(R.id.Iniciar);
        ts.setIndicator("Iniciar");
        tabHost.addTab(ts);

        ts = tabHost.newTabSpec("tag3");
        ts.setContent(R.id.Atividades);
        ts.setIndicator("Atividades");
        tabHost.addTab(ts);

        //setar a tab Iniciar (a segunda tab) como a tab inicial do
        tabHost.setCurrentTab(1);
    }

    private void instanciarIniciar(){
        btnRegViagem = (Button)findViewById(R.id.btnRegViagem);
        btnProcViagem = (Button)findViewById(R.id.btnProcViagem);

        btnRegViagem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Construct the Intent you want to end up at
                Intent intent = new Intent(MainActivity.this, CadastroViagem.class);
                startActivity(intent);
                //finish();
            }
        });

        btnProcViagem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BuscaViagensActivity.class);
                startActivity(intent);
                //finish();
            }
        });
    }
    private void instanciarAtividades(){
        btnMinhasViagens = (Button)findViewById(R.id.btnMinhasViagens);

        btnMinhasViagens.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Launching the login activity
                Intent intent = new Intent(MainActivity.this, MinhasViagensCadastradasActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        btnPedidosRecebidos = (Button)findViewById(R.id.btnPedidosRecebidos);

        btnPedidosRecebidos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Launching the login activity
                Intent intent = new Intent(MainActivity.this, PedidosRecebidosActivity.class);
                startActivity(intent);
                //finish();
            }
        });
    }


    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}