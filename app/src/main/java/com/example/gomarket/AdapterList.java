package com.example.gomarket;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterList extends RecyclerView.Adapter<AdapterList.ViewHolder> {
    public List<ListElement> tiendasLista;
    private OnNoteListener gonNoteListener;



    public AdapterList(List<ListElement> tiendasLista, OnNoteListener gonNoteListener) {
        this.tiendasLista = tiendasLista;
        this.gonNoteListener=gonNoteListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //DECLARAR VARIABLES
        private TextView nombre, descripcion;
        ImageView fotoTienda, arrow;
        OnNoteListener onNoteListener;

        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            //DECLRARA ELEMENTOS DEL LAYOUT
            nombre = (TextView) itemView.findViewById(R.id.textName);
            descripcion = (TextView) itemView.findViewById(R.id.textDescription);
            fotoTienda = (ImageView) itemView.findViewById(R.id.ImageTienda);
            arrow = (ImageView) itemView.findViewById(R.id.ImageArrow);

            this.onNoteListener= onNoteListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());

        }
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_elements, parent, false);
        ViewHolder viewHolder=new ViewHolder(view, gonNoteListener);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //INSERTAR DATOS
        holder.nombre.setText(tiendasLista.get(position).getNombre());
        holder.descripcion.setText(tiendasLista.get(position).getDescripcion());
        Bitmap bitmap = BitmapFactory.decodeByteArray(tiendasLista.get(position).getImage(), 0, tiendasLista.get(position).getImage().length);
        holder.fotoTienda.setImageBitmap(bitmap);
        holder.arrow.setImageResource(tiendasLista.get(position).getArrow());
    }

    @Override
    public int getItemCount() {
        return tiendasLista.size();
    }

    public void removeItems() {
        tiendasLista.clear();

    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }



}
