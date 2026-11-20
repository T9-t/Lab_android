package com.example.rickandmorty

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.rickandmorty.network.Character
import com.example.rickandmorty.network.Episode
import com.example.rickandmorty.ui.CharacterDetailUiState
import com.example.rickandmorty.ui.CharacterDetailViewModel
import com.example.rickandmorty.ui.CharactersUiState
import com.example.rickandmorty.ui.CharactersViewModel

enum class CharactersScreen {
    CharactersList,
    SelectedCharacter
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersAppBar(
    currentScreen: CharactersScreen,
    characterName: String = "",
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val titleText = when (currentScreen) {
        CharactersScreen.CharactersList -> stringResource(R.string.app_name)
        CharactersScreen.SelectedCharacter -> characterName.ifEmpty { "Персонаж" }
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
fun CharactersApp(
    charactersViewModel: CharactersViewModel = viewModel(factory = CharactersViewModel.Factory),
    characterDetailViewModel: CharacterDetailViewModel = viewModel(factory = CharacterDetailViewModel.Factory),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = CharactersScreen.valueOf(
        backStackEntry?.destination?.route ?: CharactersScreen.CharactersList.name
    )
    val selectedCharacterName = (characterDetailViewModel.uiState as? CharacterDetailUiState.Success)
        ?.character
        ?.first
        ?.name
        .orEmpty()

    Scaffold(
        topBar = {
            CharactersAppBar(
                currentScreen = currentScreen,
                characterName = selectedCharacterName,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = CharactersScreen.CharactersList.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(CharactersScreen.CharactersList.name) {
                when (val uiState = charactersViewModel.uiState) {
                    is CharactersUiState.Loading -> {
                        NotSuccessScreen("Данные ещё не загружены")
                    }
                    is CharactersUiState.Error -> {
                        NotSuccessScreen("Ошибка загрузки данных")
                    }
                    is CharactersUiState.Success -> {
                        CharactersListScreen(
                            charactersList = uiState.characters,
                            selectSpecies = charactersViewModel.selectedSpecies,
                            onSpeciesSelect = { species -> charactersViewModel.selectSpecies(species) },
                            onCharacterClick = { id ->
                                characterDetailViewModel.selectCharacter(id)
                                navController.navigate(CharactersScreen.SelectedCharacter.name)
                            },
                            onLoadNextPage = { charactersViewModel.loadNextPage() },
                            isLoadingNextPage = uiState.isLoadingNextPage
                        )
                    }
                }
            }
            composable(CharactersScreen.SelectedCharacter.name) {
                when (val uiState = characterDetailViewModel.uiState) {
                    is CharacterDetailUiState.Loading -> {
                        NotSuccessScreen("Данные ещё не загружены")
                    }
                    is CharacterDetailUiState.Error -> {
                        NotSuccessScreen("Ошибка загрузки данных")
                    }
                    is CharacterDetailUiState.Success -> {
                        CharacterDetailContent(uiState.character)
                    }
                }
            }
        }
    }
}
@Composable
fun NotSuccessScreen(string: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = string,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun CharactersListScreen(
    charactersList: List<Character>,
    selectSpecies: String?,
    onSpeciesSelect: (String?) -> Unit,
    onCharacterClick: (Int) -> Unit,
    onLoadNextPage: () -> Unit,
    isLoadingNextPage: Boolean,
    modifier: Modifier = Modifier
) {
    val speciesOptions = listOf(
        null to "Все",
        "Human" to "Человек",
        "Alien" to "Пришелец",
        "Humanoid" to "Гуманоид",
        "Robot" to "Робот",
        "Animal" to "Животное",
        "Mythological Creature" to "Мифическое"
    )
    Column(modifier = modifier.fillMaxSize()) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(speciesOptions) { (speciesValue, label) ->
                FilterChip(
                    selected = selectSpecies == speciesValue,
                    onClick = { onSpeciesSelect(speciesValue) },
                    label = { Text(label) }
                )
            }
        }
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(charactersList) { index, character ->
                if (index >= charactersList.size - 3 && !isLoadingNextPage) {
                    LaunchedEffect(charactersList.size) {
                        onLoadNextPage()
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCharacterClick(character.id) },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(character.image)
                                .crossfade(true)
                                .build(),
                            contentDescription = character.name,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Column(
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text(
                                text = character.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = character.species,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            if (isLoadingNextPage) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
private fun CharacterDetailContent(
    character: Pair<Character, List<Episode>>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            AsyncImage(
                model = character.first.image,
                contentDescription = character.first.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = character.first.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Статус: ${character.first.status}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Вид: ${character.first.species}",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Пол: ${character.first.gender}",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Эпизоды в которых появлялся:",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        items(character.second) { episode ->
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${episode.id} — ${episode.name}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}