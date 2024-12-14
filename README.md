# Plataforma de Voluntariado

## Objetivo de la App

El objetivo de esta aplicación es que los usuarios dispongan de una plataforma en la que puedan buscar y apuntarse a voluntariados.
La app dispone de un sitema de busqueda y filtrado para encontrar opciones de voluntariado según la ciudad del usuario o el tipo de voluntariado al que quiera apuntarse.
Los usuarios pueden ver una lista de anuncios disponibles, marcar los anuncios de voluntariados a los que quieran apuntasre y visualizar estos anuncios en otra lista.
La aplicación también incluye un widget que muestra los anuncios de voluntariado cercanos.

## Uso de Firebase

La aplicación utiliza Firebase para almacenar y cargar los anuncios de voluntariado.
Se han añadido datos de anuncios de diferentes tipos y diferentes ciudades para simular los intereses y opciones del usuario
Los datos se cargan desde Firebase y se muestran en la lista principal de la aplicación. 
La clase `FirebaseHelper` se encarga de gestionar la conexión con Firebase y cargar los datos.

## Estructura del Proyecto

### Clases JAVA

1. **MainActivity**:
   - Controla la actividad principal de la aplicación, donde se gestionan los fragmentos.
   - Maneja la lógica para cambiar entre la lista de anuncios y la lista de anuncios marcados.
   - Inicializa `PreferencesManager` para gestionar las preferencias compartidas.

2. **FragmentoListaAnuncios**:
   - Muestra la lista de anuncios disponibles.
   - Filtra los anuncios según la ciudad y el tipo seleccionados en los `Spinner`.
   - Carga los anuncios desde Firebase y gestiona el estado de los `CheckBox`.

3. **FragmentoListaMarcados**:
   - Muestra la lista de anuncios marcados por el usuario.
   - Carga los anuncios marcados desde `PreferencesManager` y gestiona el estado de los `CheckBox`.

4. **AdaptadorAnuncio**:
   - Adaptador para la lista de anuncios en el `RecyclerView`.
   - Gestiona la visualización de cada anuncio y el estado de los `CheckBox`.
   - Notifica a `PreferencesManager` cuando se marca o desmarca un anuncio.

5. **Anuncio**:
   - Clase que representa un anuncio.
   - Implementa `Parcelable` para permitir la transferencia de datos entre componentes.
   - Contiene atributos como `id`, `titulo`, `descripcion`, `tipo`, `ciudad`, `fecha` y `checked`.

6. **PreferencesManager**:
   - Gestiona las preferencias compartidas de la aplicación.
   - Guarda y carga los anuncios marcados en `SharedPreferences`.
   - Proporciona métodos para marcar y desmarcar anuncios.

7. **FirebaseHelper**:
   - Carga los anuncios desde la base de datos de Firebase.
   - Utiliza una tarea en segundo plano (`AsyncTask`) para cargar los datos de Firebase.

8. **WidgetAnuncios**:
   - Controla el widget de la aplicación.
   - Actualiza el widget y carga los anuncios.
   - Maneja los intents para abrir la aplicación desde el widget.

9. **WidgetRemoteViews**:
   - Proporciona las vistas remotas para el widget.
   - Carga los anuncios desde Firebase y los muestra en el widget.
   - Notifica al `AppWidgetManager` cuando los datos del widget han cambiado.

10. **WidgetService**:
    - Servicio para el widget de la aplicación.
    - Proporciona la fábrica de vistas remotas (`RemoteViewsFactory`) para el widget.

### Archivos XML

1. **actividad_principal.xml**:
   - Archivo de diseño para la actividad principal de la aplicación.
   - Contiene elementos como `TextView` para el título, `FrameLayout` para los fragmentos y un `Button` para cambiar de lista.

2. **fragmento_lista_anuncios.xml**:
   - Archivo de diseño para el fragmento que muestra la lista de anuncios.
   - Contiene elementos como `TextView` para el título, `Spinner` para filtrar por ciudad y tipo, y un `RecyclerView` para mostrar los anuncios.

3. **fragmento_lista_marcados.xml**:
   - Archivo de diseño para el fragmento que muestra la lista de anuncios marcados.
   - Contiene elementos como `TextView` para el título y un `RecyclerView` para mostrar los anuncios marcados.

4. **item_anuncio.xml**:
   - Archivo de diseño para cada elemento de la lista de anuncios.
   - Contiene elementos como `ImageView` para la imagen del tipo de anuncio, `TextView` para el título, descripción, ciudad y fecha, y un `CheckBox` para marcar o desmarcar el anuncio.

5. **item_widget.xml**:
   - Archivo de diseño para cada elemento del widget.
   - Contiene elementos como `ImageView` para la imagen del tipo de anuncio y `TextView` para el título del anuncio.

6. **widget.xml**:
   - Archivo de diseño para el widget de la aplicación.
   - Contiene elementos como `TextView` para el título, `ListView` para mostrar los anuncios y un `Button` para abrir la aplicación.

## Link al repositorio:
https://github.com/Samuu10/PlataformaVoluntariado.git
