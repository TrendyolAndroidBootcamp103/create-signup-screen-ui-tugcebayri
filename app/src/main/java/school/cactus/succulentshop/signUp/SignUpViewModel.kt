package school.cactus.succulentshop.signUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import school.cactus.succulentshop.R
import school.cactus.succulentshop.api.GenericErrorResponse
import school.cactus.succulentshop.api.api
import school.cactus.succulentshop.api.register.RegisterRequest
import school.cactus.succulentshop.api.register.RegisterResponse
import school.cactus.succulentshop.auth.JwtStore
import school.cactus.succulentshop.infra.BaseViewModel
import school.cactus.succulentshop.infra.snackbar.SnackbarAction
import school.cactus.succulentshop.infra.snackbar.SnackbarState
import school.cactus.succulentshop.signUp.validation.MailValidator
import school.cactus.succulentshop.signUp.validation.UsernameValidator

class SignUpViewModel(private val store: JwtStore) : BaseViewModel() {
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
            sendRegisterRequest()
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

    private fun sendRegisterRequest() {
        val request = RegisterRequest(
            mail.value.orEmpty(),
            username.value.orEmpty(),
            password.value.orEmpty()
        )

        api.register(request).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                when (response.code()) {
                    200 -> onRegisterSuccess(response.body()!!)
                    in 400..499 -> onClientError(response.errorBody())
                    else -> onUnexpectedError()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _snackbarState.value = SnackbarState(
                    errorRes = R.string.check_your_connection,
                    duration = Snackbar.LENGTH_INDEFINITE,
                    action = SnackbarAction(
                        text = R.string.retry,
                        action = this@SignUpViewModel::sendRegisterRequest
                    )
                )
            }
        })
    }

    private fun onUnexpectedError() {
        _snackbarState.value = SnackbarState(
            errorRes = R.string.unexpected_error_occurred,
            duration = BaseTransientBottomBar.LENGTH_LONG
        )
    }

    private fun onClientError(errorBody: ResponseBody?) {
        if (errorBody == null) return onUnexpectedError()

        try {
            val message = errorBody.errorMessage()
            _snackbarState.value = SnackbarState(
                error = message,
                duration = BaseTransientBottomBar.LENGTH_LONG
            )
        } catch (ex: JsonSyntaxException) {
            onUnexpectedError()
        }
    }

    private fun ResponseBody.errorMessage(): String {
        val errorBody = string()
        val gson: Gson = GsonBuilder().create()
        val loginErrorResponse = gson.fromJson(errorBody, GenericErrorResponse::class.java)
        return loginErrorResponse.message[0].messages[0].message
    }

    private fun onRegisterSuccess(response: RegisterResponse) {
        store.saveJwt(response.jwt)

        val directions = SignUpFragmentDirections.actionSignUpFragmentToProductListFragment()
        navigation.navigate(directions)
    }

}