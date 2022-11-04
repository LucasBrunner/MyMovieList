package cop4655.group3.mymovielist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

class Navbar(private val main: MainActivity) : Fragment() {

    private lateinit var history: ImageButton
    private lateinit var search: ImageButton
    private lateinit var plan: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_navbar, container, false)

        history = v.findViewById<ImageButton>(R.id.historyButton)
        search = v.findViewById<ImageButton>(R.id.searchButton)
        plan = v.findViewById<ImageButton>(R.id.planButton)

        history.setOnClickListener{ main.changeFragment(MainActivity.MovieFragment.HISTORY) }
        search.setOnClickListener{ main.changeFragment(MainActivity.MovieFragment.SEARCH) }
        plan.setOnClickListener{ main.changeFragment(MainActivity.MovieFragment.PLAN) }

        search.setColorFilter(R.color.grey)

        return v;
    }

    fun updateTint(fragment: MainActivity.MovieFragment) {
        when(fragment) {
            MainActivity.MovieFragment.SEARCH -> {
                search.setColorFilter(R.color.grey)
                history.clearColorFilter()
                plan.clearColorFilter()
            }
            MainActivity.MovieFragment.HISTORY -> {
                search.clearColorFilter()
                history.setColorFilter(R.color.grey)
                plan.clearColorFilter()
            }
            MainActivity.MovieFragment.PLAN -> {
                search.clearColorFilter()
                history.clearColorFilter()
                plan.setColorFilter(R.color.grey)
            }
        }
    }
}