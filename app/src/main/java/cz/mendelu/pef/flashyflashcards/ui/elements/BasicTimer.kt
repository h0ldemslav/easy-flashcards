package cz.mendelu.pef.flashyflashcards.ui.elements

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.delay

@Composable
fun BasicTimer(
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.End,
    totalTimeInMillis: Long,
    refresh: Boolean,
    delayInMillis: Long = 100L,
    onTimeChange: (Long) -> Unit
) {
    LaunchedEffect(totalTimeInMillis, refresh) {
        if (totalTimeInMillis > 0 || refresh) {
            delay(delayInMillis)
            onTimeChange(totalTimeInMillis)
        }
    }

    Text(
        text = (totalTimeInMillis / 1000L).toString(),
        textAlign = textAlign,
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier
    )
}