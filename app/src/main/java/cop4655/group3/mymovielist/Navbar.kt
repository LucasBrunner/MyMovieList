package cop4655.group3.mymovielist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

class Navbar(private val main: MainActivity) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_navbar, container, false)

        v.findViewById<ImageButton>(R.id.historyButton).setOnClickListener{ main.changeFragment(MainActivity.MovieFragment.HISTORY) }
        v.findViewById<ImageButton>(R.id.searchButton).setOnClickListener{ main.changeFragment(MainActivity.MovieFragment.SEARCH) }
        v.findViewById<ImageButton>(R.id.planButton).setOnClickListener{ main.changeFragment(MainActivity.MovieFragment.PLAN) }

        return v;
    }
}