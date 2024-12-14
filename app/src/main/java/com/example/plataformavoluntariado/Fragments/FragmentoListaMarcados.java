package com.example.plataformavoluntariado.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.plataformavoluntariado.Gestion.AdaptadorAnuncio;
import com.example.plataformavoluntariado.Gestion.Anuncio;
import com.example.plataformavoluntariado.R;
import com.example.plataformavoluntariado.Almacenamiento.PreferencesManager;
import java.util.List;

//Fragmento que muestra la lista de anuncios marcados por el usuario
public class FragmentoListaMarcados extends Fragment implements AdaptadorAnuncio.OnItemClickListener {

    //Variables
    private RecyclerView recyclerView;
    private AdaptadorAnuncio adaptadorAnuncio;
    private PreferencesManager preferencesManager;

    //Metodo para crear la vista del fragmento
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_lista_marcados, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_anuncios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        preferencesManager = PreferencesManager.getInstance(requireContext());
        cargarAnunciosMarcados();
        return view;
    }

    //Metodo para cargar los anuncios marcados por el usuario
    private void cargarAnunciosMarcados() {
        List<Anuncio> anunciosMarcados = preferencesManager.getAnunciosMarcados();
        for (Anuncio anuncio : anunciosMarcados) {
            anuncio.setChecked(true); // Sync estado
        }
        if (adaptadorAnuncio == null) {
            adaptadorAnuncio = new AdaptadorAnuncio(anunciosMarcados, this);
            recyclerView.setAdapter(adaptadorAnuncio);
        } else {
            adaptadorAnuncio.setAnuncios(anunciosMarcados);
            adaptadorAnuncio.notifyDataSetChanged();
        }
    }

    //Metodo para actualizar la lista de anuncios
    public void actualizarLista() {
        if (adaptadorAnuncio != null) {
            List<Anuncio> anunciosMarcados = preferencesManager.getAnunciosMarcados();
            for (Anuncio anuncio : anunciosMarcados) {
                anuncio.setChecked(true); // Sync estado
            }
            adaptadorAnuncio.setAnuncios(anunciosMarcados);
            adaptadorAnuncio.notifyDataSetChanged();
        }
    }

    //Metodo para gestionar los checkbox de los anuncios
    @Override
    public void onItemClick(int posicion, boolean isChecked) {
        Anuncio anuncio = adaptadorAnuncio.getAnuncios().get(posicion);
        String mensaje = isChecked
                ? "¿Estás seguro de que quieres apuntarte a este voluntariado?"
                : "¿Estás seguro de que quieres desapuntarte de este voluntariado?";

        new AlertDialog.Builder(requireContext())
                .setTitle("Confirmar")
                .setMessage(mensaje)
                .setPositiveButton("Confirmar", (dialog, which) -> {
                    anuncio.setChecked(isChecked);
                    if (isChecked) {
                        preferencesManager.marcarAnuncio(anuncio);
                    } else {
                        preferencesManager.desmarcarAnuncio(anuncio);
                    }
                    adaptadorAnuncio.notifyItemChanged(posicion);

                    FragmentoListaAnuncios fragmentoListaAnuncios = (FragmentoListaAnuncios) getParentFragmentManager()
                            .findFragmentByTag("FragmentoListaAnuncios");
                    if (fragmentoListaAnuncios != null) {
                        fragmentoListaAnuncios.actualizarLista();
                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    anuncio.setChecked(!isChecked);
                    adaptadorAnuncio.notifyItemChanged(posicion);
                })
                .show();
    }

    //Metodo onResume para la reanudación del fragmento
    @Override
    public void onResume() {
        super.onResume();
        cargarAnunciosMarcados();
    }

    //Metodo onStop para la parada del fragmento
    @Override
    public void onStop() {
        super.onStop();
        recyclerView.setAdapter(null);
    }

    //Metodo onDestroy para la destrucción del fragmento
    @Override
    public void onDestroy() {
        super.onDestroy();
        preferencesManager = null;
    }
}