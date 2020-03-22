package eu.chessout.v2

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private var firebaseUser: FirebaseUser? = null
    private lateinit var navController: NavController
    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkLoggedIn()

        bottomNavView = findViewById(R.id.bottom_nav_view)

        navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard02, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavView.setupWithNavController(navController)
    }

    /**
     * It checks the user is logged in and if not it will start the SignInActivity
     */
    private fun checkLoggedIn() {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser
        if (null == firebaseUser) {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }


    public fun showBottomNav() {
        bottomNavView.visibility = View.VISIBLE
    }

    public fun hideBottomNav() {
        bottomNavView.visibility = View.GONE
    }
}
