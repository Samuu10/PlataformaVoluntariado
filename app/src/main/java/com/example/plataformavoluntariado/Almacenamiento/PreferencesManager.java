package com.example.plataformavoluntariado.Almacenamiento;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.plataformavoluntariado.Gestion.Anuncio;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

//Clase que gestiona las preferencias de la aplicacion
public class PreferencesManager {

    //Variables
    private static final String PREFS_NAME = "anuncios_pref";
    private static final String KEY_ANUNCIOS_MARCADOS = "anuncios_marcados";
    private SharedPreferences sharedPreferences;
    private static PreferencesManager instance;
    private List<Anuncio> anunciosMarcados;
    private Gson gson;

    //Constructor privado
    private PreferencesManager(Context context) {
        anunciosMarcados = new ArrayList<>();
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
        cargarAnuncios();
    }

    //Metodo para obtener la instancia de la clase
    public static PreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new PreferencesManager(context);
        }
        return instance;
    }

    //Metodo para obtener los anuncios marcados
    public List<Anuncio> getAnunciosMarcados() {
        return anunciosMarcados;
    }

    //Metodo para marcar un anuncio
    public void marcarAnuncio(Anuncio anuncio) {
        if (!anunciosMarcados.contains(anuncio)) {
            anuncio.setChecked(true);
            anunciosMarcados.add(anuncio);
            guardarAnuncios();
        }
    }

    //Metodo para desmarcar un anuncio
    public void desmarcarAnuncio(Anuncio anuncio) {
        if (anunciosMarcados.contains(anuncio)) {
            anuncio.setChecked(false);
            anunciosMarcados.remove(anuncio);
            guardarAnuncios();
        }
    }

    //Metodo para guardar los anuncios en sharedPreferences
    private void guardarAnuncios() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ANUNCIOS_MARCADOS, gson.toJson(anunciosMarcados));
        editor.apply();
    }

    //Metodo para cargar los anuncios de sharedPreferences
    private void cargarAnuncios() {
        String json = sharedPreferences.getString(KEY_ANUNCIOS_MARCADOS, null);
        Type type = new TypeToken<ArrayList<Anuncio>>() {}.getType();
        anunciosMarcados = json != null ? gson.fromJson(json, type) : new ArrayList<>();
    }
}