package com.example.plataformavoluntariado.Gestion;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.plataformavoluntariado.R;

//Clase que representa un anuncio
public class Anuncio implements Parcelable {

    //Atributos
    private String id;
    private String titulo;
    private String descripcion;
    private String tipo;
    private String ciudad;
    private String fecha;
    private boolean checked;

    //Constructor sin parametros
    public Anuncio() {}

    //Constructor con parametros
    protected Anuncio(Parcel in) {
        id = in.readString();
        titulo = in.readString();
        descripcion = in.readString();
        tipo = in.readString();
        ciudad = in.readString();
        fecha = in.readString();
        checked = in.readByte() != 0;
    }

    //Metodo para crear un anuncio a partir de un parcel
    public static final Creator<Anuncio> CREATOR = new Creator<Anuncio>() {
        @Override
        public Anuncio createFromParcel(Parcel in) {
            return new Anuncio(in);
        }

        @Override
        public Anuncio[] newArray(int size) {
            return new Anuncio[size];
        }
    };

    //Getters & Setters
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getCiudad() {
        return ciudad;
    }
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    public boolean getChecked() {
        return checked;
    }
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    //Metodo describeContents para Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    //Metodo writeToParcel para Parcelable
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(titulo);
        dest.writeString(descripcion);
        dest.writeString(tipo);
        dest.writeString(ciudad);
        dest.writeString(fecha);
        dest.writeByte((byte) (checked ? 1 : 0));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Anuncio anuncio = (Anuncio) obj;
        // Comparar por ID u otro atributo único
        return id != null && id.equals(anuncio.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public int getTipoImagenResId() {
        // Devuelve el recurso de la imagen basado en el tipo de voluntariado
        switch (tipo.toLowerCase()) {
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
}