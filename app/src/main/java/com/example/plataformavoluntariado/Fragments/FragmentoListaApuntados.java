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
import java.util.ArrayList;
import java.util.List;

public class FragmentoListaApuntados extends Fragment {

    private RecyclerView recyclerView;
    private AdaptadorAnuncio adaptadorAnuncio;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_lista_apuntados, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_anuncios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adaptadorAnuncio);

        return view;
    }
}