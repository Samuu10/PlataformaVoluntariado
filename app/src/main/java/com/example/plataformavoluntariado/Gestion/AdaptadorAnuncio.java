package com.example.plataformavoluntariado.Gestion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.plataformavoluntariado.Activities.MainActivity;
import com.example.plataformavoluntariado.R;
import com.example.plataformavoluntariado.Almacenamiento.PreferencesManager;
import java.util.List;

public class AdaptadorAnuncio extends RecyclerView.Adapter<AdaptadorAnuncio.AnuncioViewHolder> {

    private List<Anuncio> anuncios;
    private OnItemClickListener listener;
    private PreferencesManager preferencesManager;

    public interface OnItemClickListener {
        void onItemClick(Anuncio anuncio, boolean isChecked);
    }

    public AdaptadorAnuncio(List<Anuncio> anuncios, OnItemClickListener listener, PreferencesManager preferencesManager) {
        this.anuncios = anuncios;
        this.listener = listener;
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

        //Establecemos los TextView con los datos del anuncio
        Anuncio anuncio = anuncios.get(position);
        holder.titulo.setText(anuncio.getTitulo());
        holder.descripcion.setText(anuncio.getDescripcion());
        holder.ciudad.setText("- " + anuncio.getCiudad());
        holder.fecha.setText("- " + anuncio.getFecha());

        //Establecemos el estado del CheckBox
        holder.checkboxApuntado.setOnCheckedChangeListener(null);
        holder.checkboxApuntado.setChecked(anuncio.getChecked());
        holder.checkboxApuntado.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                anuncios.add(anuncio);
                Toast.makeText(buttonView.getContext(), "Anuncio marcado", Toast.LENGTH_SHORT).show();
                preferencesManager.saveAnuncios(anuncios);
            } else {
                anuncios.remove(anuncio);
                Toast.makeText(buttonView.getContext(), "Anuncio desmarcado", Toast.LENGTH_SHORT).show();
                preferencesManager.saveAnuncios(anuncios);
            }
            //preferencesManager.saveAnuncios(anuncios);
            listener.onItemClick(anuncio, isChecked);
        });

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
    }

    @Override
    public int getItemCount() {
        return anuncios.size();
    }

    public static class AnuncioViewHolder extends RecyclerView.ViewHolder {

        //Variables
        TextView titulo, descripcion, ciudad, fecha;
        ImageView tipoImagen;
        CheckBox checkboxApuntado;

        public AnuncioViewHolder(@NonNull View itemView) {
            super(itemView);

            //Establecemos las vistas
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