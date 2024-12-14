package com.example.plataformavoluntariado.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
    private List<Anuncio> anuncios;
    private Spinner spinnerCiudad, spinnerTipo;

    //Metodo para crear la vista del fragmento
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_lista_anuncios, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_anuncios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        preferencesManager = PreferencesManager.getInstance(requireContext());
        handler = new Handler(Looper.getMainLooper());
        spinnerCiudad = view.findViewById(R.id.spinner_ciudad);
        spinnerTipo = view.findViewById(R.id.spinner_tipo);
        setupSpinners();
        cargarAnuncios();
        return view;
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> adapterCiudad = ArrayAdapter.createFromResource(getContext(),
                R.array.ciudades_array, android.R.layout.simple_spinner_item);
        adapterCiudad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCiudad.setAdapter(adapterCiudad);

        ArrayAdapter<CharSequence> adapterTipo = ArrayAdapter.createFromResource(getContext(),
                R.array.tipos_array, android.R.layout.simple_spinner_item);
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapterTipo);

        spinnerCiudad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filtrarAnuncios();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filtrarAnuncios();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void filtrarAnuncios() {
        if (anuncios == null || adaptadorAnuncio == null) {
            return; // Asegúrate de que los datos están listos
        }

        String ciudadSeleccionada = spinnerCiudad.getSelectedItem().toString();
        String tipoSeleccionado = spinnerTipo.getSelectedItem().toString();

        List<Anuncio> anunciosFiltrados = new ArrayList<>();
        for (Anuncio anuncio : anuncios) {
            boolean coincideCiudad = "Todas".equals(ciudadSeleccionada) || anuncio.getCiudad().equals(ciudadSeleccionada);
            boolean coincideTipo = "Todos".equals(tipoSeleccionado) || anuncio.getTipo().equals(tipoSeleccionado);

            if (coincideCiudad && coincideTipo) {
                anunciosFiltrados.add(anuncio);
            }
        }

        // Actualiza los datos en el adaptador y refleja el cambio en la UI
        adaptadorAnuncio.setAnuncios(anunciosFiltrados);
        adaptadorAnuncio.notifyDataSetChanged();
    }


    //Metodo para cargar los anuncios de Firebase
    private void cargarAnuncios() {
        new FirebaseHelper().cargarAnuncios(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                anuncios = new ArrayList<>(); // Reemplazar cualquier instancia previa
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    Anuncio anuncio = keyNode.getValue(Anuncio.class);
                    if (anuncio != null) {
                        // Sincronizar estado del anuncio con las preferencias
                        if (preferencesManager.isAnuncioMarcado(anuncio)) {
                            anuncio.setChecked(true);
                        } else {
                            anuncio.setChecked(false);
                        }
                        anuncios.add(anuncio);
                    }
                }

                if (adaptadorAnuncio == null) {
                    adaptadorAnuncio = new AdaptadorAnuncio(new ArrayList<>(anuncios), FragmentoListaAnuncios.this);
                    recyclerView.setAdapter(adaptadorAnuncio);
                } else {
                    adaptadorAnuncio.setAnuncios(anuncios);
                    adaptadorAnuncio.notifyDataSetChanged();
                }

                filtrarAnuncios(); // Aplica filtros iniciales si corresponde
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejo de errores
            }
        });
    }


    public void actualizarLista() {
        if (adaptadorAnuncio != null && anuncios != null) {
            for (Anuncio anuncio : anuncios) {
                anuncio.setChecked(preferencesManager.isAnuncioMarcado(anuncio));
            }
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
                    // Marcar o desmarcar anuncio en la lista principal
                    anuncio.setChecked(isChecked);
                    if (isChecked) {
                        preferencesManager.marcarAnuncio(anuncio);
                    } else {
                        preferencesManager.desmarcarAnuncio(anuncio);
                    }

                    // Notificar cambios en el adaptador actual
                    adaptadorAnuncio.notifyItemChanged(posicion);

                    // Actualizar otro fragmento, si está visible
                    FragmentoListaApuntados fragmentoListaApuntados = (FragmentoListaApuntados) getParentFragmentManager()
                            .findFragmentByTag("FragmentoListaApuntados");
                    if (fragmentoListaApuntados != null) {
                        fragmentoListaApuntados.actualizarLista();
                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    // Restaurar estado original
                    anuncio.setChecked(!isChecked);
                    adaptadorAnuncio.notifyItemChanged(posicion);
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