package se.id21afm.umu.cs.bouldr.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import se.id21afm.umu.cs.bouldr.R

val Jaro = FontFamily(Font(R.font.jaroregular, FontWeight.Bold))
val Inter = FontFamily(Font(R.font.interregular, FontWeight.Normal))

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Inter,
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Inter,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        color = Gray400
    ),
    titleLarge = TextStyle(
        fontFamily = Jaro,
        fontSize = 52.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = Jaro,
        fontSize = 38.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = Jaro,
        fontSize = 26.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        letterSpacing = 0.05.sp
    )
)