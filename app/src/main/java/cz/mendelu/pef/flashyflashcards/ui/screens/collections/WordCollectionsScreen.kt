package cz.mendelu.pef.flashyflashcards.ui.screens.collections

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.mlkit.nl.translate.TranslateLanguage
import com.ramcosta.composedestinations.annotation.Destination
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.model.UiState
import cz.mendelu.pef.flashyflashcards.model.WordCollection
import cz.mendelu.pef.flashyflashcards.navigation.bottombar.BottomBar
import cz.mendelu.pef.flashyflashcards.navigation.graphs.CollectionsNavGraph
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicScaffold
import cz.mendelu.pef.flashyflashcards.ui.screens.ScreenErrors
import cz.mendelu.pef.flashyflashcards.ui.theme.basicMargin

@CollectionsNavGraph(start = true)
@Destination
@Composable
fun WordCollectionsScreen(
    navController: NavController,
    viewModel: WordCollectionsViewModel = hiltViewModel()
) {
    BasicScaffold(
        topAppBarTitle = stringResource(id = R.string.collections),
        bottomAppBar = {
            BottomBar(navController = navController)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.createNewWordCollection(TranslateLanguage.CZECH, TranslateLanguage.ENGLISH)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_label)
                )
            }
        }
    ) { paddingValues ->
        WordCollectionsScreenContent(
            uiState = viewModel.uiState,
            paddingValues = paddingValues
        )
    }
}

@Composable
fun WordCollectionsScreenContent(
    uiState: UiState<MutableList<WordCollection>, ScreenErrors>,
    paddingValues: PaddingValues
) {
    if (uiState.data != null) {
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = basicMargin())
        ) {
            uiState.data!!.forEach {
                item {
                    Text(text = it.name)
                }
            }
        }
    }
}