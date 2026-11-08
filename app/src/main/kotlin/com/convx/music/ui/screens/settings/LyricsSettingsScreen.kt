/**
 * Convx Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.convx.music.ui.screens.settings

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.convx.music.R
import com.convx.music.constants.AppleMusicLyricsBlurKey
import com.convx.music.constants.LyricsGlowEffectKey
import com.convx.music.constants.LyricsStandardBlurKey
import com.convx.music.ui.component.IosSettingsGroup
import com.convx.music.ui.component.IosSettingsItem
import com.convx.music.ui.ios.compat.Switch
import com.convx.music.ui.ios.compat.Text
import com.convx.music.utils.rememberPreference
import com.convx.music.utils.rememberEnumPreference
import com.convx.music.constants.LyricsAnimationStyle
import com.convx.music.constants.LyricsAnimationStyleKey
import com.convx.music.ui.component.EnumDialog
import com.convx.music.ui.component.lyricsStyleLabel
import androidx.compose.runtime.saveable.rememberSaveable

/** Lyrics look: glow and the two blur modes. Kept apart from the animation style and text layout. */
@Composable
fun LyricsSettingsScreen(navController: NavController) {
    val (animationStyle, selectAnimationStyle) = rememberEnumPreference(LyricsAnimationStyleKey, LyricsAnimationStyle.FLAMINGO)
    var showStyleDialog by rememberSaveable { mutableStateOf(false) }
    if (showStyleDialog) {
        EnumDialog(
            onDismiss = { showStyleDialog = false },
            onSelect = { selectAnimationStyle(it); showStyleDialog = false },
            title = stringResource(R.string.lyrics_animation_style),
            current = animationStyle,
            values = LyricsAnimationStyle.entries,
            valueText = { lyricsStyleLabel(it) },
        )
    }
    val context = androidx.compose.ui.platform.LocalContext.current
    val credentials = remember(context) { context.getSharedPreferences("lyrics_credentials", android.content.Context.MODE_PRIVATE) }
    var paxKey by remember { mutableStateOf(credentials.getString("paxsenix_api_key", "").orEmpty()) }
    val (glow, onGlowChange) = rememberPreference(LyricsGlowEffectKey, defaultValue = true)
    val (appleBlur, onAppleBlurChange) = rememberPreference(AppleMusicLyricsBlurKey, defaultValue = true)
    val (standardBlur, onStandardBlurChange) = rememberPreference(LyricsStandardBlurKey, defaultValue = false)

    SettingsPage(navController = navController, title = stringResource(R.string.lyrics), horizontalPadding = 16.dp) {
        IosSettingsGroup(title = stringResource(R.string.lyrics_animation_style), items = listOf(
            IosSettingsItem(
                icon = painterResource(R.drawable.lyrics),
                title = { Text(lyricsStyleLabel(animationStyle)) },
                description = { Text(stringResource(R.string.lyrics_centered_scroll_description)) },
                onClick = { showStyleDialog = true },
            ),
        ))
        LyricsFontSettings()
        IosSettingsGroup(title = "Fuentes de letras", items = listOf(
            IosSettingsItem(
                icon = painterResource(R.drawable.lyrics),
                title = { Text("Orden y disponibilidad de proveedores") },
                description = { Text("Arrastra para reordenar las fuentes y activar o desactivar cada proveedor") },
                onClick = { navController.navigate("settings/content/advanced") },
            ),
        ))
        com.convx.music.ui.ios.compat.OutlinedTextField(
            value = paxKey,
            onValueChange = {
                paxKey = it
                credentials.edit().putString("paxsenix_api_key", it.trim()).apply()
            },
            label = { Text("Clave API de PaxSenix") },
            supportingText = { Text("La utilizan las fuentes de PaxSenix, Spotify y Musixmatch") },
            singleLine = true,
            visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
        )
        IosSettingsGroup(
            title = "Efectos de las letras",
            items = listOf(
                IosSettingsItem(
                    icon = painterResource(R.drawable.lyrics),
                    title = { Text(stringResource(R.string.lyrics_glow_effect)) },
                    description = { Text(stringResource(R.string.lyrics_glow_effect_desc)) },
                    trailingContent = { Switch(checked = glow, onCheckedChange = onGlowChange) },
                    onClick = { onGlowChange(!glow) },
                ),
                IosSettingsItem(
                    icon = painterResource(R.drawable.lyrics),
                    title = { Text(stringResource(R.string.apple_music_lyrics_blur)) },
                    description = { Text(stringResource(R.string.apple_music_lyrics_blur_desc)) },
                    trailingContent = { Switch(checked = appleBlur, onCheckedChange = onAppleBlurChange) },
                    onClick = { onAppleBlurChange(!appleBlur) },
                ),
                IosSettingsItem(
                    icon = painterResource(R.drawable.lyrics),
                    title = { Text(stringResource(R.string.standard_lyrics_blur)) },
                    description = { Text(stringResource(R.string.apple_music_lyrics_blur_desc)) },
                    trailingContent = { Switch(checked = standardBlur, onCheckedChange = onStandardBlurChange) },
                    onClick = { onStandardBlurChange(!standardBlur) },
                ),
            ),
        )
        Spacer(Modifier.height(32.dp))
    }
}
