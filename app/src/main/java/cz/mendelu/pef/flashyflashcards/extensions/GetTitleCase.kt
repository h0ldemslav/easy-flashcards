package cz.mendelu.pef.flashyflashcards.extensions

import java.util.Locale

fun String.getTitleCase(): String {
    return this
        .lowercase()
        .replaceFirstChar { it.titlecase(Locale.getDefault()) }
}