package com.example.plataformavoluntariado.Gestion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plataformavoluntariado.R;
import com.example.plataformavoluntariado.Almacenamiento.PreferencesManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        Anuncio anuncio = anuncios.get(position);
        holder.titulo.setText(anuncio.getTitulo());
        holder.descripcion.setText(anuncio.getDescripcion());
        holder.ciudad.setText("- " + anuncio.getCiudad());
        holder.fecha.setText("- " + anuncio.getFecha());

        holder.checkboxApuntado.setOnCheckedChangeListener(null);
        holder.checkboxApuntado.setChecked(anuncio.getChecked());
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
                listener.onItemClick(anuncio, isChecked);
                if (anuncio.getId() != null) {
                    updateFirebase(anuncio);
                }
            });
        });

        int imageResource = getImageResource(anuncio.getTipo());
        holder.tipoImagen.setImageResource(imageResource);
    }

    private void updateFirebase(Anuncio anuncio) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("anuncios").child(anuncio.getId());
        databaseReference.child("checked").setValue(anuncio.getChecked());
    }

    private int getImageResource(String tipo) {
        switch (tipo) {
            case "educativo":
                return R.drawable.logo_educativo;
            case "social":
                return R.drawable.logo_social;
            case "medioambiental":
                return R.drawable.logo_medioambiental;
            case "cultural":
                return R.drawable.logo_cultural;
            case "sanitario":
                return R.drawable.logo_sanitario;
            default:
                return R.drawable.default_logo;
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