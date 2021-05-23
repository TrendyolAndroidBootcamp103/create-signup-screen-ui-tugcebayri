package school.cactus.succulentshop.product.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import school.cactus.succulentshop.R
import school.cactus.succulentshop.databinding.FragmentProductDetailBinding
import school.cactus.succulentshop.product.BUNDLE_KEY_PRODUCT_ID
import school.cactus.succulentshop.product.ProductItem
import school.cactus.succulentshop.product.list.ProductStore

class ProductDetailFragment : Fragment() {
    private var _binding: FragmentProductDetailBinding? = null

    private val binding get() = _binding!!

    private val store = ProductStore()

    private val adapter = RelatedProductAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title = getString(R.string.app_name)

        val productId = requireArguments().getInt(BUNDLE_KEY_PRODUCT_ID)
        val product = store.findProduct(productId)

        binding.apply {
            titleText.text = product.title
            priceText.text = product.price
            descriptionText.text = product.description

            Glide.with(binding.root)
                .load(product.imageUrl)
                .into(imageView)
        }

        binding.productDetailRecyclerView.adapter = adapter
        //binding.productDetailRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        //binding.productDetailRecyclerView.layoutManager = StaggeredGridLayoutManager(1,HORIZONTAL)
        adapter.submitList(store.products)
        binding.productDetailRecyclerView.addItemDecoration(RelatedProductDecoration())

        var randomList = getRandomRelatedProductList(store.products)
        adapter.submitList(randomList)

        adapter.itemClickListener = {
            val action = ProductDetailFragmentDirections.actionProductDetailFragmentSelf(it.id)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getRandomRelatedProductList(list: List<ProductItem>): List<ProductItem> {
        val s: MutableSet<ProductItem> = mutableSetOf()
        while (s.size < 4) {
            var x = (0..7).random()

            if (x == id) continue
            else s.add(list[x])
        }
        return s.toList()
    }
}
