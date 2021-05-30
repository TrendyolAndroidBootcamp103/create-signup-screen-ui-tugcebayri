package school.cactus.succulentshop.signUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import school.cactus.succulentshop.R
import school.cactus.succulentshop.auth.JwtStore
import school.cactus.succulentshop.databinding.FragmentSignUpBinding
import school.cactus.succulentshop.infra.BaseFragment

class SignUpFragment : BaseFragment() {
    private var _binding: FragmentSignUpBinding? = null

    private val binding get() = _binding!!

    private val store = JwtStore(requireContext())

    override val viewModel: SignUpViewModel by viewModels {
        SignUpViewModelFactory(store)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        if (JwtStore(requireContext()).isExistsJwt()) {
            findNavController().navigate(R.id.action_signUpFragment_to_productListFragment)
        } //TODO: how to move into SVM

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title = getString(R.string.sign_up)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
