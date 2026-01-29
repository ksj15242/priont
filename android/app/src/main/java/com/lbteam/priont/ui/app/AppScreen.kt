package com.lbteam.priont.ui.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lbteam.priont.R
import com.lbteam.priont.ui.category.CategoryScreen
import com.lbteam.priont.ui.category.CategoryViewModel
import com.lbteam.priont.ui.home.HomeScreen
import com.lbteam.priont.ui.home.HomeViewModel
import com.lbteam.priont.ui.navigation.AppRoute
import com.lbteam.priont.ui.product.ProductScreen
import com.lbteam.priont.ui.product.ProductViewModel
import com.lbteam.priont.ui.search.SearchScreen
import com.lbteam.priont.ui.search.SearchViewModel
import com.lbteam.priont.ui.theme.PriontTheme

@Composable
@Preview(showBackground = true)
private fun AppLoadingScreenPreview() {
    PriontTheme {
        AppLoadingScreen()
    }
}

@Composable
fun AppLoadingScreen(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(48.dp),
            color = MaterialTheme.colorScheme.onPrimary,
            trackColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
        )
        Text(
            text = stringResource(R.string.app_loading_msg),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
        )

    }
}

@Composable
@Preview(showBackground = true)
private fun AppErrorScreenPreview() {
    PriontTheme {
        AppErrorScreen(
            onRetry = { TODO() },
            onExit = { TODO() }
        )
    }
}

@Composable
fun AppErrorScreen(
    onRetry: () -> Unit,
    onExit: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { },
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Text(
                text = stringResource(R.string.dialog_error_title),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Text(
                // TODO("Error에 따른 매칭 text 받아서 보여주기")
                text = "알 수 없는 오류가 발생했습니다.\n다시 시도해주세요",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        confirmButton = {
            TextButton(onClick = onRetry) {
                Text(
                    text = stringResource(R.string.dialog_error_retry),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onExit) {
                Text(
                    text = stringResource(R.string.dialog_error_exit),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}

@Composable
fun PriontApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoute.Home.route,
        modifier = Modifier
    ) {
        composable(route = AppRoute.Home.route) {
            val viewModel: HomeViewModel = hiltViewModel()
            val uiState by viewModel.homeUiState.collectAsStateWithLifecycle()

            HomeScreen(
                uiState = uiState,
                onRankingTabSelected = { index ->
                    viewModel.onRankingTabSelected(index)
                },
                onSearchClicked = {
                    navController.navigate(AppRoute.Search.route)
                },
                onCategoryClicked = { id ->
                    navController.navigate(AppRoute.Category.createRoute(id))

                },
                onProductClicked = { id ->
                    navController.navigate(AppRoute.Product.createRoute(id))
                }
            )
        }

        composable(route = AppRoute.Search.route) {
            val viewModel: SearchViewModel = hiltViewModel()
            val uiState by viewModel.searchUiState.collectAsStateWithLifecycle()

            SearchScreen(
                uiState = uiState,
                onQueryChanged = { viewModel.onQueryChange(it) },
                onBack = {
                    navController.popBackStack()
                },
                onProductClicked = { id ->
                    navController.navigate(AppRoute.Product.createRoute(id))
                }
            )
        }

        composable(
            route = AppRoute.Category.route,
            arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
        ) {
            val viewModel: CategoryViewModel = hiltViewModel()
            val uiState by viewModel.categoryUiState.collectAsStateWithLifecycle()

            CategoryScreen(
                uiState = uiState,
                onBack = {
                    navController.popBackStack()
                },
                onProductClicked = { id ->
                    navController.navigate(AppRoute.Product.createRoute(id))
                }
            )
        }

        composable(
            route = AppRoute.Product.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) {

            val viewModel: ProductViewModel = hiltViewModel()
            val uiState by viewModel.productUiState.collectAsStateWithLifecycle()

            ProductScreen(
                uiState = uiState,
                onLineChartTabSelected = { index ->
                    viewModel.onLineChartTabSelected(index)
                },
                onBack = {
                    navController.popBackStack()
                },
                onRegionSelected = {
                    viewModel.selectRegion(it)
                }
            )
        }
    }
}