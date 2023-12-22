package cz.mendelu.pef.flashyflashcards.ui.screens.collections.flashcards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.model.FlashcardPracticeType
import cz.mendelu.pef.flashyflashcards.model.TestHistory
import cz.mendelu.pef.flashyflashcards.model.UiState
import cz.mendelu.pef.flashyflashcards.navigation.graphs.CollectionsNavGraph
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicScaffold
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicTextFieldElement
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicTimer
import cz.mendelu.pef.flashyflashcards.ui.elements.Flashcard
import cz.mendelu.pef.flashyflashcards.ui.elements.PlaceholderElement
import cz.mendelu.pef.flashyflashcards.ui.screens.ScreenErrors
import cz.mendelu.pef.flashyflashcards.ui.theme.basicMargin
import cz.mendelu.pef.flashyflashcards.ui.theme.mediumMargin
import cz.mendelu.pef.flashyflashcards.ui.theme.smallMargin

@CollectionsNavGraph
@Destination
@Composable
fun FlashcardPracticeScreen(
    navController: NavController,
    viewModel: FlashcardPracticeScreenViewModel = hiltViewModel(),
    collectionId: Long?,
    flashcardPracticeType: FlashcardPracticeType
) {
    LaunchedEffect(Unit) {
        if (viewModel.uiState.data == null) {
            viewModel.getAllCollectionWords(collectionId)
        }

        if (flashcardPracticeType is FlashcardPracticeType.Training) {
            viewModel.turnOffTestHistory()
        }
    }

    val timer = if (flashcardPracticeType is FlashcardPracticeType.Test) {
        flashcardPracticeType.initialTimer
    } else {
        null
    }

    BasicScaffold(
        topAppBarTitle = stringResource(id = flashcardPracticeType.name),
        showLoading = viewModel.uiState.loading,
        onBackClick = { navController.popBackStack() }
    ) { paddingValues ->
        FlashcardPracticeScreenContent(
            paddingValues = paddingValues,
            uiState = viewModel.uiState,
            actions = viewModel,
            initialTestTimer = timer
        )
    }
}

@Composable
fun FlashcardPracticeScreenContent(
    paddingValues: PaddingValues,
    uiState: UiState<FlashcardPracticeScreenData, ScreenErrors>,
    actions: FlashcardPracticeScreenActions,
    initialTestTimer: Long?
) {
    var testTimer by remember {
        mutableStateOf(initialTestTimer ?: 0L)
    }
    // This is needed! Without this variable, testTimer may be not
    // reset to initial value (very rarely, but could happen)
    var testTimerRefresh by remember {
        mutableStateOf(false)
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
        if (uiState.data != null && uiState.data?.finish == false) {
            val answer = uiState.data!!.answer
            val flashcardText = uiState.data!!.flashcardText

            // If initialTestTimer is present, user's taking a collection test
            if (initialTestTimer != null) {
                val currentWordNumber = uiState.data!!.currentWordNumber + 1
                val totalWordsNumber = uiState.data!!.words.size

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "${currentWordNumber}/${totalWordsNumber}",
                        modifier = Modifier.weight(0.5f),
                        style = MaterialTheme.typography.titleMedium,
                    )

                    BasicTimer(
                        totalTimeInMillis = testTimer,
                        refresh = testTimerRefresh,
                        modifier = Modifier.weight(0.5f)
                    ) {
                        if (!testTimerRefresh) {
                            testTimer = it - 100L
                        } else {
                            testTimerRefresh = false
                        }
                    }
                }

                Practice(
                    flashcardText = flashcardText,
                    onFlashcardClick = null,
                    answer = answer,
                    answerFieldEnabled = testTimer > 0,
                    answerSupportingText = null,
                    answerErrorMessage = null,
                    onAnswerChange = { actions.setAnswer(it) },
                    actionButtonLabel = stringResource(id = R.string.next_label)
                ) {
                    val timeTaken = initialTestTimer - testTimer

                    testTimer = initialTestTimer
                    testTimerRefresh = true

                    actions.setNextWord()
                    actions.updateTimeTakenInTestHistory(timeTaken)
                }
            } else {
                Practice(
                    flashcardText = flashcardText,
                    onFlashcardClick = { actions.setFlashcardText() },
                    answer = answer,
                    answerSupportingText = stringResource(id = R.string.flashcard_hint),
                    answerErrorMessage = if (uiState.errors != null)
                        stringResource(id = uiState.errors!!.messageRes)
                    else
                        null,
                    onAnswerChange = { actions.setAnswer(it) },
                    actionButtonLabel = stringResource(id = R.string.next_label)
                ) {
                    // User should answer correctly
                    if (actions.isAnswerCorrect(answer)) {
                        actions.setNextWord()
                    }
                }
            }
        } else if (uiState.errors == null) {
            val testHistory = actions.getTestHistory()

            PracticeResult(
                title = if (testHistory != null)
                    stringResource(id = R.string.test_finished)
                else
                    stringResource(id = R.string.training_finished)
                ,
                actions = actions,
                testHistory = testHistory
            )
        } else {
            PlaceholderElement(
                imageRes = null,
                textRes = uiState.errors!!.messageRes
            )
        }
    }
}

@Composable
fun Practice(
    flashcardText: String,
    onFlashcardClick: (() -> Unit)?,
    answer: String,
    answerFieldEnabled: Boolean = true,
    answerSupportingText: String?,
    answerErrorMessage: String?,
    onAnswerChange: (String) -> Unit,
    actionButtonLabel: String,
    onActionButtonClick: () -> Unit
) {
    Flashcard(text = flashcardText) {
        if (onFlashcardClick != null) {
            onFlashcardClick()
        }
    }

    Spacer(modifier = Modifier.height(smallMargin()))

    BasicTextFieldElement(
        value = answer,
        onValueChange = onAnswerChange,
        label = stringResource(id = R.string.answer_label),
        supportingText = answerSupportingText,
        enabled = answerFieldEnabled,
        errorMessage = answerErrorMessage
    )

    ElevatedButton(onClick = {
        onActionButtonClick()
    }) {
        Text(text = actionButtonLabel)
    }
}

@Composable
fun PracticeResult(
    title: String,
    actions: FlashcardPracticeScreenActions,
    testHistory: TestHistory?
) {
    Text(
        color = MaterialTheme.colorScheme.primary,
        text = title.uppercase(),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(basicMargin()),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = mediumMargin())
    ) {
        Button(
            onClick = {
                actions.resetFlashcard()
            },
            modifier = Modifier.weight(1f)
        ) {
            Text(text = stringResource(id = R.string.repeat_label))
        }

        if (testHistory != null) {
            Button(
                onClick = {
                    actions.saveTestHistory()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(id = R.string.save_test_summary_label))
            }
        }
    }

    if (testHistory != null) {
        TestSummary(testHistory = testHistory)
    }
}

@Composable
fun TestSummary(
    testHistory: TestHistory
) {
    Column {
        Text(text = "Time taken ${testHistory.timeTaken / 1000L}")

        LazyColumn(modifier = Modifier.height(128.dp)) {
            testHistory.answers.forEach { answer ->
                item {
                    Column {
                        Text(text = "Word ${answer.word.name}")
                        Text(text = "Word ${answer.word.translation}")
                        Text(text = "Word ${answer.answer}")
                    }
                }
            }
        }
    }
}