package com.example.plataformavoluntariado.Widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.example.plataformavoluntariado.R;
import com.example.plataformavoluntariado.Gestion.Anuncio;
import com.example.plataformavoluntariado.Almacenamiento.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

//Clase que representa las vistas remotas del widget de la aplicación
public class WidgetRemoteViews implements RemoteViewsService.RemoteViewsFactory {

    //Variables
    private Context context;
    private List<Anuncio> anuncios;

    //Constructor
    public WidgetRemoteViews(Context context) {
        this.context = context;
        this.anuncios = new ArrayList<>();
    }

    //Metodo para crear las vistas remotas
    @Override
    public void onCreate() {}

    //Metodo para cargar los datos de los anuncios
    @Override
    public void onDataSetChanged() {
        loadAnunciosFromFirebase();
    }

    //Metodo para cargar los anuncios de la base de datos de Firebase
    private void loadAnunciosFromFirebase() {
        FirebaseHelper firebaseHelper = new FirebaseHelper();
        firebaseHelper.cargarAnuncios(new ValueEventListener() {

            //Simulamos que la ubicación del usuario es Madrid y cargamos los datos de los anuncios de Madrid
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                anuncios.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Anuncio anuncio = snapshot.getValue(Anuncio.class);
                    if (anuncio != null && "Madrid".equals(anuncio.getCiudad())) {
                        anuncios.add(anuncio);
                    }
                    if (anuncios.size() >= 5) {
                        break;
                    }
                }
                notifyAppWidgetViewDataChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    //Metodo para notificar que los datos del widget han cambiado
    private void notifyAppWidgetViewDataChanged() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, WidgetAnuncios.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_listview);
    }

    //Metodo onDestroy para la destrucción de las vistas remotas
    @Override
    public void onDestroy() {
        if (anuncios != null) {
            anuncios.clear();
        }
    }

    //Metodo getCount para obtener el número de elementos
    @Override
    public int getCount() {
        return anuncios != null ? anuncios.size() : 0;
    }

    //Metodo getViewAt para obtener la vista en la posición indicada
    @Override
    public RemoteViews getViewAt(int position) {
        if (anuncios == null || anuncios.size() == 0 || position < 0 || position >= anuncios.size()) {
            return null;
        }

        //Obtenemos el anuncio en la posición indicada
        Anuncio anuncio = anuncios.get(position);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.item_widget);
        views.setTextViewText(R.id.titulo_anuncio_widget, anuncio.getTitulo());
        views.setImageViewResource(R.id.tipo_imagen_widget, anuncio.getTipoImagenResId());

        //Creamos un intent para abrir la aplicación al hacer clic en el anuncio
        Intent fillInIntent = new Intent();
        views.setOnClickFillInIntent(R.id.item_widget_layout, fillInIntent);

        return views;
    }

    //Metodo getLoadingView para obtener la vista de carga
    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    //Metodo getViewTypeCount para obtener el número de tipos de vistas
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    //Metodo getItemId para obtener el id del elemento en la posición indicada
    @Override
    public long getItemId(int position) {
        return position;
    }

    //Metodo hasStableIds para comprobar si los ids son estables
    @Override
    public boolean hasStableIds() {
        return true;
    }
}