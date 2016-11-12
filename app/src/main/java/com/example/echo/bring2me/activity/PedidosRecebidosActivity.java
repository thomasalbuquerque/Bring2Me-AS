package com.example.echo.bring2me.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.echo.bring2me.model.Pedido;
import com.example.echo.bring2me.adapter.PedidosRecebidosListAdapter;
import com.example.echo.bring2me.R;
import com.example.echo.bring2me.data.RequestSender;
import com.example.echo.bring2me.data.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by thomas on 06/11/16.
 */
public class PedidosRecebidosActivity extends Activity{
    // Log tag
    private static final String TAG = PedidosRecebidosActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private List<Pedido> viagemListPedidosRecebidos = new ArrayList<Pedido>();
    private ListView listViewPedidosRecebidos;
    private PedidosRecebidosListAdapter adapter;
    private SQLiteHandler db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_pedidos_recebidos);

        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();


        listViewPedidosRecebidos = (ListView) findViewById(R.id.pedidosRecebidosListView);
        adapter = new PedidosRecebidosListAdapter(this, viagemListPedidosRecebidos);
        listViewPedidosRecebidos.setAdapter(adapter);

        // Showing progress dialog before making http request
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        final String userViagemID = user.get("uid");
        buscarPedidosRecebidos(userViagemID);

    }

    public void buscarPedidosRecebidos(final String userID){
        // Creating volley request obj
        StringRequest pedidoRecebidoReq = new StringRequest(Request.Method.POST, URLRequests.URL_PEDIDOSRecebidos,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        hidePDialog();
                        try {
                            JSONObject res = new JSONObject(response);
                            // Parsing json

                            for (int i = 0; i < res.length()-1; i++) {


                                JSONObject array = new JSONObject(response);
                                JSONObject obj = (JSONObject) array.get(Integer.toString(i));
                                Pedido pedido = new Pedido();

                                pedido.setNomePedido(obj.getString("nome_produto"));
                                pedido.setValorPedido(obj.getString("valor"));          //POSSIVEL PROBLEMA: NO SQL valor ESTÁ COMO FLOAT
                                Log.d(TAG,"valor do pedido vindo do banco"+pedido.getValorPedido());
                                pedido.setLinkPedido(obj.getString("link"));
                                Log.d(TAG,"link do pedido vindo do banco"+pedido.getLinkPedido());
                                pedido.setEmailUsuarioPedidoi(obj.getString("email_usuario"));
                                Log.d(TAG,"email do usuario vindo do banco"+pedido.getEmailUsuarioPedido());
                                pedido.setIdPedido(obj.getInt("id_pedido"));
                                pedido.setEmpacotadoPedido(obj.getInt("empacotado"));
                                pedido.setCorreioOuPessoalPedido(obj.getInt("entrega"));
                                pedido.setEnderecoPedido(obj.getString("Adress"));
                                pedido.setIdViagem(obj.getString("id_viagem"));
                                /*if(pedido.getIdViagem() != userID){
                                    Log.d(TAG, "userID Viagem meu é diferente do userID viagem da tabela Pedidos do banco");
                                    Log.d(TAG, "userID Viagem meu: " + userID + ", userID da tabela: " + pedido.getIdViagem());
                                }*/

                                // adding viagem to viagens array
                                viagemListPedidosRecebidos.add(pedido);
                            }

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        }){

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to activity_login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("viagem", userID);

                return params;
            }

        };

        // Adding request to request queue
        RequestSender.getInstance().addToRequestQueue(pedidoRecebidoReq);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }


/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
*/
}
