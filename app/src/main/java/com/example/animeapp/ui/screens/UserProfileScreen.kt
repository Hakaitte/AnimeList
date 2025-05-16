package com.example.animeapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CancelPresentation
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.PauseCircleOutline
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.animeapp.AnimeListViewModel
import com.example.animeapp.data.UserAnimeStatus
import com.example.animeapp.ui.reusableComponents.BottomNavigationBar
import com.example.animeapp.ui.theme.AnimeAppTheme
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    navController: NavController,
    animeListViewModel: AnimeListViewModel = viewModel()
) {
    val watchingCount by animeListViewModel.getAnimeCountByStatus(UserAnimeStatus.WATCHING).collectAsStateWithLifecycle(initialValue = 0)
    val planningCount by animeListViewModel.getAnimeCountByStatus(UserAnimeStatus.PLAN_TO_WATCH).collectAsStateWithLifecycle(initialValue = 0)
    val watchedCount by animeListViewModel.getAnimeCountByStatus(UserAnimeStatus.COMPLETED).collectAsStateWithLifecycle(initialValue = 0)
    val onHoldCount by animeListViewModel.getAnimeCountByStatus(UserAnimeStatus.ON_HOLD).collectAsStateWithLifecycle(initialValue = 0)
    val droppedCount by animeListViewModel.getAnimeCountByStatus(UserAnimeStatus.DROPPED).collectAsStateWithLifecycle(initialValue = 0)

    val currentRoute = navController.currentDestination?.route

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mój Profil Anime") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(navController, currentRoute)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()), // Jeśli zawartość będzie większa
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Sekcja informacji o użytkowniku (placeholder)
            // CircleShapeAvatarPlaceholder() // Możesz stworzyć taki Composable
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Hakaitte", // Zastąp rzeczywistą nazwą
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Statystyki: $watchedCount obejrzanych, $planningCount w planach", // Zastąp rzeczywistymi statystykami
                style = MaterialTheme.typography.bodyMedium
            )

            Divider(modifier = Modifier.padding(vertical = 24.dp))

            Text(
                text = "Moje Listy Anime",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Przyciski do list
            UserListButton(
                text = "Oglądam",
                icon = Icons.Filled.PlayCircleOutline,
                // count = watchingCount, // Odkomentuj, gdy będziesz miał liczniki
                onClick = {
                    // navController.navigate("anime_list_screen/${UserAnimeStatus.WATCHING.name}")
                    // TODO: Zaimplementuj nawigację do ekranu listy "Oglądam"
                    println("Przejdź do listy: Oglądam")
                }
            )

            UserListButton(
                text = "Planuję obejrzeć",
                icon = Icons.Filled.PlaylistPlay,
                // count = planningCount,
                onClick = {
                    // navController.navigate("anime_list_screen/${UserAnimeStatus.PLAN_TO_WATCH.name}")
                    // TODO: Zaimplementuj nawigację do ekranu listy "Planuję"
                    println("Przejdź do listy: Planuję obejrzeć")
                }
            )

            UserListButton(
                text = "Obejrzane",
                icon = Icons.Filled.CheckCircleOutline,
                onClick = {
                    // navController.navigate("anime_list_screen/${UserAnimeStatus.COMPLETED.name}")
                    // TODO: Zaimplementuj nawigację do ekranu listy "Obejrzane"
                    println("Przejdź do listy: Obejrzane")
                }
            )

            UserListButton(
                text = "Wstrzymane",
                icon = Icons.Filled.PauseCircleOutline,
                onClick = {
                    // navController.navigate("anime_list_screen/${UserAnimeStatus.ON_HOLD.name}")
                    // TODO: Zaimplementuj nawigację do ekranu listy "Wstrzymane"
                    println("Przejdź do listy: Wstrzymane")
                }
            )

            UserListButton(
                text = "Porzucone",
                icon = Icons.Filled.CancelPresentation, // lub Icons.Filled.DoNotDisturbOn
                onClick = {
                    // navController.navigate("anime_list_screen/${UserAnimeStatus.DROPPED.name}")
                    // TODO: Zaimplementuj nawigację do ekranu listy "Porzucone"
                    println("Przejdź do listy: Porzucone")
                }
            )
        }
    }
}

@Composable
private fun UserListButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    count: Int? = null // Opcjonalny licznik
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(text, fontSize = 16.sp, modifier = Modifier.weight(1f))
        count?.let {
            Text("($it)", fontSize = 14.sp, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f))
        }
    }
}

// --- Preview ---
@Preview(showBackground = true, widthDp = 360)
@Composable
fun UserProfileScreenPreview() {
    AnimeAppTheme {
        // W podglądzie nie mamy rzeczywistego NavControllera ani ViewModelu
        // Możemy przekazać mocki lub pominąć, jeśli logika nie jest kluczowa dla wyglądu
        UserProfileScreen(
            navController = rememberNavController(),
            animeListViewModel = viewModel()
        )
    }
}