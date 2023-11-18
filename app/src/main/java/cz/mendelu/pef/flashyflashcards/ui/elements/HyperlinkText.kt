package cz.mendelu.pef.flashyflashcards.ui.elements

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle

@Composable
fun HyperlinkText(
    text: String,
    spanStyle: SpanStyle? = null,
    uri: String,
    uriHandler: UriHandler
) {
    val style = spanStyle ?: SpanStyle(
        color = MaterialTheme.colorScheme.primary,
        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
        fontWeight = FontWeight.SemiBold,
        textDecoration = TextDecoration.Underline
    )

    val annotatedString = buildAnnotatedString {
        pushStringAnnotation(tag = "URL", annotation = uri)

        withStyle(style = style) {
            append(text)
        }

        pop()
    }

    ClickableText(
        text = annotatedString,
        style = TextStyle(color = MaterialTheme.colorScheme.onSurface),
        onClick = {
            annotatedString
                .getStringAnnotations("URL", it, it)
                .firstOrNull()?.let { stringAnnotation ->
                    uriHandler.openUri(stringAnnotation.item)
                }
        }
    )
}
