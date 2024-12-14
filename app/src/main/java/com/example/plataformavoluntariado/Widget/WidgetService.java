package com.example.plataformavoluntariado.Widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

//Clase que representa el servicio del widget de la aplicación
public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViews(this.getApplicationContext());
    }
}