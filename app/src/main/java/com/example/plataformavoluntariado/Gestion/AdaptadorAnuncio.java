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
import java.util.List;

//Adaptador para la lista de anuncios
public class AdaptadorAnuncio extends RecyclerView.Adapter<AdaptadorAnuncio.AnuncioViewHolder> {

    //Variables
    private List<Anuncio> anuncios;
    private OnItemClickListener listener;

    //Constructor
    public AdaptadorAnuncio(List<Anuncio> anuncios, OnItemClickListener listener) {
        this.anuncios = anuncios;
        this.listener = listener;
    }

    //Interfaz para gestionar los clicks en los checkbox de los anuncios
    public interface OnItemClickListener {
        void onItemClick(int posicion, boolean isChecked);
    }

    //Metodo para crear la vista de los anuncios
    @NonNull
    @Override
    public AnuncioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anuncio, parent, false);
        return new AnuncioViewHolder(view);
    }

    //Metodo para asignar los valores de los campo de los anuncios a los elementos de la vista
    @Override
    public void onBindViewHolder(@NonNull AnuncioViewHolder holder, int position) {
        Anuncio anuncio = anuncios.get(position);
        holder.titulo.setText(anuncio.getTitulo());
        holder.descripcion.setText(anuncio.getDescripcion());
        holder.ciudad.setText("- " + anuncio.getCiudad());
        holder.fecha.setText("- " + anuncio.getFecha());
        holder.checkboxApuntado.setOnCheckedChangeListener(null); // Remove previous listener
        holder.checkboxApuntado.setChecked(anuncio.getChecked());
        holder.checkboxApuntado.setOnCheckedChangeListener((buttonView, isChecked) -> listener.onItemClick(position, isChecked));
        int imageResource = getImageResource(anuncio.getTipo());
        holder.tipoImagen.setImageResource(imageResource);
    }

    //Metodo para asignar la imagen correspondiente a cada tipo de anuncio
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

    //Metodo para obtener el numero de anuncios
    @Override
    public int getItemCount() {
        return anuncios.size();
    }

    //Clase para gestionar los elementos de la vista de los anuncios
    public static class AnuncioViewHolder extends RecyclerView.ViewHolder {

        //Variables
        TextView titulo, descripcion, ciudad, fecha;
        ImageView tipoImagen;
        CheckBox checkboxApuntado;

        //Constructor
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