package com.example.echo.bring2me.listview.adapter;

/**
 * Created by thomas on 16/09/16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.example.echo.bring2me.AppController;
import com.example.echo.bring2me.R;
import com.example.echo.bring2me.RemoveViagemCadastradaActivity;
import com.example.echo.bring2me.SQLiteHandler;
import com.example.echo.bring2me.listview.model.Viagem;

import java.util.HashMap;
import java.util.List;

public class ViagensCadastradasListAdapter extends BaseAdapter {

    private SQLiteHandler db;
    private Activity activity;
    private LayoutInflater inflater;
    private List<Viagem> viagemItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public ViagensCadastradasListAdapter(Activity activity, List<Viagem> viagemItems) {
        this.activity = activity;
        this.viagemItems = viagemItems;
        this.inflater = ( LayoutInflater )activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return viagemItems.size();
    }

    @Override
    public Object getItem(int location) {
        return viagemItems.get(location);
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
        View rowView = inflater.inflate(R.layout.list_row_viagens_cadastradas,null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        TextView origemTV = (TextView) rowView.findViewById(R.id.mostraorigemDasCadastradas);
        TextView destinoTV = (TextView) rowView.findViewById(R.id.mostradestinoDasCadastradas);
        TextView precoBaseTV = (TextView) rowView.findViewById(R.id.precoBaseDasCadastradas);
        Button botaoViagemCadastrada = (Button) rowView.findViewById(R.id.botaoViagemCadastrada);

        // getting movie data for the row
        final Viagem v = viagemItems.get(position);

        botaoViagemCadastrada.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(activity.getApplicationContext(), RemoveViagemCadastradaActivity.class);
                i.putExtra("user_id", userViagemID);
                i.putExtra("paisAtual", v.getOrigem());
                i.putExtra("paisDestino",v.getDestino());
                activity.startActivity(i);
            }
        });

        if(v != null) {

            // origem
            origemTV.setText("Origem: " + v.getOrigem());

            // destino
            destinoTV.setText("Destino: " + v.getDestino());

            // preço base
            precoBaseTV.setText("R$" + v.getPrecoBase());
        }
        return rowView;
    }

}