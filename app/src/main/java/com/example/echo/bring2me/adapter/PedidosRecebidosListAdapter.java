package com.example.echo.bring2me.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.echo.bring2me.R;
import com.example.echo.bring2me.activity.DetalhesPedidoRecebidoActivity;
import com.example.echo.bring2me.activity.PedidosRecebidosActivity;
import com.example.echo.bring2me.data.SQLiteHandler;
import com.example.echo.bring2me.model.Pedido;

import java.util.HashMap;
import java.util.List;

/**
 * Created by thomas on 06/11/16.
 */
public class PedidosRecebidosListAdapter extends BaseAdapter{
    private SQLiteHandler db;
    private Activity activity;
    private LayoutInflater inflater;
    private List<Pedido> pedidoItens;
    private static final String TAG = PedidosRecebidosActivity.class.getSimpleName();
                                                                                        //TIREI AQUELE IMAGE LOADER (PODE DAR BUG?)
    public PedidosRecebidosListAdapter(Activity activity, List<Pedido> pedidoItens) {
        this.activity = activity;
        this.pedidoItens = pedidoItens;
        this.inflater = ( LayoutInflater )activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return pedidoItens.size();
    }

    @Override
    public Object getItem(int location) {
        return pedidoItens.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        db = new SQLiteHandler(activity.getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();
        final String userViagemID = user.get("uid");

        View rowView = inflater.inflate(R.layout.list_row_pedidos_recebidos,null);

        TextView nomePedidoTV = (TextView) rowView.findViewById(R.id.nomePedidoRecebido);
        TextView valorPedidoTV = (TextView) rowView.findViewById(R.id.valorPedidoRecebido);
        TextView empacotadoPedidoTV = (TextView) rowView.findViewById(R.id.empacotadoPedidoRecebido);
        TextView pessoalOuCorreioPedidoTV = (TextView) rowView.findViewById(R.id.correioOuPessoalPedidoRecebido);

        Button botaoDetalhesDoPedido = (Button) rowView.findViewById(R.id.btn_detalhesPedidoRecebido);

        // getting movie data for the row
        final Pedido p = pedidoItens.get(position);

        botaoDetalhesDoPedido.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(activity.getApplicationContext(), DetalhesPedidoRecebidoActivity.class);
                i.putExtra("id_viagem", ""+p.getIdViagem());
                i.putExtra("nomeProdutoPedido",p.getNomePedido());

                i.putExtra("valorProdutoPedido", ""+p.getValorPedido());
                Log.d(TAG,"valor do pedido Adapter: " + p.getValorPedido());

                i.putExtra("linkProdutoPedido",""+p.getLinkPedido());
                Log.d(TAG,"link do pedido Adapter: " + p.getLinkPedido());

                i.putExtra("emailClienteProdutoPedido",""+p.getEmailUsuarioPedido());
                Log.d(TAG,"email do pedido Adapter: " + p.getEmailUsuarioPedido());

                i.putExtra("id_pedido",p.getIdPedido());
                i.putExtra("empacotadoProdutoPedido",p.getEmpacotadoPedido());
                i.putExtra("entregaProdutoPedido",p.getCorreioOuPessoalPedido());
                i.putExtra("AdressProdutoPedido",p.getEnderecoPedido());

                activity.startActivity(i);
                activity.finish();
            }
        });

        if(p != null) {

            // nome do produto
            nomePedidoTV.setText("Produto: " + p.getNomePedido());

            // valor do produto
            valorPedidoTV.setText("Valor: " + p.getValorPedido());

            // produto Empacotado
            if(p.getEmpacotadoPedido() == 1){
                empacotadoPedidoTV.setText("Empacotado: Sim");
            }
            else {
                empacotadoPedidoTV.setText("Empacotado: Não");
            }

            // entrega pelo correio ou pessoal
            if (p.getCorreioOuPessoalPedido() == 0) {
                pessoalOuCorreioPedidoTV.setText("Entrega: Via correio");
            }
            else{
                pessoalOuCorreioPedidoTV.setText("Entrega: Pessoalmente");
            }

            //referenciar a TextView pedidoReferenteAViagem


        }
        return rowView;
    }
}
