package school.cactus.succulentshop.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import school.cactus.succulentshop.R
import school.cactus.succulentshop.databinding.ActivityLoginBinding
import school.cactus.succulentshop.validation.IdentifierValidator
import school.cactus.succulentshop.validation.PasswordValidator

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val identifierValidator = IdentifierValidator()
    private val passwordValidator = PasswordValidator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.log_in)

        binding.apply {
            logInButton.setOnClickListener {
                passwordInputLayout.validate()
                identifierInputLayout.validate()
            }

            createAccountButton.setOnClickListener {
                navigateToSignUpActivity()
            }
        }
    }

    private fun navigateToSignUpActivity() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    private fun TextInputLayout.validate() {
        val errorMessage = validator().validateForSignIn(editText!!.text.toString())
        error = errorMessage?.resolveAsString()
        isErrorEnabled = errorMessage != null
    }

    private fun Int.resolveAsString() = getString(this)

    private fun TextInputLayout.validator() = when (this) {
        binding.identifierInputLayout -> identifierValidator
        binding.passwordInputLayout -> passwordValidator
        else -> throw IllegalArgumentException("Cannot find any validator for the given TextInputLayout")
    }
}
