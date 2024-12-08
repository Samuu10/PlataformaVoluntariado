package com.example.plataformavoluntariado.Almacenamiento;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import com.example.plataformavoluntariado.Gestion.Anuncio;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PreferencesManager {

    private static final String PREF_NAME = "anuncios_prefs";
    private static final String KEY_ANUNCIOS_APUNTADOS = "anuncios_apuntados";
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private Context context;

    public PreferencesManager(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveAnuncios(List<Anuncio> anuncios) {
        if (anuncios == null) {
            return;
        }
        new SaveAnunciosTask().execute(anuncios);
    }

    public void loadAnuncios(LoadAnunciosCallback callback) {
        if (callback == null) {
            return;
        }
        new LoadAnunciosTask(callback).execute();
    }

    private class SaveAnunciosTask extends AsyncTask<List<Anuncio>, Void, Void> {
        @Override
        protected Void doInBackground(List<Anuncio>... lists) {
            if (lists == null || lists.length == 0) {
                return null;
            }
            List<Anuncio> anuncios = lists[0];
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String json = gson.toJson(anuncios);
            editor.putString(KEY_ANUNCIOS_APUNTADOS, json);
            editor.apply();
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {}
    }

    private class LoadAnunciosTask extends AsyncTask<Void, Void, List<Anuncio>> {
        private LoadAnunciosCallback callback;

        public LoadAnunciosTask(LoadAnunciosCallback callback) {
            this.callback = callback;
        }

        @Override
        protected List<Anuncio> doInBackground(Void... voids) {
            String json = sharedPreferences.getString(KEY_ANUNCIOS_APUNTADOS, null);
            Type type = new TypeToken<ArrayList<Anuncio>>() {}.getType();
            return json != null ? gson.fromJson(json, type) : new ArrayList<>();
        }

        @Override
        protected void onPostExecute(List<Anuncio> anuncios) {
            if (callback != null) {
                callback.onAnunciosLoaded(anuncios);
            }
        }
    }

    public interface LoadAnunciosCallback {
        void onAnunciosLoaded(List<Anuncio> anuncios);
    }
}