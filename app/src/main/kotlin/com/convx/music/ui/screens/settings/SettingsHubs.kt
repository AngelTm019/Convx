/**
 * Convx Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.convx.music.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.convx.music.R
import com.convx.music.ui.component.IosPageSheet
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.convx.music.ui.component.SymbolIcon
import com.convx.music.ui.component.sf
import com.convx.music.ui.component.shapes.ContinuousRoundedRectangle
import com.convx.music.ui.ios.compat.HorizontalDivider
import com.convx.music.ui.ios.compat.Icon
import com.convx.music.ui.ios.compat.Text
import com.convx.music.ui.ios.foundation.IosColors
import com.convx.music.ui.ios.foundation.IosMetrics
import com.convx.music.ui.ios.foundation.IosTheme
import com.convx.music.ui.ios.foundation.iosRowPress

/** What a settings row does when tapped. */
sealed interface SettingsAction {
    data class Route(val route: String) : SettingsAction
    data object PlayerDesign : SettingsAction
    data object LyricsStyle : SettingsAction
    data object AppleMusicImport : SettingsAction
}

/** One row: a coloured glyph tile, a name, and where it goes. */
data class SettingsLink(
    val title: String,
    val icon: Int,
    val tint: IosColors.() -> Color,
    val action: SettingsAction,
    val subtitle: String? = null,
    /** Extra words people search with that are not in the title. */
    val keywords: String = "",
)

data class SettingsLinkGroup(val title: String?, val links: List<SettingsLink>)

/** A root category and the page it opens. */
data class SettingsHub(
    val id: String,
    val title: String,
    val icon: Int,
    val tint: IosColors.() -> Color,
    val groups: List<SettingsLinkGroup>,
) {
    val route get() = "settings/hub/$id"
    fun asLink() = SettingsLink(title, icon, tint, SettingsAction.Route(route))
}

private fun route(r: String) = SettingsAction.Route(r)

/** The settings tree: root categories, the pages they open, and every destination under them. */
object SettingsHubs {
    val player = SettingsHub(
        "player", "Reproductor y letras", R.drawable.play, { systemPink },
        listOf(
            SettingsLinkGroup(
                "Reproductor",
                listOf(
                    SettingsLink("Diseño del reproductor", R.drawable.play, { systemPink }, SettingsAction.PlayerDesign,
                        "Tarjeta, vinilo, trébol y Apple Music: vistas previas en vivo", "theme layout background seek bar slider up next shadow volume bar"),
                    SettingsLink("Controles y diseño", R.drawable.sliders, { systemIndigo }, route("settings/appearance/advanced"),
                        "Portada, mini reproductor, botones e indicadores", "mini player comment button audio quality badge thumbnail"),
                    SettingsLink("Iconos del reproductor", R.drawable.tune, { systemOrange }, route("settings/appearance/playericons"),
                        "Elige los iconos de los controles de reproducción"),
                    SettingsLink("Canvas", R.drawable.canvas_art, { systemTeal }, route("settings/appearance/canvas"),
                        "Portada animada detrás del reproductor", "video animated artwork"),
                ),
            ),
            SettingsLinkGroup(
                "Letras",
                listOf(
                    SettingsLink("Estilo de letras", R.drawable.lyrics, { systemPink }, SettingsAction.LyricsStyle,
                        "Animación, posición, desplazamiento y gestos: vista previa en vivo", "animation karaoke word by word apple music position auto scroll swipe"),
                    SettingsLink("Efectos de letras", R.drawable.lyrics, { systemPink }, route("settings/lyrics"),
                        "Estilo, tipografía, tamaño, brillo y desenfoque", "convx lyrics font size glow blur apple music standard"),
                    SettingsLink("Texto y diseño", R.drawable.lyrics, { systemOrange }, route("settings/appearance"),
                        "Tamaño del texto, espaciado y letras a pantalla completa", "size spacing fullscreen"),
                    SettingsLink("Fuentes y prioridad", R.drawable.lyrics, { systemBlue }, route("settings/content/advanced"),
                        "Elige y ordena los proveedores de letras", "provider youlyplus paxsenix lrclib"),
                    SettingsLink("Romanización", R.drawable.translate, { systemTeal }, route("settings/content/romanization"),
                        "Mostrar letras con caracteres latinos", "japanese korean chinese cyrillic"),
                    SettingsLink("Traducción con IA", R.drawable.translate, { systemPurple }, route("settings/ai"),
                        "Proveedor, modelo e idioma de destino", "openrouter deepl api key"),
                ),
            ),
        ),
    )

    val playback = SettingsHub(
        "playback", "Reproducción y audio", R.drawable.equalizer, { systemRed },
        listOf(
            SettingsLinkGroup(
                "Sonido",
                listOf(
                    SettingsLink("Ecualizador", R.drawable.equalizer, { systemGreen }, route("settings/equalizer"), "Personaliza el sonido", "eq bass treble"),
                    SettingsLink("Calidad y sonido", R.drawable.tune, { systemBlue }, route("settings/player"),
                        "Calidad de audio, normalización y omisión de silencios", "quality normalization loudness silence"),
                    SettingsLink("Transiciones entre canciones", R.drawable.shuffle, { systemPurple }, route("settings/song_transitions"),
                        "Fundido cruzado, reproducción sin pausas y mezcla automática", "crossfade gapless automix dj mixing"),
                ),
            ),
            SettingsLinkGroup(
                "Cola y comportamiento",
                listOf(
                    SettingsLink("Cola y reproducción automática", R.drawable.queue_music, { systemOrange }, route("settings/player"),
                        "Cola persistente, canciones similares y memoria de reproducción aleatoria", "persistent queue autoplay similar shuffle repeat"),
                    SettingsLink("Bluetooth y salida de audio", R.drawable.cast, { systemIndigo }, route("settings/player"),
                        "Reanudar al conectar, pausar al silenciar y transmitir", "bluetooth cast chromecast mute"),
                ),
            ),
            SettingsLinkGroup(
                "Avanzado",
                listOf(
                    SettingsLink("Reproducción avanzada", R.drawable.settings, { systemGray }, route("settings/player/advanced"),
                        "Procesamiento externo, audio bit-perfect y reglas de la cola", "offload bit perfect duplicates history duration"),
                    SettingsLink("JioSaavn", R.drawable.music_note, { systemTeal }, route("settings/player/jio"), "Ajustes de la fuente de streaming"),
                    SettingsLink("Análisis de DJ con IA", R.drawable.equalizer, { systemPurple }, route("ai_dj_set"),
                        "Analiza canciones y crea una mezcla", "bpm key mix analysis"),
                ),
            ),
        ),
    )

    val appearance = SettingsHub(
        "appearance", "Apariencia", R.drawable.palette, { systemPurple },
        listOf(
            SettingsLinkGroup(
                "Tema y colores",
                listOf(
                    SettingsLink("Tema", R.drawable.palette, { systemPurple }, route("settings/appearance/theme"), "Color de énfasis, modo oscuro y negro puro", "dark light accent color"),
                    SettingsLink("Preajustes", R.drawable.palette, { systemPink }, route("settings/appearance/presets"), "Apariencias prediseñadas"),
                    SettingsLink("Crea tu propio diseño", R.drawable.edit, { systemOrange }, route("settings/appearance/diy"), "Diseña un tema desde cero", "diy"),
                    SettingsLink("Cristal líquido", R.drawable.palette, { systemCyan }, route("settings/appearance/liquidglass"), "Desenfoque, tinte, reflejos y lente", "glass blur"),
                ),
            ),
            SettingsLinkGroup(
                "Aplicación",
                listOf(
                    SettingsLink("Tipografía", R.drawable.alphabet_cyrillic, { systemGray }, route("settings/appearance/font"), "Tipo de letra de la aplicación"),
                    SettingsLink("Icono de la aplicación", R.drawable.convx_logo, { systemBlue }, route("settings/appearance/appicon"), "Icono de la pantalla de inicio"),
                    SettingsLink("Diseño e inicio", R.drawable.sliders, { systemGreen }, route("settings/appearance"),
                        "Idioma, pestaña predeterminada, densidad e inicio", "language default tab density refresh rate"),
                    SettingsLink("Orden del contenido de Inicio", R.drawable.sliders, { systemOrange }, route("settings/home_feed_order"), "Organiza las secciones de Inicio"),
                ),
            ),
            SettingsLinkGroup(
                "Avanzado",
                listOf(
                    SettingsLink("Apariencia avanzada", R.drawable.settings, { systemGray }, route("settings/appearance/advanced"),
                        "Cuadrículas, esquinas, desenfoque y desplazamiento excesivo", "grid corner radius blur overscroll pure black"),
                ),
            ),
        ),
    )

    val library = SettingsHub(
        "library", "Biblioteca y contenido", R.drawable.music_note, { systemOrange },
        listOf(
            SettingsLinkGroup(
                "Contenido",
                listOf(
                    SettingsLink("Región y filtros", R.drawable.language, { systemBlue }, route("settings/content"),
                        "Idioma, país, contenido explícito, Shorts y ahorro de datos", "language country explicit shorts data saver video"),
                    SettingsLink("Páginas de artistas e Inicio", R.drawable.music_note, { systemPink }, route("settings/content/advanced"),
                        "Videos de artistas, selecciones rápidas y duración de listas", "artist background video quick picks"),
                ),
            ),
            SettingsLinkGroup(
                "Música local",
                listOf(
                    SettingsLink("Carpetas de música", R.drawable.music_note, { systemOrange }, route("settings/player/local_folders"), "Elige las carpetas que se analizarán", "local files"),
                    SettingsLink("Buscar archivos locales", R.drawable.search, { systemGreen }, route("settings/scan_music"), "Busca canciones en este dispositivo"),
                    SettingsLink("Almacenamiento en red", R.drawable.storage, { systemGray }, route("settings/network_storage"), "Recursos compartidos SMB y WebDAV", "smb webdav nas"),
                ),
            ),
            SettingsLinkGroup(
                null,
                listOf(SettingsLink("Módulos", R.drawable.link, { systemBlue }, route("settings/modules"), "Fuentes de música y complementos", "plugins extensions")),
            ),
        ),
    )

    val accounts = SettingsHub(
        "accounts", "Cuentas y servicios", R.drawable.account, { systemGreen },
        listOf(
            SettingsLinkGroup(
                "Cuentas",
                listOf(
                    SettingsLink("YouTube Music", R.drawable.account, { systemRed }, route("settings/account"), "Inicio de sesión, sincronización y ListenBrainz", "login youtube sync listenbrainz"),
                    SettingsLink("Spotify", R.drawable.spotify, { systemGreen }, route("settings/spotify"), "Conecta e importa listas de reproducción", "import"),
                    SettingsLink("Importar desde Apple Music", R.drawable.playlist_add, { systemRed }, SettingsAction.AppleMusicImport, "Importa listas desde un enlace o una exportación", "apple music playlist import export"),
                    SettingsLink("Discord", R.drawable.discord, { systemIndigo }, route("settings/integrations/discord"), "Estado de reproducción", "rich presence"),
                    SettingsLink("Last.fm", R.drawable.music_note, { systemRed }, route("settings/integrations/lastfm"), "Registro de reproducciones", "scrobble"),
                ),
            ),
            SettingsLinkGroup(
                "Servicios",
                listOf(
                    SettingsLink("Red y proxy", R.drawable.wifi_proxy, { systemGray }, route("settings/network"), "Versión IP y proxy", "proxy ipv6 ipv4 network"),
                ),
            ),
        ),
    )

    val privacy = SettingsHub(
        "privacy", "Privacidad y almacenamiento", R.drawable.security, { systemBlue },
        listOf(
            SettingsLinkGroup(
                null,
                listOf(
                    SettingsLink("Privacidad", R.drawable.security, { systemBlue }, route("settings/privacy"), "Historial y capturas de pantalla", "pause history clear history screenshot"),
                    SettingsLink("Almacenamiento", R.drawable.storage, { systemGray }, route("settings/storage"), "Descargas y cachés", "cache downloads clear"),
                    SettingsLink("Copia de seguridad y restauración", R.drawable.restore, { systemGreen }, route("settings/backup_restore"), "Guarda o restaura tu biblioteca", "backup import export"),
                ),
            ),
        ),
    )

    val help = SettingsHub(
        "help", "Ayuda e información", R.drawable.info, { systemGray },
        listOf(
            SettingsLinkGroup(
                null,
                listOf(
                    SettingsLink("Informe de fallos", R.drawable.bug_report, { systemRed }, route("settings/help/crash_logs"), "Consulta o comparte el último informe de fallos"),
                    SettingsLink("Registros de reproducción", R.drawable.bug_report, { systemOrange }, route("settings/content/advanced"), "Soluciona problemas de reproducción", "logs client probe"),
                    SettingsLink("Registro de cambios", R.drawable.network_update, { systemBlue }, route("settings/changelog"), "Novedades"),
                    SettingsLink("Acerca de", R.drawable.info, { systemGray }, route("settings/about"), "Versión, licencias y proyecto"),
                ),
            ),
        ),
    )

    val all = listOf(player, playback, appearance, library, accounts, privacy, help)

    fun byId(id: String?) = all.firstOrNull { it.id == id }
}

/** Which settings sheet is open; one app-wide slot so no two screens can each open their own. */
object SettingsSheets {
    var open by mutableStateOf<SettingsAction?>(null)
}

/** Runs a row's action: a page, or one of the sheets that open over the current screen. */
@Composable
fun rememberSettingsActionHandler(navController: NavController): (SettingsAction) -> Unit = { action ->
    when (action) {
        is SettingsAction.Route -> navController.navigate(action.route)
        SettingsAction.PlayerDesign, SettingsAction.LyricsStyle, SettingsAction.AppleMusicImport -> SettingsSheets.open = action
    }
}

/** Hosted once at the app root; shows whichever settings sheet is open. */
@Composable
fun SettingsSheetHost(navController: NavController) {
    val close = { SettingsSheets.open = null }
    when (SettingsSheets.open) {
        SettingsAction.PlayerDesign -> IosPageSheet("Diseño del reproductor", close) {
            PlayerDesignSheetContent(onMoreOptions = {
                close()
                navController.navigate("settings/appearance/playertheme")
            })
        }
        SettingsAction.LyricsStyle -> IosPageSheet("Estilo de letras", close) { LyricsAnimationSheetContent() }
        SettingsAction.AppleMusicImport -> AppleMusicImportDialogHost(
            navController = navController,
            onCloseSheet = { SettingsSheets.open = null },
        )
        else -> Unit
    }
}

/** A category's page: its groups of rows, iOS inset-grouped. */
@Composable
fun SettingsHubScreen(navController: NavController, hub: SettingsHub) {
    val onAction = rememberSettingsActionHandler(navController)
    SettingsPage(navController = navController, title = hub.title, horizontalPadding = 16.dp) {
        SettingsHubBody(hub, onAction)
    }
}

@Composable
fun ColumnScope.SettingsHubBody(hub: SettingsHub, onAction: (SettingsAction) -> Unit) {
    hub.groups.forEachIndexed { index, group ->
        if (index > 0) Spacer(Modifier.height(28.dp))
        SettingsLinkCard(group.title, group.links, onAction)
    }
}

@Composable
fun SettingsLinkCard(
    title: String?,
    links: List<SettingsLink>,
    onAction: (SettingsAction) -> Unit,
    trailing: (SettingsLink) -> String? = { null },
    selected: SettingsLink? = null,
) {
    Column(Modifier.fillMaxWidth()) {
        if (title != null) Text(
            title,
            style = IosTheme.typography.footnote,
            fontWeight = FontWeight.SemiBold,
            color = IosTheme.colors.secondaryLabel,
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 8.dp),
        )
        Column(
            Modifier
                .fillMaxWidth()
                .clip(ContinuousRoundedRectangle(IosMetrics.GroupedCornerRadius))
                .background(IosTheme.colors.secondarySystemGroupedBackground),
        ) {
            links.forEachIndexed { i, link ->
                if (i > 0) HorizontalDivider(
                    Modifier.padding(start = 58.dp),
                    color = IosTheme.colors.separator,
                    thickness = IosMetrics.SeparatorThickness,
                )
                SettingsLinkRow(link, trailing(link), selected == link) { onAction(link.action) }
            }
        }
    }
}

@Composable
fun SettingsLinkRow(link: SettingsLink, trailing: String?, selected: Boolean, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .then(if (selected) Modifier.background(IosTheme.colors.systemFill) else Modifier)
            .iosRowPress(onClick = onClick)
            .defaultMinSize(minHeight = 52.dp)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(30.dp)
                .clip(ContinuousRoundedRectangle(7.dp))
                .background(link.tint(IosTheme.colors)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(painterResource(link.icon), null, tint = Color.White, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(link.title, style = IosTheme.typography.body, color = IosTheme.colors.label)
            if (link.subtitle != null) Text(
                link.subtitle,
                style = IosTheme.typography.footnote,
                color = IosTheme.colors.secondaryLabel,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
        if (trailing != null) {
            Text(trailing, style = IosTheme.typography.body, color = IosTheme.colors.secondaryLabel, maxLines = 1)
            Spacer(Modifier.width(8.dp))
        }
        SymbolIcon(symbol = sf("chevron.right"), contentDescription = null, size = 14.dp, tint = IosTheme.colors.tertiaryLabel)
    }
}
