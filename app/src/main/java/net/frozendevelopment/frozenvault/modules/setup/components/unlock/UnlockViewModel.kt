package net.frozendevelopment.frozenvault.modules.setup.components.unlock

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.frozendevelopment.frozenvault.R
import net.frozendevelopment.frozenvault.AppSession
import net.frozendevelopment.frozenvault.data.daos.RecoveryKeyDao
import net.frozendevelopment.frozenvault.infrustructure.StatefulViewModel
import net.frozendevelopment.frozenvault.modules.setup.SetupFragmentDirections

class UnlockViewModel(
    private val appSession: AppSession,
    private val recoveryKeyDao: RecoveryKeyDao
) : StatefulViewModel<UnlockState>() {

    override fun getDefaultState(): UnlockState = UnlockState()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun checkForRecoveryKeys() = viewModelScope.launch(Dispatchers.IO) {
        state = state.copy(hasRecoveryKeys = recoveryKeyDao.getUnusedKeyCount() > 0)
    }

    private fun validate() : Boolean {
        if (state.password.isNullOrBlank()) {
            state = state.copy(errorMessageResource = R.string.field_required)
            return false
        }

        state = state.copy(errorMessageResource = null)
        return true
    }

    fun attemptUnlock(navController: NavController) = viewModelScope.launch(Dispatchers.IO) {
        if (!validate()) return@launch

        if (appSession.attemptUnlock(state.password!!)) {
            launch(Dispatchers.Main) {
                navController.navigate(SetupFragmentDirections.actionSetupFragmentToPasswordListFragment())
            }
        } else {
            state = state.copy(errorMessageResource = R.string.invalid_password)
        }
    }

}
