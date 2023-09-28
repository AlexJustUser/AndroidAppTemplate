package com.softteco.template.ui.feature.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.R
import com.softteco.template.ui.components.CustomTopAppBar
import com.softteco.template.ui.components.TextSnackbarContainer

@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel = hiltViewModel(),
    onBackClicked: () -> Unit = {}
) {
    ScreenContent(onBackClicked)
}

@Composable
private fun ScreenContent(
    onBackClicked: () -> Unit = {}
) {
        Column(modifier = Modifier.fillMaxWidth()) {
            CustomTopAppBar(
                stringResource(id = R.string.details_view),
                showBackIcon = true,
                modifier = Modifier.fillMaxWidth(),
                onBackClicked = onBackClicked
            )
        }
}