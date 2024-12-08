package com.example.plataformavoluntariado.Gestion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.plataformavoluntariado.R;
import java.util.List;

public class AdaptadorAnuncio extends RecyclerView.Adapter<AdaptadorAnuncio.AnuncioViewHolder> {

    private List<Anuncio> anuncios;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Anuncio anuncio);
    }

    public AdaptadorAnuncio(List<Anuncio> anuncios, OnItemClickListener listener) {
        this.anuncios = anuncios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AnuncioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anuncio, parent, false);
        return new AnuncioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnuncioViewHolder holder, int position) {
        Anuncio anuncio = anuncios.get(position);
        holder.titulo.setText(anuncio.getTitulo());
        holder.descripcion.setText(anuncio.getDescripcion());
        holder.ciudad.setText("- " + anuncio.getCiudad());
        holder.fecha.setText("- " +anuncio.getFecha());

        // Set image based on the type of voluntariado
        switch (anuncio.getTipo()) {
            case "educativo":
                holder.tipoImagen.setImageResource(R.drawable.logo_educativo);
                break;
            case "social":
                holder.tipoImagen.setImageResource(R.drawable.logo_social);
                break;
            case "medioambiental":
                holder.tipoImagen.setImageResource(R.drawable.logo_medioambiental);
                break;
            case "cultural":
                holder.tipoImagen.setImageResource(R.drawable.logo_cultural);
                break;
            case "sanitario":
                holder.tipoImagen.setImageResource(R.drawable.logo_sanitario);
                break;
            default:
                break;
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(anuncio));
    }

    @Override
    public int getItemCount() {
        return anuncios != null ? anuncios.size() : 0;
    }

    public static class AnuncioViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, descripcion, ciudad, fecha;
        ImageView tipoImagen;

        public AnuncioViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.titulo_anuncio);
            descripcion = itemView.findViewById(R.id.descripcion_anuncio);
            ciudad = itemView.findViewById(R.id.ciudad_anuncio);
            fecha = itemView.findViewById(R.id.fecha_anuncio);
            tipoImagen = itemView.findViewById(R.id.tipo_imagen);
        }
    }
}