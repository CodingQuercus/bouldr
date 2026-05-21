package se.id21afm.umu.cs.bouldr.ui.screens

import NavigationBar
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import se.id21afm.umu.cs.bouldr.model.PlannedRoute
import se.id21afm.umu.cs.bouldr.ui.components.SectionLabel
import se.id21afm.umu.cs.bouldr.ui.components.TopBar
import se.id21afm.umu.cs.bouldr.viewmodel.PlannedViewModel

/**
 * Composable function that displays all routes split into two sections:
 * planned and completed. Each route is clickable and navigates to
 * the route detail screen.
 *
 * @param navController navController used to handle navigation between screens
 * @param viewModel the ViewModel managing the planned routes data
 */
@Composable
fun PlannedScreen(
    navController: NavController,
    viewModel: PlannedViewModel
) {
    val allRoutes by viewModel.plannedRoutes.collectAsState()

    val planned = allRoutes.filter { !it.markedAsCompleted }
    val completed = allRoutes.filter { it.markedAsCompleted }

    Scaffold(
        topBar = { TopBar("Dina rutter", "${planned.size} planerade") },
        bottomBar = { NavigationBar(navController) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {
            item {
                SectionLabel("Planerade")
            }

            if (planned.isEmpty()) {
                item {
                    Text(
                        text = "Inga planerade rutter ännu.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            } else {
                items(planned) { route ->
                    RouteListCard(
                        route = route,
                        onClick = { navController.navigate("route_detail/${route.id}") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            item {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp),
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.outline
                )
                SectionLabel("Avklarade")
            }

            if (completed.isEmpty()) {
                item {
                    Text(
                        text = "Inga avklarade rutter ännu.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            } else {
                items(completed) { route ->
                    RouteListCard(
                        route = route,
                        onClick = { navController.navigate("route_detail/${route.id}") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
}

/**
 * Composable function that displays a clickable card for a route,
 * showing a thumbnail, grade, name and date.
 *
 * @param route the route to display
 * @param onClick function to call when the card is clicked
 */
@Composable
fun RouteListCard(route: PlannedRoute, onClick: () -> Unit) {
    val title = buildString {
        if (route.grade.isNotBlank()) append("${route.grade} – ")
        append(route.name.ifBlank {
            if (route.notes.isNotBlank())
                route.notes.lines().first()
            else "Namnlös rutt"
        })

    }

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (route.imageUri.isNotBlank()) {
                AsyncImage(
                    model = route.imageUri,
                    contentDescription = "Ruttbild",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Image,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = route.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            StatusBadge(completed = route.markedAsCompleted)
        }
    }
}