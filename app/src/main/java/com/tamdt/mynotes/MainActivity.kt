package com.tamdt.mynotes

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.tamdt.mynotes.calendar.CalendarActivity
import com.tamdt.mynotes.constant.Constants
import com.tamdt.mynotes.database.NoteDatabase
import com.tamdt.mynotes.databinding.ActivityMainBinding
import com.tamdt.mynotes.reponsitory.NoteRespository
import com.tamdt.mynotes.viewmodel.NoteViewModel
import com.tamdt.mynotes.viewmodel.NoteViewModelFactory

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var noteViewModel: NoteViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
//        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupViewModel()
        setupNavigation()
        drawerLayout = findViewById(R.id.main)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolsbar)
        setSupportActionBar(toolbar)

//        toggle = ActionBarDrawerToggle(
//            this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav
//        )
//        drawerLayout.addDrawerListener(toggle)
//        toggle.syncState()

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment), binding.main)

        setSupportActionBar(binding.toolsbar)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.navView, navController)
    }

    fun openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            navController, appBarConfiguration
        ) || super.onSupportNavigateUp()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        val searchItem = menu?.findItem(R.id.searchMenu)
        val searchView = searchItem?.actionView as SearchView
        return true
    }


    private fun setupViewModel() {
        val noteRespository = NoteRespository(NoteDatabase(this))
        val viewModelProviderFactory = NoteViewModelFactory(application, noteRespository)
        noteViewModel = ViewModelProvider(this, viewModelProviderFactory)[NoteViewModel::class.java]
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                // Xử lý sự kiện cho item One
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            R.id.nav_calendar -> {
                startActivity(Intent(this, CalendarActivity::class.java))
            }

            R.id.nav_share -> {
                shareFriend()
            }

            R.id.nav_about -> {
                feedbackApp()
            }

            R.id.nav_rate -> {
                openPlayStoreForRating()
            }
        }
        drawerLayout.closeDrawers()
        return true
    }

    private fun openPlayStoreForRating() {
        try {
            val packageName: String = packageName
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                )
                startActivity(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun shareFriend() {
        val sendIntent = Intent()
        val shareText = getString(R.string.share_mess_and_link)
        sendIntent.setAction(Intent.ACTION_SEND)
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText)
        sendIntent.setType("text/plain")
        startActivity(sendIntent)
    }

    private fun feedbackApp() {
        val i = Intent(Intent.ACTION_SEND)
        i.type = "message/rfc822"
        i.putExtra(Intent.EXTRA_EMAIL, arrayOf(Constants.EMAIL_FEEDBACK))
        i.putExtra(Intent.EXTRA_SUBJECT, Constants.SUBJECT_OF_EMAIL + getString(R.string.app_name))
        try {
            startActivity(Intent.createChooser(i, "Send mail..."))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                this, "There are no email clients installed.", Toast.LENGTH_SHORT
            ).show()
        }
    }

}