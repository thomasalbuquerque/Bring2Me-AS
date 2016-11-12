package com.example.echo.bring2me.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.echo.bring2me.model.Pedido;
import com.example.echo.bring2me.R;
import com.example.echo.bring2me.data.SQLiteHandler;
import com.example.echo.bring2me.activity.DetalhesPedidosFeitosActivity;

import java.util.HashMap;
import java.util.List;


public class PedidosFeitosListAdapter extends BaseAdapter{
    private SQLiteHandler db;
    private Activity activity;
    private LayoutInflater inflater;
    private List<Pedido> pedidoItens;
                                                                                        //TIREI AQUELE IMAGE LOADER (PODE DAR BUG?)
    public PedidosFeitosListAdapter(Activity activity, List<Pedido> pedidoItens) {
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

        View rowView = inflater.inflate(R.layout.list_row_pedidos_feitos,null);

        TextView nomePedidoTV = (TextView) rowView.findViewById(R.id.nomePedidoFeito);
        TextView valorPedidoTV = (TextView) rowView.findViewById(R.id.valorPedidoFeito);
        TextView empacotadoPedidoTV = (TextView) rowView.findViewById(R.id.empacotadoPedidoFeito);
        TextView pessoalOuCorreioPedidoTV = (TextView) rowView.findViewById(R.id.correioOuPessoalPedidoFeito);

        Button botaoDetalhesDoPedido = (Button) rowView.findViewById(R.id.btn_detalhesPedidoFeito);

        // getting movie data for the row
        final Pedido p = pedidoItens.get(position);

        botaoDetalhesDoPedido.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(activity.getApplicationContext(), DetalhesPedidosFeitosActivity.class);
                i.putExtra("id_viagem", p.getIdViagem());
                i.putExtra("nomeProdutoPedido",p.getNomePedido());
                i.putExtra("valorProdutoPedido", p.getValorPedido());
                i.putExtra("linkProdutoPedido",p.getLinkPedido());
                i.putExtra("emailClienteProdutoPedido",p.getEmailUsuarioPedido());
                i.putExtra("id_pedido",p.getIdPedido());
                i.putExtra("empacotadoProdutoPedido",p.getEmpacotadoPedido());
                i.putExtra("entregaProdutoPedido",p.getCorreioOuPessoalPedido());
                i.putExtra("AdressProdutoPedido",p.getEnderecoPedido());
                i.putExtra("avaliado",p.getAvaliado());
                i.putExtra("aceito",p.getAceito());

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
