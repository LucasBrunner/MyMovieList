package cop4655.group3.mymovielist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cop4655.group3.mymovielist.databinding.ActivityMainBinding
import cop4655.group3.mymovielist.fragments.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    enum class MovieFragment {
        SEARCH,
        HISTORY,
        PLAN,
        HEARTED,
    }

    private val navbar = Navbar(this)
    private val movieSearch = MovieSearch(this)
    private val movieHistory = MovieSortListFragment(this, MovieSortListFragment.ListShowType.HISTORY)
    private val moviePlan = MovieSortListFragment(this, MovieSortListFragment.ListShowType.PLAN)
    private val movieHearted = MovieSortListFragment(this, MovieSortListFragment.ListShowType.HEARTED)

    private var currentFragment = MovieFragment.SEARCH

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        supportFragmentManager.beginTransaction().apply {
            replace(binding.navbarContainer.id, navbar)
            commit()
        }

        setContent(movieSearch)
    }

    fun changeFragment(fragment: MovieFragment) {
        if (currentFragment != fragment) {
            currentFragment = fragment
            navbar.updateTint(fragment)
            when (fragment) {
                MovieFragment.SEARCH -> setContent(movieSearch)
                MovieFragment.HISTORY -> setContent(movieHistory)
                MovieFragment.PLAN -> setContent(moviePlan)
                MovieFragment.HEARTED -> setContent(movieHearted)
            }
        }
    }

    private fun setContent(fragment: MovieAppFragment) {
        fragment.refresh()
        supportFragmentManager.beginTransaction().apply {
            replace(binding.contentContainer.id, fragment)
            commit()
        }
    }
}