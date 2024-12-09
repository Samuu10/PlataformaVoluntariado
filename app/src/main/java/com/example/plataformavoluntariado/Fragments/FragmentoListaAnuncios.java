package com.example.plataformavoluntariado.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plataformavoluntariado.Activities.MainActivity;
import com.example.plataformavoluntariado.Gestion.AdaptadorAnuncio;
import com.example.plataformavoluntariado.Almacenamiento.FirebaseHelper;
import com.example.plataformavoluntariado.Almacenamiento.PreferencesManager;
import com.example.plataformavoluntariado.Gestion.Anuncio;
import com.example.plataformavoluntariado.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FragmentoListaAnuncios extends Fragment {

    // Variables
    private AdaptadorAnuncio adaptadorAnuncio;
    private RecyclerView recyclerView;
    private PreferencesManager preferencesManager;

    // Metodo para crear la vista del fragmento
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_lista_anuncios, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_anuncios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        preferencesManager = new PreferencesManager(requireContext());

        new FirebaseHelper().cargarAnuncios(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Anuncio> anuncios = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    Anuncio anuncio = keyNode.getValue(Anuncio.class);
                    anuncios.add(anuncio);
                }
                adaptadorAnuncio = new AdaptadorAnuncio(anuncios, (anuncio, isChecked) -> {}, preferencesManager);
                recyclerView.setAdapter(adaptadorAnuncio);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return view;
    }

    //Metodo para gestionar la pausa del fragmento y liberar recursos
    @Override
    public void onStop() {
        super.onStop();
        //Liberar referencias a vistas o adaptadores
        recyclerView.setAdapter(null);
    }

    //Metodo para gestionar la destrucción del fragmento y liberar recursos
    @Override
    public void onDestroy() {
        super.onDestroy();
        //Liberar referencias a objetos grandes o contextos
        preferencesManager = null;
    }
}