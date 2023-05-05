package ph.kodego.sumaya.juan.vroom_carrental.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ph.kodego.sumaya.juan.vroom_carrental.R


val NunitoSans = FontFamily(
    Font(R.font.nunitosans_regular, weight = FontWeight.Normal),
    Font(R.font.nunitosans_light, weight = FontWeight.Light),
    Font(R.font.nunitosans_bold, weight = FontWeight.Bold),
)
val Typography = Typography(
    NunitoSans,
    h1 = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 42.sp
    ),
    h2 = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 36.sp
    ),
    h3 = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp
    ),
    h4 = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp
    ),
    h5 = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    ),
    body1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    ),
    body2 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    button = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    caption = TextStyle(
        fontWeight = FontWeight.Light,
        fontSize = 12.sp
    )
)