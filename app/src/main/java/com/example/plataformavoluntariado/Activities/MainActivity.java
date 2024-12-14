package com.example.plataformavoluntariado.Activities;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.plataformavoluntariado.Almacenamiento.PreferencesManager;
import com.example.plataformavoluntariado.Fragments.FragmentoListaAnuncios;
import com.example.plataformavoluntariado.Fragments.FragmentoListaMarcados;
import com.example.plataformavoluntariado.R;

//Clase principal de la aplicación en la que se gestionan los fragmentos
public class MainActivity extends AppCompatActivity {

    //Variables
    private boolean showingSeleccionados = false;
    PreferencesManager preferencesManager;

    //Metodo para crear la actividad
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_principal);

        //Inicializamos sharedPreferences
        preferencesManager = PreferencesManager.getInstance(this);

        //Botón para cambiar de lista
        Button btnCambiarLista = findViewById(R.id.btn_cambiar_lista);
        btnCambiarLista.setOnClickListener(v -> cambiarLista());

        //Gestionamos la carga de los fragmentos
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentoListaAnuncios fragmentoListaAnuncios = new FragmentoListaAnuncios();
        FragmentoListaMarcados fragmentoListaMarcados = new FragmentoListaMarcados();
        fragmentTransaction.add(R.id.fragment_container, fragmentoListaAnuncios, "FragmentoListaAnuncios");
        fragmentTransaction.add(R.id.fragment_container, fragmentoListaMarcados, "FragmentoListaMarcados");
        fragmentTransaction.hide(fragmentoListaMarcados);
        fragmentTransaction.commit();
    }

    //Metodo para cambiar de lista
    private void cambiarLista() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragmentToShow = showingSeleccionados ? fragmentManager.findFragmentByTag("FragmentoListaAnuncios") : fragmentManager.findFragmentByTag("FragmentoListaMarcados");
        Fragment fragmentToHide = showingSeleccionados ? fragmentManager.findFragmentByTag("FragmentoListaMarcados") : fragmentManager.findFragmentByTag("FragmentoListaAnuncios");
        if (fragmentToShow != null && fragmentToHide != null) {
            fragmentTransaction.hide(fragmentToHide);
            fragmentTransaction.show(fragmentToShow);
            fragmentTransaction.commit();
            showingSeleccionados = !showingSeleccionados;
        }
    }
}