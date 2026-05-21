package se.id21afm.umu.cs.bouldr.factories

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import se.id21afm.umu.cs.bouldr.dao.PlannedRouteDao
import se.id21afm.umu.cs.bouldr.viewmodel.PlannedViewModel

/**
 * Factory class for creating instances of PlannedViewModel.
 * Necessary to make sure the viewmodel has access to the DAO
 * and saved state.
 */
class PlannedRouteViewModelFactory(
    private val dao: PlannedRouteDao,
    private val savedStateHandle: SavedStateHandle
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlannedViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlannedViewModel(dao, savedStateHandle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
