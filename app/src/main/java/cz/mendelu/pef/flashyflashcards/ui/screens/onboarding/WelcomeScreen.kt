package cz.mendelu.pef.flashyflashcards.ui.screens.onboarding

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.ui.screens.NavGraphs
import cz.mendelu.pef.flashyflashcards.ui.theme.Pink40
import cz.mendelu.pef.flashyflashcards.ui.theme.PinkPrimaryLight
import cz.mendelu.pef.flashyflashcards.ui.theme.basicMargin
import cz.mendelu.pef.flashyflashcards.ui.theme.extraMediumMargin
import cz.mendelu.pef.flashyflashcards.ui.theme.halfMargin
import cz.mendelu.pef.flashyflashcards.ui.theme.mediumMargin
import cz.mendelu.pef.flashyflashcards.ui.theme.smallMargin
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun WelcomeScreen(
    navController: NavController,
    viewModel: WelcomeScreenViewModel = hiltViewModel()
) {
    val pages = viewModel.getOnBoardingPages()
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.CenterVertically,
            userScrollEnabled = false
        ) {position ->
            OnBoardingPageContent(onBoardingPage = pages[position]) {
                scope.launch {
                    if (position != pages.size - 1) {
                        pagerState.animateScrollToPage(
                            page = position + 1,
                            animationSpec = tween(durationMillis = 550)
                        )
                    } else {
                        viewModel.setOnboardingFinished()
                        navController.navigate(NavGraphs.collections.startRoute.route)
                    }
                }
            }
        }

        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(top = basicMargin())
                .padding(bottom = halfMargin()),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) {
                    Pink40
                } else {
                    MaterialTheme.colorScheme.onBackground
                }

                Box(
                    modifier = Modifier
                        .padding(all = smallMargin())
                        .clip(CircleShape)
                        .background(color)
                        .size(halfMargin())
                )
            }
        }
    }
}

@Composable
fun OnBoardingPageContent(
    onBoardingPage: OnBoardingPage,
    onContinueClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(all = basicMargin()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(132.dp)
                .clip(CircleShape)
                .background(PinkPrimaryLight),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = onBoardingPage.icon),
                contentDescription = null,
                modifier = Modifier.size(onBoardingPage.iconSize)
            )
        }

        Text(
            text = stringResource(id = onBoardingPage.title),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .padding(top = extraMediumMargin())
                .padding(bottom = basicMargin())
        )

        Text(
            text = stringResource(id = onBoardingPage.body),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = mediumMargin())
        )

        Spacer(modifier = Modifier.height(extraMediumMargin()))
        
        Button(onClick = onContinueClick) {
            Text(stringResource(id = R.string.continue_label))
        }
    }
}