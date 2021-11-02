package com.karumi.ui.image

import com.karumi.core.ui.ViewModel
import com.karumi.core.ui.ViewModelState
import com.karumi.domain.model.SuperHero
import com.karumi.domain.usecase.GetSuperHeroByName
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class SuperHeroImageState(val superHero: SuperHero)


@HiltViewModel
class SuperHeroImageViewModel @Inject constructor(
  private val getSuperHeroByName: GetSuperHeroByName
): ViewModel<SuperHeroImageState, Nothing>() {

  lateinit var superHeroName: String

  override fun onCreate() {
    super.onCreate()
    fetchSuperHero()
  }

  private fun fetchSuperHero() = async {
    val superHero = await { getSuperHeroByName(superHeroName) }
    updateState(ViewModelState.Loaded(SuperHeroImageState(superHero)))
  }
}