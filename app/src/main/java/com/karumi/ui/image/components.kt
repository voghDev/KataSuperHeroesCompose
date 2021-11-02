package com.karumi.ui.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.rememberCoilPainter
import com.karumi.core.ui.LinkViewModelLifecycle
import com.karumi.core.ui.SuperHeroTopBar
import com.karumi.core.ui.ViewModelState
import com.karumi.domain.model.SuperHero

@Composable
fun SuperHeroImageScreen(
  viewModel: SuperHeroImageViewModel,
  onBackButtonTapped: () -> Unit
) {
  LinkViewModelLifecycle(viewModel)
  val state by viewModel.state.collectAsState(ViewModelState.Loading())
  Scaffold(
    backgroundColor = MaterialTheme.colors.background,
    topBar = {
      SuperHeroTopBar(
        title = viewModel.superHeroName,
        onBackButtonTapped = onBackButtonTapped
      )
    },
    content = {
      when (val currentState = state) {
        is ViewModelState.Loaded -> SuperHeroImageLoadedScreen(
          currentState.content.superHero
        )
        else -> SuperHeroImageLoadingScreen()
      }
    }
  )
}

@Composable
fun SuperHeroImageLoadedScreen(superHero: SuperHero) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .height(300.dp)
  ) {
    Image(
      modifier = Modifier.fillMaxSize(),
      contentScale = ContentScale.Crop,
      painter = rememberCoilPainter(superHero.photo),
      contentDescription = superHero.name,
    )
  }
}

@Composable
private fun SuperHeroImageLoadingScreen() {
  Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    CircularProgressIndicator(color = MaterialTheme.colors.secondary)
  }
}
