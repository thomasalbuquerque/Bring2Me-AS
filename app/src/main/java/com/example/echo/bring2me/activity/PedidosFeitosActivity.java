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
import com.example.echo.bring2me.URLRequests;
import com.example.echo.bring2me.model.Pedido;
import com.example.echo.bring2me.adapter.PedidosFeitosListAdapter;
import com.example.echo.bring2me.R;
import com.example.echo.bring2me.data.RequestSender;
import com.example.echo.bring2me.data.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PedidosFeitosActivity extends Activity{
    // Log tag
    private static final String TAG = PedidosFeitosActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private List<Pedido> viagemListPedidosFeitos = new ArrayList<Pedido>();
    private ListView listViewPedidosFeitos;
    private PedidosFeitosListAdapter adapter;
    private SQLiteHandler db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_pedidos_feitos);

        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();


        listViewPedidosFeitos = (ListView) findViewById(R.id.pedidosFeitosListView);
        adapter = new PedidosFeitosListAdapter(this, viagemListPedidosFeitos);
        listViewPedidosFeitos.setAdapter(adapter);

        // Showing progress dialog before making http request
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        final String userEmail = user.get("email");
        buscarPedidosFeitos(userEmail);

    }

    public void buscarPedidosFeitos(final String userID){
        // Creating volley request obj
        StringRequest pedidoFeitoReq = new StringRequest(Request.Method.POST, URLRequests.URL_PEDIDOSFeitos,
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
                                pedido.setValorPedido( obj.getString("valor"));//POSSIVEL PROBLEMA: NO SQL valor ESTÁ COMO FLOAT
                                pedido.setLinkPedido(obj.getString("link"));
                                pedido.setEmailUsuarioPedidoi(userID);
                                pedido.setIdPedido(obj.getInt("id_pedido"));
                                pedido.setEmailUsuarioPedidoi(obj.getString("email_usuario"));
                                pedido.setEmpacotadoPedido(obj.getInt("empacotado"));
                                pedido.setCorreioOuPessoalPedido(obj.getInt("entrega"));
                                pedido.setEnderecoPedido(obj.getString("adress"));
                                pedido.setIdViagem(obj.getString("id_viagem"));
                                pedido.setAvaliado(obj.getInt("avaliado"));
                                pedido.setAceito(obj.getInt("aceito"));
                                if(pedido.getIdViagem() != userID){
                                    Log.d(TAG, "userID Viagem meu é diferente do userID viagem da tabela Pedidos do banco");
                                }

                                // adding viagem to viagens array
                                viagemListPedidosFeitos.add(pedido);
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
                params.put("user_id", userID);

                return params;
            }

        };

        // Adding request to request queue
        RequestSender.getInstance().addToRequestQueue(pedidoFeitoReq);

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
