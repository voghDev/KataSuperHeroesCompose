package com.karumi.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.karumi.ui.detail.SuperHeroDetailScreen
import com.karumi.ui.detail.SuperHeroDetailViewModel
import com.karumi.ui.image.SuperHeroImageScreen
import com.karumi.ui.image.SuperHeroImageViewModel
import com.karumi.ui.list.SuperHeroListScreen
import com.karumi.ui.list.SuperHeroListViewModel

sealed class Routes(val path: String) {
  object List : Routes("list")
  object Detail : Routes("detail/{superHeroId}") {
    const val superHeroIdArgName: String = "superHeroId"
    fun pathFor(superHeroId: String): String = "detail/$superHeroId"
  }

  object SuperHeroImage : Routes("detail/{superHeroId}/image") {
    const val superHeroIdArgName: String = "superHeroId"
    fun pathFor(superHeroId: String): String = "detail/$superHeroId/image"
  }
}

@Composable
fun SuperHeroApp() {
  val navController = rememberNavController()
  NavHost(navController = navController, startDestination = Routes.List.path) {
    SuperHeroListNavRoute(navController)
    SuperHeroDetailNavRoute(navController)
    SuperHeroImageNavRoute(navController)
  }
}

private fun NavGraphBuilder.SuperHeroListNavRoute(navController: NavHostController) {
  composable(Routes.List.path) {
    val viewModel: SuperHeroListViewModel = hiltViewModel()
    SuperHeroListScreen(viewModel) { superHero ->
      navController.navigate(Routes.Detail.pathFor(superHero.name))
    }
  }
}

private fun NavGraphBuilder.SuperHeroImageNavRoute(navController: NavHostController) {
  composable(
    Routes.SuperHeroImage.path,
    arguments = listOf(navArgument(Routes.SuperHeroImage.superHeroIdArgName) {
      type = NavType.StringType
    }
    )
  ) { backStackEntry ->
    val superHeroId =
      backStackEntry.arguments?.getString(Routes.SuperHeroImage.superHeroIdArgName) ?: ""
    val viewModel: SuperHeroImageViewModel = hiltViewModel()
    viewModel.superHeroName = superHeroId
    SuperHeroImageScreen(
      viewModel
    ) { navController.popBackStack() }
  }
}

private fun NavGraphBuilder.SuperHeroDetailNavRoute(navController: NavHostController) {
  composable(
    Routes.Detail.path,
    arguments = listOf(
      navArgument(Routes.Detail.superHeroIdArgName) {
        type = NavType.StringType
      }
    )
  ) { backStackEntry ->
    val superHeroId =
      backStackEntry.arguments?.getString(Routes.Detail.superHeroIdArgName) ?: ""
    val viewModel: SuperHeroDetailViewModel = hiltViewModel()
    viewModel.superHeroName = superHeroId
    SuperHeroDetailScreen(
      viewModel,
      onBackButtonTapped = { navController.popBackStack() },
      onSuperHeroImageTapped = { navController.navigate(Routes.SuperHeroImage.pathFor(superHeroId)) }
    )
  }
}
