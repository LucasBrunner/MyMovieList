package cop4655.group3.mymovielist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class MoviePlan(main: MainActivity) : MovieAppFragment(main) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_plan, container, false)
    }

    override fun startup() {} // Is run once when the fragment is created
    override fun refresh() {} // Is run every time the fragment is swapped to
}