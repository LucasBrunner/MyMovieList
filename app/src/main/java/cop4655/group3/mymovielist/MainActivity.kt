package cop4655.group3.mymovielist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    enum class MovieFragment {
        SEARCH,
        HISTORY,
        PLAN,
    }

    private val navbar = Navbar(this)
    private val movieSearch = MovieSearch(this)
    private val movieHistory = MovieHistory(this)
    private val moviePlan = MoviePlan(this)

    private var currentFragment = MovieFragment.SEARCH

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.navbar_container, navbar)
            commit()
        }

        setContent(movieSearch)
    }

    fun changeFragment(fragment: MovieFragment) {
        if (currentFragment != fragment) {
            currentFragment = fragment
            setContent(when (fragment) {
                MovieFragment.SEARCH -> movieSearch
                MovieFragment.HISTORY -> movieHistory
                MovieFragment.PLAN -> moviePlan
            })
        }
    }

    private fun setContent(fragment: MovieAppFragment) {
        fragment.refresh()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.content_container, fragment)
            commit()
        }
    }
}