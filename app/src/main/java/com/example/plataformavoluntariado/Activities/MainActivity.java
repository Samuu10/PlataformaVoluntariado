package com.example.plataformavoluntariado.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.plataformavoluntariado.Fragments.FragmentoListaAnuncios;
import com.example.plataformavoluntariado.Fragments.FragmentoListaApuntados;
import com.example.plataformavoluntariado.R;

public class MainActivity extends AppCompatActivity {

    private boolean showingAnuncios = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_principal);

        if (savedInstanceState == null) {
            loadFragment(new FragmentoListaAnuncios());
        }

        Button btnCambiarLista = findViewById(R.id.btn_cambiar_lista);
        btnCambiarLista.setOnClickListener(v -> {
            if (showingAnuncios) {
                loadFragment(new FragmentoListaApuntados());
            } else {
                loadFragment(new FragmentoListaAnuncios());
            }
            showingAnuncios = !showingAnuncios;
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}