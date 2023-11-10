package cz.mendelu.pef.flashyflashcards.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.spec.NavGraphSpec
import cz.mendelu.pef.flashyflashcards.ui.screens.NavGraphs

@Composable
fun DestinationsNavHostWrapper(
    navGraph: NavGraphSpec,
    navController: NavHostController = rememberNavController(),
) {
    DestinationsNavHost(
        navGraph = NavGraphs.root,
        navController = navController,
        // Start destination root cannot use the same route as the graph
        startRoute = if (navGraph == NavGraphs.root) navGraph.startRoute else navGraph
    )
}