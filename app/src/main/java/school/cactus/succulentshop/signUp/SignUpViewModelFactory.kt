package school.cactus.succulentshop.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import school.cactus.succulentshop.auth.JwtStore

@Suppress("UNCHECKED_CAST")
class SignUpViewModelFactory(private val store: JwtStore) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = SignUpViewModel(store) as T
}