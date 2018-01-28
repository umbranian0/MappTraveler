package com.example.vasil.mapptraveler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Kaytsak on 27/01/2018.
 */

public class PopularAdapter extends ArrayAdapter<String> {

    String [] locais;
    int [] images;
    Context c;
    int[] visitas;
    int[] nvis;
    int visitado = R.drawable.visitado;
    int porvisitar = R.drawable.porvisitar;


    public PopularAdapter(@NonNull Context context, String[] nomesLocais, int[] imagens, int[] visita, int[] nVisitas) {
        super(context, R.layout.listview_item2);
        this.locais = nomesLocais;
        this.images = imagens;
        this.c = context;
        this.visitas = visita;
        this.nvis = nVisitas;
    }

    @Override
    public int getCount() {
        return locais.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        VH vholder = new VH();
        if(convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.listview_item2, parent, false);

            vholder.imagem = (ImageView) convertView.findViewById(R.id.imageView4);
            vholder.imagem2 = (ImageView) convertView.findViewById(R.id.imageView5);
            vholder.nomeLocal = (TextView) convertView.findViewById(R.id.textView5);
            vholder.nv = (TextView) convertView.findViewById(R.id.textView6);

            convertView.setTag(vholder);
        } else {
            vholder = (VH) convertView.getTag();
        }

        vholder.imagem.setImageResource(images[position]);

        if (visitas[position]==1)
            vholder.imagem2.setImageResource(visitado);
        else if (visitas[position]==0)
            vholder.imagem2.setImageResource(porvisitar);

        vholder.nomeLocal.setText(locais[position]);

        vholder.nv.setText(" NºVisitas " + nvis[position]);
        return convertView;
    }

    static class VH {
        ImageView imagem;
        TextView nomeLocal;
        ImageView imagem2;
        TextView nv;
    }
}
