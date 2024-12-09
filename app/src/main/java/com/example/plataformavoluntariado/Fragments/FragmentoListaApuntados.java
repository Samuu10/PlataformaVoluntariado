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
import com.example.plataformavoluntariado.Gestion.AdaptadorAnuncio;
import com.example.plataformavoluntariado.Gestion.Anuncio;
import com.example.plataformavoluntariado.R;
import com.example.plataformavoluntariado.Almacenamiento.PreferencesManager;
import java.util.List;
import java.util.stream.Collectors;

public class FragmentoListaApuntados extends Fragment implements PreferencesManager.LoadAnunciosCallback {

    private RecyclerView recyclerView;
    private AdaptadorAnuncio adaptadorAnuncio;
    private PreferencesManager preferencesManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_lista_apuntados, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_anuncios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        preferencesManager = new PreferencesManager(requireContext());
        preferencesManager.loadAnuncios(this);
        return view;
    }

    @Override
    public void onAnunciosLoaded(List<Anuncio> anuncios) {
        List<Anuncio> apuntados = anuncios.stream().filter(Anuncio::getChecked).collect(Collectors.toList());
        adaptadorAnuncio = new AdaptadorAnuncio(apuntados, (anuncio, isChecked) -> {
            if (!isChecked) {
                apuntados.remove(anuncio);
                adaptadorAnuncio.notifyDataSetChanged();
                preferencesManager.saveAnuncios(anuncios);
            }
        }, preferencesManager);
        recyclerView.setAdapter(adaptadorAnuncio);
    }

    //Metodo para actualizar la lista de anuncios
    public void actualizarLista(List<Anuncio> nuevosAnuncios) {
        List<Anuncio> apuntados = nuevosAnuncios.stream().filter(Anuncio::getChecked).collect(Collectors.toList());
        adaptadorAnuncio.setAnuncios(apuntados);
        adaptadorAnuncio.notifyDataSetChanged();
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