package cz.mendelu.pef.flashyflashcards.ui.screens.explore

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.ramcosta.composedestinations.annotation.Destination
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.architecture.UiState
import cz.mendelu.pef.flashyflashcards.model.Business
import cz.mendelu.pef.flashyflashcards.model.DataSourceType
import cz.mendelu.pef.flashyflashcards.navigation.graphs.ExploreNavGraph
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicScaffold
import cz.mendelu.pef.flashyflashcards.ui.elements.HyperlinkText
import cz.mendelu.pef.flashyflashcards.ui.elements.PlaceholderElement
import cz.mendelu.pef.flashyflashcards.ui.theme.PinkPrimaryLight
import cz.mendelu.pef.flashyflashcards.ui.theme.basicMargin

@ExploreNavGraph
@Destination
@Composable
fun DetailScreen(
    navController: NavController,
    viewModel: DetailScreenViewModel = hiltViewModel(),
    dataSourceType: DataSourceType
) {

    LaunchedEffect(Unit) {
        viewModel.getBusiness(dataSourceType)
    }

    BasicScaffold(
        topAppBarTitle = stringResource(id = R.string.detail),
        onBackClick = { navController.popBackStack() }
    ) { paddingValues ->
        DetailScreenContent(
            paddingValues = paddingValues,
            uiState = viewModel.uiState
        )
    }
}

@Composable
fun DetailScreenContent(
    paddingValues: PaddingValues,
    uiState: UiState<Business?, ExploreErrors>
) {
    if (uiState.data != null) {
        val uriHandler = LocalUriHandler.current

        Column(
            verticalArrangement = Arrangement.spacedBy(basicMargin()),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = basicMargin())
        ) {
            DetailScreenGoogleMap(
                latitude = uiState.data!!.latitude,
                longitude = uiState.data!!.longitude
            )

            Text(
                text = uiState.data!!.name,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = uiState.data!!.displayAddress.joinToString(", ")
            )

            HyperlinkText(
                text = stringResource(id = R.string.yelp_link_text),
                uri = uiState.data!!.businessUrl,
                uriHandler = uriHandler
            )

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {
                    // TODO: Save to bookmarks and show a snackbar; change button label to remove (add remove function)
                    },
                    modifier = Modifier.padding(top = basicMargin())
                ) {
                    Text(text = stringResource(id = R.string.bookmark_label))
                }
            }
        }
    } else if (uiState.errors != null) {
        Column(modifier = Modifier.padding(paddingValues)) {
            PlaceholderElement(
                imageRes = uiState.errors!!.imageRes,
                textRes = uiState.errors!!.messageRes
            )
        }
    }
}

@Composable
fun DetailScreenGoogleMap(
    latitude: Double,
    longitude: Double,
    cameraZoom: Float = 18f
) {
    val location = LatLng(latitude, longitude)

    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = false,
                mapToolbarEnabled = false,
            )
        )
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            location,
            cameraZoom
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(450.dp)
            .padding(top = 25.dp)
            .padding(bottom = 5.dp)
    ) {
        GoogleMap(
            uiSettings = mapUiSettings,
            cameraPositionState = cameraPositionState,
            modifier = Modifier
                .fillMaxSize()
                .border(BorderStroke(1.dp, PinkPrimaryLight))
        ) {
            Marker(state = MarkerState(position = location))
        }
    }
}