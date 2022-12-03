package cop4655.group3.mymovielist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cop4655.group3.mymovielist.MainActivity
import cop4655.group3.mymovielist.R
import cop4655.group3.mymovielist.databinding.FragmentMoviePlanBinding
import cop4655.group3.mymovielist.databinding.FragmentMovieSearchBinding

class MoviePlan(main: MainActivity) : MovieAppFragment(main) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentMoviePlanBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun startup() {} // Is run once when the fragment is created
    override fun refresh() {} // Is run every time the fragment is swapped to
}