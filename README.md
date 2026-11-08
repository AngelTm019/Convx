# Convx

Reproductor de música para Android con una interfaz inspirada en el cristal, controles personalizados y herramientas para descubrir, organizar y reproducir música.

## Estado del proyecto

Este repositorio contiene la aplicación Convx y sus módulos. Los cambios funcionales y visuales que se realicen en este proyecto se documentarán en la sección **Cambios completados** únicamente cuando estén terminados y hayan sido verificados.

## Cambios completados

_Aún no se han registrado cambios nuevos en esta etapa. Las tareas que estén en desarrollo o en pruebas no se incluirán aquí._

## Estructura del proyecto

Convx es un proyecto Android de Gradle con varios módulos. Cada módulo contiene la funcionalidad que le corresponde.

| Ruta | Responsabilidad |
| --- | --- |
| `app/` | Aplicación Android, pantallas, modelos de vista, navegación, servicio de reproducción, almacenamiento y configuración general. |
| `hig/` | Componentes compartidos de interfaz, controles, temas, accesibilidad y utilidades visuales de cristal. |
| `innertube/` | Cliente InnerTube y sus modelos relacionados. |
| `lyrics/` | Proveedores, análisis y gestión de letras. |
| `canvas/` | Funciones de lienzo animado y catálogo. |
| `artwork/` | Carga, caché y transiciones de carátulas. |
| `spotify/` | Autenticación e importación de listas de reproducción de Spotify. |
| `integrations/` | Integraciones con Discord, Last.fm, reconocimiento musical y otras conexiones. |
| `network-storage/` | Fuentes de música mediante SMB y WebDAV. |
| `spine/` | Alojamiento y ciclo de vida de módulos adicionales. |
| `listen-together-server/` | Componente de servidor para la función de escucha compartida. |

La aplicación Android utiliza Kotlin, Jetpack Compose para la interfaz, Media3 para la reproducción, Room para la biblioteca local y DataStore para las preferencias. Los módulos de la aplicación se conectan mediante la configuración Hilt existente.

## Compilación local

### Requisitos

- JDK 21.
- Android Studio con Android SDK 37.
- Android NDK para los componentes nativos.

Clona el repositorio, ábrelo en Android Studio y permite que Gradle sincronice el proyecto. Para generar una versión de depuración o ejecutar las pruebas unitarias desde una terminal:

```bash
./gradlew :app:assembleUniversalFossDebug
./gradlew :app:testUniversalFossDebugUnitTest
```

En Windows, utiliza `gradlew.bat` con las mismas tareas. La variante `Foss` omite los servicios de Google Play; la variante `Gms` incluye compatibilidad con Google Cast.

## Contribuciones

Los informes de errores, las correcciones y las contribuciones de nuevas funciones son bienvenidos. Consulta la [guía de contribución](.github/CONTRIBUTING.md), el [código de conducta](CODE_OF_CONDUCT.md) y la [política de seguridad](SECURITY.md) antes de contribuir o informar de una vulnerabilidad.

Las herramientas de asistencia automatizada también deben seguir la [guía de contribución para agentes](AGENT_CONTRIBUTION_GUIDE.md). Sus recursos para Android, animación, revisión de código y análisis de requisitos se encuentran en `contributor/skills/` y pueden utilizarse sin conexión.

## Ayuda y comunidad

- [Comunidad en Discord](https://discord.gg/Ejeb4cmzfd)
- [Apoyar el proyecto en Ko-fi](https://ko-fi.com/cosmictaser)
- [Sitio web del proyecto](https://cosmictaser.de5.net)

## Aviso legal

Convx es un proyecto independiente y no está afiliado, respaldado ni relacionado con YouTube, Google o sus empresas afiliadas. Algunas funciones utilizan interfaces no oficiales de terceros que pueden cambiar o dejar de funcionar. Cada usuario es responsable de cumplir las leyes aplicables y las condiciones de los servicios que utilice.

Convx se distribuye bajo la licencia GNU General Public License, versión 3.0. Consulta los archivos [LICENSE](LICENSE) y [NOTICE](NOTICE) para conocer los términos de la licencia y las atribuciones de terceros.

---

Proyecto mantenido originalmente por [Aryan (Cosmic Taser)](https://github.com/cosmictaserdev-creator).
