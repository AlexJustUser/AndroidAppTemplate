package com.softteco.template.ui.feature.chart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.R
import com.softteco.template.ui.components.CustomTopAppBar
import com.softteco.template.ui.theme.Dimens

@Composable
fun ChartScreen(
    viewModel: ChartViewModel = hiltViewModel(),
    onBackClicked: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    ScreenContent(state, onBackClicked)
}

@Composable
private fun ScreenContent(
    state: ChartViewModel.State,
    onBackClicked: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        CustomTopAppBar(
            stringResource(id = R.string.chart),
            showBackIcon = false,
            modifier = Modifier.fillMaxWidth()
        )

    }
}

@Composable
fun Charts(
    state: ChartViewModel.State
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(Dimens.PaddingDefault)
    ) {
        state.dataLYWSD03MMC.let {

        }
    }
}