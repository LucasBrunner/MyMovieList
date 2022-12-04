package cop4655.group3.mymovielist.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cop4655.group3.mymovielist.MainActivity
import cop4655.group3.mymovielist.R
import cop4655.group3.mymovielist.databinding.FragmentNavbarBinding

class Navbar(private val main: MainActivity) : Fragment() {

    private lateinit var binding: FragmentNavbarBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNavbarBinding.inflate(layoutInflater, container, false)

        binding.historyButton.setOnClickListener{ main.changeFragment(MainActivity.MovieFragment.HISTORY) }
        binding.searchButton.setOnClickListener{ main.changeFragment(MainActivity.MovieFragment.SEARCH) }
        binding.planButton.setOnClickListener{ main.changeFragment(MainActivity.MovieFragment.PLAN) }
        binding.heartedButton.setOnClickListener{ main.changeFragment(MainActivity.MovieFragment.HEARTED) }

        binding.searchButton.setColorFilter(R.color.grey)

        return binding.root;
    }

    fun updateTint(fragment: MainActivity.MovieFragment) {
        when(fragment) {
            MainActivity.MovieFragment.SEARCH -> {
                binding.searchButton.setColorFilter(Color.GRAY)
                binding.historyButton.clearColorFilter()
                binding.planButton.clearColorFilter()
                binding.heartedButton.clearColorFilter()
            }
            MainActivity.MovieFragment.HISTORY -> {
                binding.searchButton.clearColorFilter()
                binding.historyButton.setColorFilter(Color.GRAY)
                binding.planButton.clearColorFilter()
                binding.heartedButton.clearColorFilter()
            }
            MainActivity.MovieFragment.PLAN -> {
                binding.searchButton.clearColorFilter()
                binding.historyButton.clearColorFilter()
                binding.planButton.setColorFilter(Color.GRAY)
                binding.heartedButton.clearColorFilter()
            }
            MainActivity.MovieFragment.HEARTED -> {
                binding.searchButton.clearColorFilter()
                binding.historyButton.clearColorFilter()
                binding.planButton.clearColorFilter()
                binding.heartedButton.setColorFilter(Color.GRAY)
            }
        }
    }
}