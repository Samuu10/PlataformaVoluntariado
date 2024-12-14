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

public class WidgetRemoteViews implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private List<Anuncio> anuncios;

    public WidgetRemoteViews(Context context) {
        this.context = context;
        this.anuncios = new ArrayList<>();
    }

    @Override
    public void onCreate() {}

    @Override
    public void onDataSetChanged() {
        loadAnunciosFromFirebase();
    }

    private void loadAnunciosFromFirebase() {
        FirebaseHelper firebaseHelper = new FirebaseHelper();
        firebaseHelper.cargarAnuncios(new ValueEventListener() {
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
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private void notifyAppWidgetViewDataChanged() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, WidgetAnuncios.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_listview);
    }

    @Override
    public void onDestroy() {
        if (anuncios != null) {
            anuncios.clear();
        }
    }

    @Override
    public int getCount() {
        return anuncios != null ? anuncios.size() : 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (anuncios == null || anuncios.size() == 0 || position < 0 || position >= anuncios.size()) {
            return null;
        }

        Anuncio anuncio = anuncios.get(position);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.item_widget);
        views.setTextViewText(R.id.titulo_anuncio_widget, anuncio.getTitulo());
        views.setImageViewResource(R.id.tipo_imagen_widget, anuncio.getTipoImagenResId());

        Intent fillInIntent = new Intent();
        views.setOnClickFillInIntent(R.id.item_widget_layout, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}