package cop4655.group3.mymovielist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import cop4655.group3.mymovielist.database.DatabaseInterface
import cop4655.group3.mymovielist.databinding.ActivityMainBinding
import cop4655.group3.mymovielist.fragments.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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