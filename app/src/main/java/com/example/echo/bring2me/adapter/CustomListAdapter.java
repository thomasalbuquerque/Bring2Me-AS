package com.example.echo.bring2me.adapter;

/**
 * Created by thomas on 16/09/16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.example.echo.bring2me.AppController;
import com.example.echo.bring2me.activity.OrderActivity;
import com.example.echo.bring2me.R;
import com.example.echo.bring2me.model.Viagem;

import java.util.List;

public class CustomListAdapter extends BaseAdapter {
        private Activity activity;
        private LayoutInflater inflater;
        private List<Viagem> viagemItems;
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        public CustomListAdapter(Activity activity, List<Viagem> viagemItems) {
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

                View rowView = inflater.inflate(R.layout.list_row,null);

                if (imageLoader == null)
                        imageLoader = AppController.getInstance().getImageLoader();

                ImageButton thumbNail = (ImageButton) rowView.findViewById(R.id.thumbnail);
                TextView origemTV = (TextView) rowView.findViewById(R.id.mostraorigem);
                TextView destinoTV = (TextView) rowView.findViewById(R.id.mostradestino);
                TextView avaliacaoViajanteTV = (TextView) rowView.findViewById(R.id.avaliacaoViajante);
                TextView precoBaseTV = (TextView) rowView.findViewById(R.id.precoBase);

                // getting movie data for the row
                final Viagem v = viagemItems.get(position);
                // thumbnail image
                Bitmap bitmap = BitmapFromURL.getBitmapFromURL(v.getThumbnailUrl());
                thumbNail.setImageBitmap(bitmap);

                thumbNail.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View view) {
                                Intent i = new Intent(activity.getApplicationContext(), OrderActivity.class);
                                i.putExtra("id_viagem", v.getId());
                                activity.startActivity(i);
                                //activity.finish();
                        }
                });

            if(v != null) {

                        // origem
                        origemTV.setText("Origem: " + v.getOrigem());

                        // destino
                        destinoTV.setText("Destino: " + v.getDestino());

                        // avaliacaoViajante
                        avaliacaoViajanteTV.setText("Avaliação do viajante: " + v.getAvaliacaoViajante());

                        // preço base
                        precoBaseTV.setText("R$" + v.getPrecoBase());
                }
                return rowView;
        }

}