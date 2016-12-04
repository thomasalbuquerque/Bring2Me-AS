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
import java.util.Objects;

/**
 * Created by thomas on 06/11/16.
 */
public class DetalhesPedidoRecebidoActivity extends Activity{
    // Log tag
    private static final String TAG = DetalhesPedidoRecebidoActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private String id_viagem;
    private String id_pedido;
    private String email;
    private String  pago;
    private String  avaliado;
    private AlertDialog alerta;

    private String aceito;

    private Button btn_Aceitar;
    private Button btn_Recusar;
    private Button btn_Comprei;
    private Button btn_Enviei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalhes_do_pedido_recebido);

        TextView nomeProdutoPedido = (TextView) findViewById(R.id.detalhesNomePedido);
        TextView valorProdutoPedido = (TextView) findViewById(R.id.detalhesValorPedido);
        TextView linkProdutoPedido = (TextView) findViewById(R.id.detalhesLinkPedido);
        TextView emailClienteProdutoPedido = (TextView) findViewById(R.id.detalhesEmailClientePedido);
        TextView empacotadoProdutoPedido = (TextView) findViewById(R.id.detalhesEmpacotadoPedido);
        TextView entregaProdutoPedido = (TextView) findViewById(R.id.detalhesCorreioOuPessoalPedido);
        TextView AdressProdutoPedido = (TextView) findViewById(R.id.detalhesEnderecoPedido);
        TextView txtStatus = (TextView) findViewById(R.id.txtStatus);

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

        if(extras != null){
            nomeProdutoPedido.setText("Produto: " + extras.getString("nomeProdutoPedido"));
            valorProdutoPedido.setText("Valor: " + extras.getString("valorProdutoPedido"));
            linkProdutoPedido.setText("Link do produto: " + extras.getString("linkProdutoPedido"));
            emailClienteProdutoPedido.setText("Email do Cliente: " + extras.getString("emailClienteProdutoPedido"));

            pago = extras.getInt("pago") + "";
            avaliado = extras.getInt("avaliado") + "";

            // produto Empacotado
            if(extras.getInt("empacotadoProdutoPedido") == 1){
                empacotadoProdutoPedido.setText("Empacotado: " + "Sim");
            }
            else {
                empacotadoProdutoPedido.setText("Empacotado: " + "Não");
            }

            // entrega pelo correio ou pessoal
            if (extras.getInt("entregaProdutoPedido") == 0) {
                entregaProdutoPedido.setText("Entrega: Via correio");
                AdressProdutoPedido.setText("Endereço de entrega " + extras.getString("AdressProdutoPedido"));
            }
            else{
                entregaProdutoPedido.setText("Entrega: Pessoalmente");
            }

            //##########################   faltou também referenciar a TextView detalhesPedidoReferenteAViagem      ##########################
        }
        else
            Log.d(TAG, "erro no extra");

        pDialog = new ProgressDialog(this);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        btn_Aceitar = (Button) findViewById(R.id.btnAceitaPedido);
        btn_Recusar = (Button) findViewById(R.id.btnRecusaPedido);
        btn_Comprei = (Button) findViewById(R.id.btn_compraProduto);
        btn_Enviei = (Button) findViewById(R.id.btn_envioProduto);
        Log.d(TAG, "VALOR AVALIADO:" + avaliado);
        if(Objects.equals(avaliado, "0")){ //não avaliado, não pago
            Log.d(TAG, "ENTROU");
            btn_Aceitar.setVisibility(View.VISIBLE);
            btn_Recusar.setVisibility(View.VISIBLE);
        }
        else if(Objects.equals(pago, "1")){ //avaliado, e pago
            btn_Comprei.setVisibility(View.VISIBLE);
        }
        if(Objects.equals(pago, "2")){ //avaliado, pago e comprado
            btn_Enviei.setVisibility(View.VISIBLE);
        }
        else if(Objects.equals(pago, "4")){
            txtStatus.setText("O cliente recebeu o pedido e o dinheiro foi direcionado à sua conta.");
        }

        /*// Check for empty data in the form
        if (!userIDfromAnterior.isEmpty() && !paisAtual.isEmpty() && !paisDestino.isEmpty()) {
            // busca viagem
            buscar(origem, destino);
        } else {
            // diga para digitar origem e destino
            Toast.makeText(getApplicationContext(),
                    "Insira sua origem e destino!", Toast.LENGTH_LONG).show();
        }*/



        btn_Aceitar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                aceito="1";
                pago="0";

                pDialog.setMessage("Loading...");
                pDialog.show();
                final Bundle extrasBotao = getIntent().getExtras();
                if (extrasBotao != null) {
                    id_viagem = extrasBotao.getString("id_viagem");
                    Log.d(TAG,"id_viagem: " + id_viagem);
                    id_pedido = ""+extrasBotao.getInt("id_pedido");
                    Log.d(TAG,"id_pedido: " + id_pedido);
                    email = extrasBotao.getString("email_usuario");
                }
                Alerta(aceito,pago,id_viagem,id_pedido);
                //avaliaNoBanco(aceito,id_viagem,id_pedido);

            }
        });

        btn_Recusar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                aceito="0";
                pago="0";

                pDialog.setMessage("Loading...");
                pDialog.show();
                final Bundle extrasBotao = getIntent().getExtras();
                if (extrasBotao != null) {
                    id_viagem = extrasBotao.getString("id_viagem");
                    Log.d(TAG,"id_viagem: " + id_viagem);
                    id_pedido = ""+extrasBotao.getInt("id_pedido");
                    Log.d(TAG,"id_pedido: " + id_pedido);
                    email = extrasBotao.getString("email_usuario");
                }

                Alerta(aceito,pago,id_viagem,id_pedido);
                //avaliaNoBanco(aceito,id_viagem,id_pedido);

            }
        });

        btn_Comprei.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                pago = "2";
                aceito="1";  //se ja estava pago, entao ele esta aceito (utilizar mesmo codigo)

                pDialog.setMessage("Loading...");
                pDialog.show();
                final Bundle extrasBotao = getIntent().getExtras();
                if (extrasBotao != null) {
                    id_viagem = extrasBotao.getString("id_viagem");
                    Log.d(TAG,"id_viagem: " + id_viagem);
                    id_pedido = ""+extrasBotao.getInt("id_pedido");
                    Log.d(TAG,"id_pedido: " + id_pedido);
                    email = extrasBotao.getString("email_usuario");
                }

                Alerta(aceito,pago,id_viagem,id_pedido);
                //avaliaNoBanco(aceito,id_viagem,id_pedido);

            }
        });

        btn_Enviei.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                pago = "3";
                aceito="1";  //se ja estava pago, entao ele esta aceito (utilizar mesmo codigo)

                pDialog.setMessage("Loading...");
                pDialog.show();
                final Bundle extrasBotao = getIntent().getExtras();
                if (extrasBotao != null) {
                    id_viagem = extrasBotao.getString("id_viagem");
                    Log.d(TAG,"id_viagem: " + id_viagem);
                    id_pedido = ""+extrasBotao.getInt("id_pedido");
                    Log.d(TAG,"id_pedido: " + id_pedido);
                    email = extrasBotao.getString("email_usuario");
                }

                Alerta(aceito,pago,id_viagem,id_pedido);
                //avaliaNoBanco(aceito,id_viagem,id_pedido);

            }
        });

    }  //fim do método onCreate()

    public void avaliaNoBanco(final String avaliacao, final String pago, final String id_viagem, final String id_pedido){
        Log.e(TAG, "Recebido:" + avaliacao + ", " + pago + ", " + id_viagem + ", " + id_pedido);
        StringRequest pedidoReq = new StringRequest(Request.Method.POST, URLRequests.URL_AVALIAPedido,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        hidePDialog();
                        try {
                            JSONObject res = new JSONObject(response);

                            boolean error = res.getBoolean("error");
                            if (!error) {
                                if(avaliacao == "1") {
                                    Toast.makeText(getApplicationContext(), "Pedido aceito com sucesso.", Toast.LENGTH_LONG).show();
                                }
                                else if(avaliacao == "0"){
                                    Toast.makeText(getApplicationContext(), "Pedido recusado com sucesso.", Toast.LENGTH_LONG).show();
                                }
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
                params.put("id_viagem", id_viagem);          //PARAMETROS SERÃO ID VIAGEM E IDE PEDIDO
                params.put("id_pedido", id_pedido);
                params.put("aceito", avaliacao);
                params.put("pago", pago);
                params.put("email", email);
                Log.d(TAG,"aceito parameters: " + ""+avaliacao);
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
    private void Alerta(final String aceito, final String pago, final String id_viagem, final String id_pedido) {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Pedido Recebido");
        //define a mensagem
        Log.d(TAG,"aceito = " + aceito);
        if(aceito == "0") {
            builder.setMessage("Você quer mesmo recusar esse pedido?");
        }
        else if(aceito == "1"){
            builder.setMessage("Tem certeza que quer aceitar esse pedido?");
        }
        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //Toast.makeText(DetalhesPedidoRecebidoActivity.this, "positivo=" + arg1, Toast.LENGTH_SHORT).show();
                avaliaNoBanco(aceito,pago,id_viagem,id_pedido);
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
