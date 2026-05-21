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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import se.id21afm.umu.cs.bouldr.model.PlannedRoute
import se.id21afm.umu.cs.bouldr.ui.components.TopBar
import se.id21afm.umu.cs.bouldr.viewmodel.PlannedViewModel

/**
 * Composable function that displays the home screen containing statistics
 * about the user's routes and a list of recent activity.
 *
 * @param navController navController used to handle navigation between screens
 * @param viewModel the ViewModel managing the planned routes data
 */
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: PlannedViewModel
) {
    val completedCount by viewModel.completedBouldersCount.collectAsState()
    val plannedCount by viewModel.plannedBouldersCount.collectAsState()
    val hardestGrade by viewModel.hardestGrade.collectAsState()
    val recentRoutes by viewModel.recentRoutes.collectAsState()

    Scaffold(
        topBar = { TopBar("BouldR") },
        bottomBar = { NavigationBar(navController) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 14.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            StatCardLarge(
                label = "Avklarade boulder",
                value = completedCount.toString(),
                sub = if (completedCount == 0) "Inga avklarade än" else "Fortsätt klättra!"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCardSmall(
                    label = "Planerade",
                    value = plannedCount.toString(),
                    modifier = Modifier.weight(1f)
                )
                StatCardSmall(
                    label = "Svåraste",
                    value = hardestGrade.ifEmpty { "–" },
                    modifier = Modifier.weight(1f)
                )
            }

            if (recentRoutes.isNotEmpty()) {
                Text(
                    text = "SENASTE AKTIVITET",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
                recentRoutes.forEach { route ->
                    RecentRouteCard(
                        route = route,
                        onClick = { navController.navigate("route_detail/${route.id}") }
                    )
                }
            } else {
                EmptyStateCard()
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

/**
 * Composable function that displays a clickable card for a recent route,
 * showing a thumbnail, name/grade and date.
 *
 * @param route the route to display
 * @param onClick function to call when the card is clicked
 */
@Composable
fun RecentRouteCard(route: PlannedRoute, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
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
                    text = if (route.name.isNotBlank()) route.name
                    else route.grade.ifBlank { "Namnlös rutt" },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
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

/**
 * Composable function that displays a badge indicating whether a route
 * is completed or planned.
 *
 * @param completed true if the route is completed, false if planned
 */
@Composable
fun StatusBadge(completed: Boolean) {
    val bgColor = if (completed)
        MaterialTheme.colorScheme.primaryContainer
    else
        MaterialTheme.colorScheme.secondaryContainer

    val textColor = if (completed)
        MaterialTheme.colorScheme.onPrimaryContainer
    else
        MaterialTheme.colorScheme.onSecondaryContainer

    val label = if (completed) "Klar" else "Planerad"

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

/**
 * Composable function that displays a large statistics card with a label,
 * value and subtitle.
 *
 * @param label the descriptive label shown above the value
 * @param value the main statistic value to display
 * @param sub the subtitle shown below the value
 */
@Composable
fun StatCardLarge(label: String, value: String, sub: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = sub,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Composable function that displays a small statistics card with a label and value.
 *
 * @param label the descriptive label shown above the value
 * @param value the statistic value to display
 * @param modifier modifier to be applied to the card
 */
@Composable
fun StatCardSmall(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

/**
* Composable function that displays an empty state card with a message
* encouraging the user to add their first route.
*/
@Composable
fun EmptyStateCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Inga rutter ännu",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Lägg till din första rutt och börja spåra din klättring!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}