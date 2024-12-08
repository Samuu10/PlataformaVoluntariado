package com.example.plataformavoluntariado.Gestion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plataformavoluntariado.Activities.MainActivity;
import com.example.plataformavoluntariado.R;
import com.example.plataformavoluntariado.Almacenamiento.PreferencesManager;
import java.util.List;

public class AdaptadorAnuncio extends RecyclerView.Adapter<AdaptadorAnuncio.AnuncioViewHolder> {

    private List<Anuncio> anuncios;
    private PreferencesManager preferencesManager;

    public AdaptadorAnuncio(List<Anuncio> anuncios, PreferencesManager preferencesManager) {
        this.anuncios = anuncios;
        this.preferencesManager = preferencesManager;
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
        holder.fecha.setText("- " + anuncio.getFecha());
        holder.checkboxApuntado.setOnCheckedChangeListener(null);
        holder.checkboxApuntado.setChecked(anuncio.getChecked());

        //Establecemos la imagen del item según el tipo de anuncio
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

        holder.checkboxApuntado.setOnCheckedChangeListener((buttonView, isChecked) -> {
            anuncio.setChecked(isChecked);
            preferencesManager.loadAnuncios(loadedAnuncios -> {
                for (Anuncio a : loadedAnuncios) {
                    if (a.equals(anuncio)) {
                        a.setChecked(anuncio.getChecked());
                        break;
                    }
                }
                preferencesManager.saveAnuncios(loadedAnuncios);
                ((MainActivity) holder.itemView.getContext()).actualizarFragmentos(loadedAnuncios);
            });
        });
    }

    @Override
    public int getItemCount() {
        return anuncios.size();
    }

    public static class AnuncioViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, descripcion, ciudad, fecha;
        ImageView tipoImagen;
        CheckBox checkboxApuntado;

        public AnuncioViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.titulo_anuncio);
            descripcion = itemView.findViewById(R.id.descripcion_anuncio);
            ciudad = itemView.findViewById(R.id.ciudad_anuncio);
            fecha = itemView.findViewById(R.id.fecha_anuncio);
            tipoImagen = itemView.findViewById(R.id.tipo_imagen);
            checkboxApuntado = itemView.findViewById(R.id.checkbox_apuntado);
        }
    }

    //Getters & Setters
    public List<Anuncio> getAnuncios() {
        return anuncios;
    }
    public void setAnuncios(List<Anuncio> anuncios) {
        this.anuncios = anuncios;
    }
}