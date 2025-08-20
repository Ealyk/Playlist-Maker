package playlist.media.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.ealyk.playlistmaker2.databinding.FragmentMediaFavouritesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import playlist.media.ui.view_model.FavoritesViewModel
import kotlin.getValue

class FavoritesFragment: Fragment() {
    private var _binding: FragmentMediaFavouritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMediaFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        fun newInstance() = FavoritesFragment().apply {
            arguments = bundleOf()
        }
    }
}