package com.example.echo.bring2me.activity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.echo.bring2me.R;
import com.example.echo.bring2me.URLRequests;
import com.example.echo.bring2me.data.RequestSender;
import com.example.echo.bring2me.data.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class DetalhesPedidosFeitosActivity extends Activity{
    // Log tag
    private static final String TAG = DetalhesPedidosFeitosActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private String id_viagem;
    private String id_pedido;
    private Button btn_Cancelar;

    private AlertDialog alerta;

    private int avaliado;
    private int aceito;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalhes_do_pedido_feito);

        TextView nomeProdutoPedido = (TextView) findViewById(R.id.detalhesNomePedidoFeito);
        TextView valorProdutoPedido = (TextView) findViewById(R.id.detalhesValorPedidoFeito);
        TextView linkProdutoPedido = (TextView) findViewById(R.id.detalhesLinkPedidoFeito);
        TextView empacotadoProdutoPedido = (TextView) findViewById(R.id.detalhesEmpacotadoPedidoFeito);
        TextView entregaProdutoPedido = (TextView) findViewById(R.id.detalhesCorreioOuPessoalPedidoFeito);
        TextView AdressProdutoPedido = (TextView) findViewById(R.id.detalhesEnderecoPedidoFeito);
        TextView AvaliadoAceitoPedido = (TextView) findViewById(R.id.avaliadoaceitoPedidoFeito);
        TextView textoPergunta = (TextView) findViewById(R.id.textoPergunta);

        final Bundle extras = getIntent().getExtras();
/*
        i.putExtra("id_viagem", p.getIdViagem());
        i.putExtra("nomeProdutoPedido",p.getNomePedido());
        i.putExtra("valorProdutoPedido", p.getValorPedido());
        i.putExtra("linkProdutoPedido",p.getLinkPedido());
        i.putExtra("emailClienteProdutoPedido",p.getEmailUsuarioPedido());
        i.putExtra("id_pedido",p.getIdPedido());
        i.putExtra("empacotadoProdutoPedido",p.getEmpacotadoPedido());
        i.putExtra("entregaProdutoPedido",p.getCorreioOuPessoalPedido());
        i.putExtra("AdressProdutoPedido",p.getEnderecoPedido());
*/
        btn_Cancelar = (Button) findViewById(R.id.btnCancelar);

        if(extras != null){
            nomeProdutoPedido.setText("Produto: " + extras.getString("nomeProdutoPedido"));
            valorProdutoPedido.setText("Valor: " + extras.getString("valorProdutoPedido"));
            id_pedido = Integer.toString(extras.getInt("id_pedido"));
            if(extras.getString("linkProdutoPedido") == null){
                linkProdutoPedido.setText("Link do produto: ");
            }
            else linkProdutoPedido.setText("Link do produto: " + extras.getString("linkProdutoPedido"));

            // produto Empacotado
            if(extras.getInt("empacotadoProdutoPedido") == 1){
                empacotadoProdutoPedido.setText("Empacotado: " + "Sim");
            }
            else {
                empacotadoProdutoPedido.setText("Empacotado: " + "Não");
            }

            if(extras.getInt("avaliado") == 1 && extras.getInt("aceito") == 1){
                AvaliadoAceitoPedido.setText("Seu pedido foi avaliado e ACEITO pelo viajante");
                textoPergunta.setText("Este pedido foi aceito. Deseja cancelá-lo?");
                btn_Cancelar.setText("Cancelar");
            }
            else {
                if(extras.getInt("avaliado") == 1 && extras.getInt("aceito") == 0){
                    AvaliadoAceitoPedido.setText("Seu pedido foi avaliado, mas NÃO ACEITO pelo viajante");
                    textoPergunta.setText("Este pedido foi recusado. Deseja apagá-lo?");
                    btn_Cancelar.setText("Apagar");
                }
                else {
                    AvaliadoAceitoPedido.setText("Seu Pedido Ainda Não Foi Avaliado");
                    textoPergunta.setText("Deseja cancelar este pedido?");
                    btn_Cancelar.setText("Cancelar");
                }
            }

            if(extras.getInt("empacotadoProdutoPedido") == 1){
                empacotadoProdutoPedido.setText("Empacotado: " + "Sim");
            }
            else {
                empacotadoProdutoPedido.setText("Empacotado: " + "Não");
            }

            // entrega pelo correio ou pessoal
            if (extras.getInt("entregaProdutoPedido") == 0) {
                entregaProdutoPedido.setText("Entrega: Via correio");
                AdressProdutoPedido.setText("Endereço de entrega: " + extras.getString("AdressProdutoPedido"));
            }
            else{
                entregaProdutoPedido.setText("Entrega: Pessoalmente");
            }

            //##########################   faltou também referenciar a TextView detalhesPedidoReferenteAViagem      #########################
        }
        else
            Log.d(TAG, "erro no extra");

        pDialog = new ProgressDialog(this);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();

        final String email = user.get("email");

        /*// Check for empty data in the form
        if (!userIDfromAnterior.isEmpty() && !paisAtual.isEmpty() && !paisDestino.isEmpty()) {
            // busca viagem
            buscar(origem, destino);
        } else {
            // diga para digitar origem e destino
            Toast.makeText(getApplicationContext(),
                    "Insira sua origem e destino!", Toast.LENGTH_LONG).show();
        }*/

        btn_Cancelar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                pDialog.setMessage("Loading...");
                pDialog.show();
                Alerta(id_pedido, email);

            }
        });

    }  //fim do método onCreate()

    private void removePedido(final String id_pedido, final String email){
        StringRequest pedidoReq = new StringRequest(Request.Method.POST, URLRequests.URL_REMOVEPEDIDOCadastrado,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        hidePDialog();
                        try {
                            JSONObject res = new JSONObject(response);

                            boolean error = res.getBoolean("error");
                            if (!error) {
                                Toast.makeText(getApplicationContext(), "Pedido removido com sucesso.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else    // Error in deleting. Get the error message
                            {
                                String errorMsg = res.getString("error_msg");
                                Toast.makeText(getApplicationContext(),
                                        errorMsg, Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to activity_login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_pedido", id_pedido);
                params.put("email", email);
                return params;
            }

        };
        // Adding request to request queue
        RequestSender.getInstance().addToRequestQueue(pedidoReq);
    }

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

    private void Alerta(final String id_pedido, final String email) {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Remover pedido");
        //define a mensagem

        builder.setMessage("Você quer mesmo remover este pedido?");

        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //Toast.makeText(DetalhesPedidoRecebidoActivity.this, "positivo=" + arg1, Toast.LENGTH_SHORT).show();
                removePedido(id_pedido, email);
            }
        });
        //define um botão como negativo.
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //Toast.makeText(DetalhesPedidoRecebidoActivity.this, "negativo=" + arg1, Toast.LENGTH_SHORT).show();
                pDialog.cancel();
            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }
}
