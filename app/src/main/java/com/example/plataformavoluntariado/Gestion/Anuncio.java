package com.example.plataformavoluntariado.Gestion;

import android.os.Parcel;
import android.os.Parcelable;

public class Anuncio implements Parcelable {

    private String id;
    private String titulo;
    private String descripcion;
    private String tipo;
    private String ciudad;
    private String fecha;
    private boolean checked;

    public Anuncio() {}

    protected Anuncio(Parcel in) {
        id = in.readString();
        titulo = in.readString();
        descripcion = in.readString();
        tipo = in.readString();
        ciudad = in.readString();
        fecha = in.readString();
        checked = in.readByte() != 0;
    }

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

    // Getters & Setters
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


    @Override
    public int describeContents() {
        return 0;
    }

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
}