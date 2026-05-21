package se.id21afm.umu.cs.bouldr.database

import androidx.room.Database
import androidx.room.RoomDatabase
import se.id21afm.umu.cs.bouldr.dao.PlannedRouteDao
import se.id21afm.umu.cs.bouldr.model.PlannedRoute

/**
 * Database class that provides access to PlannedRouteDAO.
 */
@Database(entities = [PlannedRoute::class], version = 5, exportSchema = false)
abstract class PlannedRouteDatabase : RoomDatabase() {
    abstract fun plannedRouteDao(): PlannedRouteDao
}