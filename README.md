# Audio Diario - App de Notas de Voz

Aplicación Android desarrollada en Kotlin y Jetpack Compose que permite gestionar notas de voz personales.

## Características
* **Grabación de Audio**: Captura de audio en formato MPEG_4/AAC utilizando el micrófono del dispositivo.
* **Gestión de Archivos**: Almacenamiento automático en el directorio interno de la aplicación (`context.filesDir`) con nombres basados en marcas de tiempo.
* **Reproducción Multimedia**: Reproductor integrado con funciones de preparación, play, pausa y stop.
* **Navegación Dinámica**: Sistema de navegación entre la lista principal y la pantalla de grabación.
* **Control de Permisos**: Gestión de permisos de audio en tiempo real con estados visuales claros para el usuario.

## Tecnologías Utilizadas
* **Lenguaje**: Kotlin
* **Interfaz**: Jetpack Compose
* **Multimedia**: MediaRecorder y MediaPlayer de Android
* **Navegación**: Navigation Compose
* **Almacenamiento**: Internal Storage (Files API)

## Estructura del Proyecto
* `interfaces/`: Contiene las pantallas (`HomeScreen`, `AudioScreen`), la navegación (`AppNav`) y la lógica de permisos.
* `media/`: Clases envolventes (`SimpleAudioPlayer` y `SimpleAudioRecorder`) para el manejo del hardware de audio.
* `model/`: Definición del modelo de datos `AudioNote`.
* `storage/`: Lógica de gestión de ficheros y listado de audios.

## Requisitos del Enunciado Cumplidos
1. **Dos Pantallas**: Implementadas `HomeScreen` (lista) y `AudioScreen` (grabación/reproducción).
2. **Modelo de Datos**: Clase `AudioNote` con ID, nombre, ruta y fecha.
3. **Gestión de Permisos**: Solicitud de permiso de micrófono con mensajes de error si se deniega.
4. **Almacenamiento Interno**: Los audios se guardan y recuperan desde `context.filesDir`.
5. **Estados en Compose**: Uso de `mutableStateOf` para actualizar la UI según el estado del audio (grabando, pausado, preparado, etc.).
