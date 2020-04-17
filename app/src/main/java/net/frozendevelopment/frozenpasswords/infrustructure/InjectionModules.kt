package net.frozendevelopment.frozenpasswords.infrustructure

import android.content.Context
import net.frozendevelopment.frozenpasswords.AppSession
import net.frozendevelopment.frozenpasswords.data.AppDatabase
import net.frozendevelopment.frozenpasswords.modules.changepassword.ChangePasswordViewModel
import net.frozendevelopment.frozenpasswords.modules.passwords.editable.EditPasswordViewModel
import net.frozendevelopment.frozenpasswords.modules.passwords.editable.WorkingMode
import net.frozendevelopment.frozenpasswords.modules.passwords.list.PasswordListViewModel
import net.frozendevelopment.frozenpasswords.modules.settings.SettingsViewModel
import net.frozendevelopment.frozenpasswords.modules.unlock.UnlockViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val databaseModule = module {
    factory { AppDatabase.getDatabase(androidContext()).servicePasswordDao() }
    factory { AppDatabase.getDatabase(androidContext()).userDao() }
    single { AppThemeService(androidContext().getSharedPreferences("FrozenPasswordsPrefs", Context.MODE_PRIVATE)) }
}

val appModule = module {
    single { AppSession() }
}

val viewModelsModule = module {
    viewModel { PasswordListViewModel(get(), get()) }
    viewModel { (workingMode: WorkingMode) -> EditPasswordViewModel(workingMode, get(), get()) }
    viewModel { UnlockViewModel(get(), get()) }
    viewModel { SettingsViewModel(get(), get(), get()) }
    viewModel { ChangePasswordViewModel(get(), get(), get()) }
}
