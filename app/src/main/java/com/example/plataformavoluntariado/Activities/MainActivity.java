package com.example.plataformavoluntariado.Activities;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.plataformavoluntariado.Almacenamiento.PreferencesManager;
import com.example.plataformavoluntariado.Fragments.FragmentoListaAnuncios;
import com.example.plataformavoluntariado.Fragments.FragmentoListaApuntados;
import com.example.plataformavoluntariado.Gestion.Anuncio;
import com.example.plataformavoluntariado.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private boolean showingSeleccionados = false;
    PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferencesManager = new PreferencesManager(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_principal);

        Button btnCambiarLista = findViewById(R.id.btn_cambiar_lista);
        btnCambiarLista.setOnClickListener(v -> cambiarLista());

        loadFragment(new FragmentoListaAnuncios(), "FragmentoListaAnuncios");
    }

    private void cambiarLista(){
        if (showingSeleccionados) {
            loadFragment(new FragmentoListaAnuncios(), "FragmentoListaAnuncios");
        } else {
            loadFragment(new FragmentoListaApuntados(), "FragmentoListaApuntados");
        }
        showingSeleccionados = !showingSeleccionados;
    }


    private void loadFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    // Metodo para actualizar los fragmentos
    public void actualizarFragmentos(List<Anuncio> anuncios) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof FragmentoListaApuntados) {
            ((FragmentoListaApuntados) currentFragment).actualizarLista(anuncios);
        }
    }
}