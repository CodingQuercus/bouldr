package se.id21afm.umu.cs.bouldr

import android.app.Application
import androidx.room.Room
import se.id21afm.umu.cs.bouldr.database.PlannedRouteDatabase

/**
 * Responsible for initializing the database. Sets up a Room database
 * and provides access to the PlannedRouteDatabase
 */
class RouteApp : Application() {
    lateinit var database: PlannedRouteDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            PlannedRouteDatabase::class.java,
            "routes_db"
        ).fallbackToDestructiveMigration().build()
    }
}