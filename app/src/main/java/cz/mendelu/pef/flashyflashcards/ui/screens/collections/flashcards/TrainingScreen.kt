package cz.mendelu.pef.flashyflashcards.ui.screens.collections.flashcards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.model.UiState
import cz.mendelu.pef.flashyflashcards.model.Word
import cz.mendelu.pef.flashyflashcards.navigation.graphs.CollectionsNavGraph
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicScaffold
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicTextFieldElement
import cz.mendelu.pef.flashyflashcards.ui.elements.PlaceholderElement
import cz.mendelu.pef.flashyflashcards.ui.screens.ScreenErrors
import cz.mendelu.pef.flashyflashcards.ui.theme.basicMargin
import cz.mendelu.pef.flashyflashcards.ui.theme.mediumMargin
import cz.mendelu.pef.flashyflashcards.ui.theme.smallMargin

@CollectionsNavGraph
@Destination
@Composable
fun TrainingScreen(
    navController: NavController,
    viewModel: TrainingScreenViewModel = hiltViewModel(),
    collectionId: Long?
) {
    LaunchedEffect(Unit) {
        if (viewModel.uiState.data == null) {
            viewModel.getAllCollectionWords(collectionId)
        }
    }

    BasicScaffold(
        topAppBarTitle = stringResource(id = R.string.training_label),
        showLoading = viewModel.uiState.loading,
        onBackClick = { navController.popBackStack() }
    ) { paddingValues ->
        TrainingScreenContent(
            paddingValues = paddingValues,
            uiState = viewModel.uiState,
            actions = viewModel
        )
    }
}

@Composable
fun TrainingScreenContent(
    paddingValues: PaddingValues,
    uiState: UiState<Word, ScreenErrors>,
    actions: TrainingScreenActions
) {
    var boxText by remember {
        mutableStateOf("")
    }
    var answer by remember {
        mutableStateOf("")
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(mediumMargin()),
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .padding(basicMargin())
            .padding(top = basicMargin())
    ) {
        if (uiState.data != null) {
            if (boxText.isEmpty()) {
                boxText = uiState.data!!.name
            }

            Flashcard(text = boxText) {
                boxText = if (boxText == uiState.data!!.name) {
                    uiState.data!!.translation
                } else {
                    uiState.data!!.name
                }
            }

            Answer(
                answer = answer,
                onValueChange = {
                    answer = it
                },
                errors = uiState.errors
            )

            Spacer(modifier = Modifier.height(smallMargin()))

            ElevatedButton(onClick = {
                if (actions.isAnswerCorrect(answer)) {
                    actions.setNextWord()
                    answer = ""
                    boxText = ""
                }
            }) {
                Text(text = stringResource(id = R.string.next_label))
            }
        } else if (uiState.errors == null) {
            Text(
                color = MaterialTheme.colorScheme.primary,
                text = stringResource(id = R.string.training_finished).uppercase(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Button(onClick = {
                actions.resetWordToTheFirst()
            }) {
                Text(text = stringResource(id = R.string.repeat_label))
            }
        } else {
            PlaceholderElement(
                imageRes = null,
                textRes = uiState.errors!!.messageRes
            )
        }
    }
}

@Composable
fun Flashcard(
    text: String,
    height: Int = 156,
    onCardClick: () -> Unit
) {
    val modifiedText = if (text.length > 48) text.slice(0..47) else text

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(height.dp)
            .clip(RoundedCornerShape(basicMargin()))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable {
                onCardClick()
            }
    ) {
        Text(
            text = modifiedText,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun Answer(
    answer: String,
    onValueChange: (String) -> Unit,
    errors: ScreenErrors?
) {
    BasicTextFieldElement(
        value = answer,
        onValueChange = {
            onValueChange(it)
        },
        label = stringResource(id = R.string.answer_label),
        supportingText = stringResource(id = R.string.flashcard_hint),
        errorMessage = if (errors != null)
            stringResource(id = errors.messageRes)
        else
            null
    )
}