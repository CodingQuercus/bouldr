package se.id21afm.umu.cs.bouldr.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import se.id21afm.umu.cs.bouldr.model.PlannedRoute

/**
 * DAO for interacting with PlannedRoute entities in the Room database.
 * Includes methods to perform CRUD operations.
 */
@Dao
interface PlannedRouteDao {
    /**
     * Inserts a new route into database
     *
     * @param plannedRoute the route to be inserted
     */
    @Insert
    suspend fun insertRoute(plannedRoute: PlannedRoute)

    /**
     * Updates an existing route in the database
     *
     * @param plannedRoute the route to be updated
     */
    @Update
    suspend fun updateRoute(plannedRoute: PlannedRoute)

    /**
     * Deletes an existing route in the database
     *
     * @param plannedRoute the route to be deleted
     */
    @Delete
    suspend fun deleteRoute(plannedRoute: PlannedRoute)

    /**
     * Deletes all routes
     */
    @Query("DELETE FROM routes")
    suspend fun clearAllRoutes()

    /**
     * Gets all routes from the database
     */
    @Query("SELECT * FROM routes")
    fun getAllRoutes(): Flow<List<PlannedRoute>>
}