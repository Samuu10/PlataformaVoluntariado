package com.example.plataformavoluntariado.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.example.plataformavoluntariado.Almacenamiento.FirebaseHelper;
import com.example.plataformavoluntariado.Almacenamiento.PreferencesManager;
import com.example.plataformavoluntariado.Gestion.Anuncio;
import com.example.plataformavoluntariado.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

//Fragmento que muestra la lista de anuncios
public class FragmentoListaAnuncios extends Fragment implements AdaptadorAnuncio.OnItemClickListener {

    //Variables
    private AdaptadorAnuncio adaptadorAnuncio;
    private RecyclerView recyclerView;
    private PreferencesManager preferencesManager;
    private Handler handler;

    //Metodo para crear la vista del fragmento
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_lista_anuncios, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_anuncios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        preferencesManager = PreferencesManager.getInstance(requireContext());
        handler = new Handler(Looper.getMainLooper());
        cargarAnuncios();
        return view;
    }

    //Metodo para cargar los anuncios de Firebase
    private void cargarAnuncios() {
        new FirebaseHelper().cargarAnuncios(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Anuncio> anuncios = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    Anuncio anuncio = keyNode.getValue(Anuncio.class);
                    if (preferencesManager.getAnunciosMarcados().contains(anuncio)) {
                        anuncio.setChecked(true);
                    }
                    anuncios.add(anuncio);
                }
                adaptadorAnuncio = new AdaptadorAnuncio(anuncios, FragmentoListaAnuncios.this);
                recyclerView.setAdapter(adaptadorAnuncio);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    //Metodo para gestionar los checkbox de los anuncios
    @Override
    public void onItemClick(int posicion, boolean isChecked) {
        Anuncio anuncio = adaptadorAnuncio.getAnuncios().get(posicion);
        String mensaje = isChecked ? "¿Estás seguro de que quieres marcar este anuncio?" : "¿Estás seguro de que quieres desmarcar este anuncio?";
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
                    handler.post(() -> adaptadorAnuncio.notifyItemChanged(posicion));
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    anuncio.setChecked(!isChecked);
                    handler.post(() -> adaptadorAnuncio.notifyItemChanged(posicion));
                })
                .show();
    }

    //Metodo onResume para la reanudación del fragmento
    @Override
    public void onResume() {
        super.onResume();
        cargarAnuncios();
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