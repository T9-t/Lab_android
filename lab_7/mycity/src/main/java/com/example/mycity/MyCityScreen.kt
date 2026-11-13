package com.example.mycity

import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mycity.model.CategoryType
import com.example.mycity.model.Place
import com.example.mycity.ui.MyCityUiState
import com.example.mycity.ui.MyCityViewModel

enum class MyCityScreen {
    Categories,
    PlacesList,
    PlaceDetail
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCityAppBar(
    currentScreen: MyCityScreen,
    uiState: MyCityUiState,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val titleText = when (currentScreen) {
        MyCityScreen.Categories -> stringResource(R.string.app_name)
        MyCityScreen.PlacesList -> stringResource(uiState.currentCategory.title)
        MyCityScreen.PlaceDetail -> stringResource(uiState.selectedPlace.title)
    }
    TopAppBar(
        title = { Text(titleText) },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCityApp(
    windowSize: WindowWidthSizeClass,
    viewModel: MyCityViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isExpandedScreen = windowSize == WindowWidthSizeClass.Expanded

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = MyCityScreen.valueOf(
        backStackEntry?.destination?.route ?: MyCityScreen.Categories.name
    )
    Scaffold(
        topBar = {
            MyCityAppBar(
                currentScreen = currentScreen,
                uiState = uiState,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        if (isExpandedScreen) {
            ExpandedCityLayout(
                uiState = uiState,
                onCategoryClick = { viewModel.selectCategory(it) },
                onPlaceClick = { viewModel.selectPlace(it) },
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            NavHost(
                navController = navController,
                startDestination = MyCityScreen.Categories.name,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(MyCityScreen.Categories.name) {
                    CategoryListScreen(
                        categories = CategoryType.entries,
                        onCategoryClick = { category ->
                            viewModel.selectCategory(category)
                            navController.navigate(MyCityScreen.PlacesList.name)
                        }
                    )
                }
                composable(MyCityScreen.PlacesList.name) {
                    PlaceListScreen(
                        places = uiState.currentPlacesList,
                        onPlaceClick = { place ->
                            viewModel.selectPlace(place)
                            navController.navigate(MyCityScreen.PlaceDetail.name)
                        }
                    )
                }
                composable(MyCityScreen.PlaceDetail.name) {
                    PlaceDetailScreen(place = uiState.selectedPlace)
                }
            }
        }
    }
}

@Composable
fun CategoryListScreen(
    categories: List<CategoryType>,
    onCategoryClick: (CategoryType) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories) { category ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCategoryClick(category) },
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(category.image),
                        contentDescription = null,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = stringResource(category.title),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}


@Composable
fun PlaceListScreen(
    places: List<Place>,
    onPlaceClick: (Place) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(places) { place ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPlaceClick(place) },
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(place.image),
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = stringResource(place.title),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Composable
fun PlaceDetailScreen(
    place: Place,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(place.image),
            contentDescription = stringResource(place.title),
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(place.title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Адрес: ${stringResource(place.address)}",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(place.description),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun ExpandedCityLayout(
    uiState: MyCityUiState,
    onCategoryClick: (CategoryType) -> Unit,
    onPlaceClick: (Place) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxSize()) {
        CategoryListScreen(
            categories = CategoryType.entries,
            onCategoryClick = onCategoryClick,
            modifier = Modifier.weight(1f)
        )
        PlaceListScreen(
            places = uiState.currentPlacesList,
            onPlaceClick = onPlaceClick,
            modifier = Modifier.weight(1.2f)
        )
        PlaceDetailScreen(
            place = uiState.selectedPlace,
            modifier = Modifier.weight(1.8f)
        )
    }
}