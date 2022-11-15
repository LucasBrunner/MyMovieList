package cop4655.group3.mymovielist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import cop4655.group3.mymovielist.MainActivity

abstract class MovieAppFragment(val main: MainActivity) : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startup()
    }

    abstract fun startup()
    abstract fun refresh()
}