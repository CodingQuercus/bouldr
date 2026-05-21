package se.id21afm.umu.cs.bouldr.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import se.id21afm.umu.cs.bouldr.dao.PlannedRouteDao
import se.id21afm.umu.cs.bouldr.model.PlannedRoute

/**
 * Viewmodel responsible for managing data related to the planned routes.
 * Interacts with DAO to perform operations on the database.
 * Manages the state of UI using SavedStateHandle to store temporary UI data,
 * which allow data to persist if the configuration changes.
 */
class PlannedViewModel(
    private val dao: PlannedRouteDao,
    private val state: SavedStateHandle
) : ViewModel() {

    /**
     * A list of all planned routes.
     * Uses flow to make sure UI is always up to date-
     */
    val plannedRoutes: StateFlow<List<PlannedRoute>> = dao.getAllRoutes()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    /**
     * Count of completed boulders.
     * Uses flow to make sure UI is always up to date-
     */
    val completedBouldersCount: StateFlow<Int> = plannedRoutes
        .map { notes -> notes.count { it.markedAsCompleted } }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)


    /**
     * Counts the numbers of planned boulders.
     */
    val plannedBouldersCount: StateFlow<Int> = plannedRoutes
        .map { routes -> routes.count { !it.markedAsCompleted } }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    /**
     * Gets the 3 most recent routes completed.
     */
    val recentRoutes: StateFlow<List<PlannedRoute>> = plannedRoutes
        .map { routes -> routes.sortedByDescending { it.date }.take(3) }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    /**
     * Updates a route
     *
     * @param routeToUpdate the route to be updated
     */
    fun updateRoute(routeToUpdate: PlannedRoute) {
        viewModelScope.launch {
            dao.updateRoute(routeToUpdate)
        }
    }

    /**
     * Inserts a route
     *
     * @param routeToInsert the route to be inserted
     */
    fun insertRoute(routeToInsert: PlannedRoute) {
        viewModelScope.launch {
            dao.insertRoute(routeToInsert)
        }
    }

    /**
     * Deletes a route
     *
     * @param routeToDelete the route to be deleted
     */
    fun deleteRoute(routeToDelete: PlannedRoute) {
        viewModelScope.launch {
            dao.deleteRoute(routeToDelete)
        }
    }

    /**
     * Clears all routes from the database
     */
    fun clearAllRoutes() {
        viewModelScope.launch {
            dao.clearAllRoutes()
        }
    }

    /**
     * Gets the hardest completed grade.
     */
    val hardestGrade: StateFlow<String> = plannedRoutes
        .map { routes ->
            routes
                .filter { it.markedAsCompleted }
                .mapNotNull { it.grade.ifBlank { null } }
                .maxOrNull() ?: ""
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, "")

}
