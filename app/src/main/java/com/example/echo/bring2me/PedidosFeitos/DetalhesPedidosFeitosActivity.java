package com.example.echo.bring2me.PedidosFeitos;
import android.app.Activity;
import android.app.ProgressDialog;
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
import com.example.echo.bring2me.BD_e_Controle.AppConfig;
import com.example.echo.bring2me.BD_e_Controle.AppController;
import com.example.echo.bring2me.BD_e_Controle.SQLiteHandler;
import com.example.echo.bring2me.MainActivity;
import com.example.echo.bring2me.R;

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
            }
            else {
                if(extras.getInt("avaliado") == 1 && extras.getInt("aceito") == 0){
                    AvaliadoAceitoPedido.setText("Seu pedido foi avaliado, mas NÃO ACEITO pelo viajante");
                }
                else AvaliadoAceitoPedido.setText("Seu Pedido Ainda Não Foi Avaliado");
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

            //##########################   faltou também referenciar a TextView detalhesPedidoReferenteAViagem      ##########################
        }
        else
            Log.d(TAG, "erro no extra");

        pDialog = new ProgressDialog(this);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        /*// Check for empty data in the form
        if (!userIDfromAnterior.isEmpty() && !paisAtual.isEmpty() && !paisDestino.isEmpty()) {
            // busca viagem
            buscar(origem, destino);
        } else {
            // diga para digitar origem e destino
            Toast.makeText(getApplicationContext(),
                    "Insira sua origem e destino!", Toast.LENGTH_LONG).show();
        }*/

    }  //fim do método onCreate()

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
}
