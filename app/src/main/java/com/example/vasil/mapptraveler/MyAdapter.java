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
 * Created by Kaytsak on 04/01/2018.
 */

public class MyAdapter extends ArrayAdapter<String> {

    String [] locais;
    int [] images;
    Context c;

    public MyAdapter(@NonNull Context context, String[] nomesLocais, int[] imagens) {
        super(context, R.layout.listview_item);
        this.locais = nomesLocais;
        this.images = imagens;
        this.c = context;
    }

    @Override
    public int getCount() {
        return locais.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder vh = new ViewHolder();
        if(convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.listview_item, parent, false);
            vh.imagem = (ImageView) convertView.findViewById(R.id.imageView);
            vh.nomeLocal = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.imagem.setImageResource(images[position]);
        vh.nomeLocal.setText(locais[position]);

        return convertView;
    }

    static class ViewHolder{
        ImageView imagem;
        TextView nomeLocal;
    }

}
