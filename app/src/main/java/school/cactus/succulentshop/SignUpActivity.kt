package school.cactus.succulentshop

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import school.cactus.succulentshop.databinding.ActivitySignUpBinding

class SignUpActivity: AppCompatActivity() {
    private lateinit var binding : ActivitySignUpBinding

    private val mailValidator = MailValidator()
    private val usernameValidator = UsernameValidator()
    private val signUpPasswordValidator = SignUpPasswordValidator()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.sign_up)

        binding.apply {
            signUpButton.setOnClickListener {
                signUpEmail.validate()
                signUpUsername.validate()
                signUpPassword.validate()
            }

            goToSingInButton.setOnClickListener {
                navigateToSignInButton()
            }
        }

    }

    private fun navigateToSignInButton() {
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
    }

    private fun TextInputLayout.validate() {
        val errorMessage = validator().validate(editText!!.text.toString())
        errorMessage?.let {
            error = resolveAsString(it)
        }
        isErrorEnabled = errorMessage != null
    }

    private fun resolveAsString(number: Int) = getString(number)

    private fun TextInputLayout.validator() = when (this) {
        binding.signUpEmail -> mailValidator
        binding.signUpUsername -> usernameValidator
        binding.signUpPassword -> signUpPasswordValidator
        else -> throw IllegalArgumentException("Cannot find any validator for the given TextInputLayout")
    }
}

