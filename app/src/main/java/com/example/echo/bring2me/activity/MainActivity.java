package com.example.echo.bring2me.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.echo.bring2me.Config;
import com.example.echo.bring2me.R;
import com.example.echo.bring2me.data.SQLiteHandler;
import com.example.echo.bring2me.SessionManager;
import com.example.echo.bring2me.util.NotificationUtil;
import com.google.firebase.messaging.FirebaseMessaging;

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
    private Button btnPedidosFeitos;

    private SQLiteHandler db;
    private SessionManager session;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);


                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();


                }
            }
        };

        Log.e(TAG, "Firebase reg id: " + regId);
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
                Intent intent = new Intent(MainActivity.this, CadastroViagemActivity.class);
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
                // Launching the activity_login activity
                Intent intent = new Intent(MainActivity.this, ViagensCadastradasActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        btnPedidosRecebidos = (Button)findViewById(R.id.btnPedidosRecebidos);

        btnPedidosRecebidos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Launching the activity_login activity
                Intent intent = new Intent(MainActivity.this, PedidosRecebidosActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        btnPedidosFeitos = (Button)findViewById(R.id.btnPedidosFeitos);

        btnPedidosFeitos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Launching the activity_login activity
                Intent intent = new Intent(MainActivity.this, PedidosFeitosActivity.class);
                startActivity(intent);
                //finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtil.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the activity_login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}