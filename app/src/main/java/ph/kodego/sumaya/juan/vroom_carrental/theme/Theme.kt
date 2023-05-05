package ph.kodego.sumaya.juan.vroom_carrental.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors

import androidx.compose.runtime.Composable

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */

@Composable
fun VroomCarRentalTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}