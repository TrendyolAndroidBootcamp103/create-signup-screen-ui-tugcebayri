package school.cactus.succulentshop.signUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar.LENGTH_INDEFINITE
import school.cactus.succulentshop.R
import school.cactus.succulentshop.auth.JwtStore
import school.cactus.succulentshop.infra.BaseViewModel
import school.cactus.succulentshop.infra.snackbar.SnackbarAction
import school.cactus.succulentshop.infra.snackbar.SnackbarState
import school.cactus.succulentshop.signUp.SignUpRepository.RegisterRequestCallback
import school.cactus.succulentshop.signUp.validation.MailValidator
import school.cactus.succulentshop.signUp.validation.UsernameValidator

class SignUpViewModel(
    private val store: JwtStore,
    private val repository: SignUpRepository
) : BaseViewModel() {

    private val mailValidator = MailValidator()
    private val usernameValidator = UsernameValidator()
    private val passwordValidator =
        school.cactus.succulentshop.signUp.validation.PasswordValidator()

    val mail = MutableLiveData<String>()
    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _mailErrorMessage = MutableLiveData<Int>()
    private val _usernameErrorMessage = MutableLiveData<Int>()
    private val _passwordErrorMessage = MutableLiveData<Int>()

    val mailErrorMessage: LiveData<Int> = _mailErrorMessage
    val usernameErrorMessage: LiveData<Int> = _usernameErrorMessage
    val passwordErrorMessage: LiveData<Int> = _passwordErrorMessage

    fun onSignUpButtonClick() {
        if (isUsernameValid() and isEmailValid()
            and isPasswordValid()
        ) {
            repository.sendRegisterRequest(
                mail.value.orEmpty(),
                username.value.orEmpty(),
                password.value.orEmpty(),
                object : RegisterRequestCallback {
                    override fun onSuccess(jwt: String) {
                        store.saveJwt(jwt)

                        val directions =
                            SignUpFragmentDirections.actionSignUpFragmentToProductListFragment()
                        navigation.navigate(directions)
                    }

                    override fun onClientError(errorMessage: String) {
                        _snackbarState.value = SnackbarState(
                            error = errorMessage,
                            duration = BaseTransientBottomBar.LENGTH_LONG
                        )
                    }

                    override fun onUnexpectedError() {
                        _snackbarState.value = SnackbarState(
                            errorRes = R.string.unexpected_error_occurred,
                            duration = BaseTransientBottomBar.LENGTH_LONG
                        )
                    }

                    override fun onFailure() {
                        _snackbarState.value = SnackbarState(
                            errorRes = R.string.check_your_connection,
                            duration = LENGTH_INDEFINITE,
                            action = SnackbarAction(
                                text = R.string.retry,
                                action = {
                                    onSignUpButtonClick()
                                }
                            )
                        )
                    }

                }
            )
        }
    }

    fun onNavigateToSingInButtonClick() {
        val directions = SignUpFragmentDirections.actionSignUpFragmentToLoginFragment()
        navigation.navigate(directions)
    }

    private fun isEmailValid(): Boolean {
        _mailErrorMessage.value = mailValidator.validate(mail.value!!)
        return _mailErrorMessage.value == null
    }

    private fun isUsernameValid(): Boolean {
        _usernameErrorMessage.value = usernameValidator.validate(username.value!!)
        return _usernameErrorMessage.value == null
    }

    private fun isPasswordValid(): Boolean {
        _passwordErrorMessage.value = passwordValidator.validate(password.value!!)
        return _passwordErrorMessage.value == null
    }
}