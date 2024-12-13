package com.example.plataformavoluntariado.Almacenamiento;

import android.os.AsyncTask;
import com.example.plataformavoluntariado.Gestion.Anuncio;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

//Clase que carga los anuncios de la base de datos de Firebase
public class FirebaseHelper {

    //Variables
    private final FirebaseDatabase database;
    private final List<Anuncio> anuncios;

    //Constructor
    public FirebaseHelper() {
        database = FirebaseDatabase.getInstance();
        this.anuncios = new ArrayList<>();
    }

    //Metodo para cargar los anuncios que llama a la clase interna CargarAnunciosTask
    public void cargarAnuncios(ValueEventListener listener) {
        new CargarAnunciosTask(listener).execute();
    }

    //Clase interna para cargar los anuncios en segundo plano
    private class CargarAnunciosTask extends AsyncTask<Void, Void, Void> {
        private ValueEventListener listener;

        public CargarAnunciosTask(ValueEventListener listener) {
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            DatabaseReference databaseReference = database.getReference("anuncios");
            databaseReference.addListenerForSingleValueEvent(listener);
            return null;
        }
    }
}