package school.cactus.succulentshop.signUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import school.cactus.succulentshop.R
import school.cactus.succulentshop.databinding.FragmentSignUpBinding
import school.cactus.succulentshop.signUp.validation.MailValidator
import school.cactus.succulentshop.signUp.validation.UsernameValidator

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null

    private val binding get() = _binding!!

    private val mailValidator = MailValidator()
    private val usernameValidator = UsernameValidator()
    private val passwordValidator =
        school.cactus.succulentshop.signUp.validation.PasswordValidator()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            signUpButton.setOnClickListener {
                if (signUpUsername.isValid() and signUpEmail.isValid()
                    and signUpPassword.isValid()
                ) {
                    findNavController().navigate(R.id.action_signUpFragment_to_productListFragment)
                }
            }

            goToSingInButton.setOnClickListener {
                findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
            }
        }

        requireActivity().title = getString(R.string.sign_up)
    }

    private fun TextInputLayout.isValid(): Boolean {
        val errorMessage = validator().validate(editText!!.text.toString())
        errorMessage?.let {
            error = resolveAsString(it)
        }
        isErrorEnabled = errorMessage != null
        return errorMessage == null
    }

    private fun resolveAsString(number: Int) = getString(number)

    private fun TextInputLayout.validator() = when (this) {
        binding.signUpEmail -> mailValidator
        binding.signUpUsername -> usernameValidator
        binding.signUpPassword -> passwordValidator
        else -> throw IllegalArgumentException("Cannot find any validator for the given TextInputLayout")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
