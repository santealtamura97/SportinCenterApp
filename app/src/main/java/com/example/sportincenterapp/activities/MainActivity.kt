package com.example.sportincenterapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.sportincenterapp.R
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.sportincenterapp.fragments.*
import com.example.sportincenterapp.interfaces.Communicator
import com.example.sportincenterapp.utils.ApplicationContextProvider
import com.example.sportincenterapp.utils.SessionManager
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, Communicator {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var sessionManager: SessionManager

    private lateinit var userName: TextView
    private lateinit var userEmail: TextView

    private val fragmentUser : Fragment = UserPage()
    private val fragmentSettings : Fragment = Settings()
    private val fragmentHome : Fragment = HomeFragment()
    private val fragmentAdvertisment : Fragment = Advertisment()
    private val fragmentFaq : Fragment = Faq()
    private val fragmentContacts : Fragment = Contacts()
    private val bundleUser : Bundle = Bundle()
    private val bundleHome : Bundle = Bundle()
    private val bundleAdvertisment: Bundle = Bundle()
    private val bundleFaq: Bundle = Bundle()
    private val bundleContacts: Bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(ApplicationContextProvider.getContext()) //initialize session manager in this class

        //Main navigation settings
        setContentView(R.layout.activity_main)
        drawerLayout = findViewById(R.id.nav_view)
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        val header = navigationView.getHeaderView(0)

        userName = header.findViewById<TextView>(R.id.nome_utente_nav_header)
        userEmail = header.findViewById<TextView>(R.id.email_nav_header)

        val loginButton = header.findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener { intent = Intent(ApplicationContextProvider.getContext(), LoginActivity::class.java)
            startActivity(intent) }

        if (!sessionManager.fetchUserName().isNullOrEmpty()) {
            userName?.text = sessionManager.fetchUserName()
            userEmail.text = sessionManager.fetchUserEmail()
            userName?.visibility = View.VISIBLE
            loginButton.visibility = View.GONE
        } else{
            val menu = navigationView.menu

            val userPageItem = menu.getItem(0)
            val bookingsItem = menu.getItem(1)
            val calendarItem = menu.getItem(2)

            userPageItem.isVisible = false
            bookingsItem.isVisible = false
            calendarItem.isVisible = false
        }

        navigationView.setNavigationItemSelectedListener(this)

        supportFragmentManager.beginTransaction().replace(R.id.Fragment_container, fragmentHome)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_draw_open, R.string.navigation_draw_close)

        //Home button settings
        val homeButton : ImageButton = findViewById(R.id.home_button)
        homeButton.setOnClickListener {supportFragmentManager.beginTransaction()
            .replace(R.id.Fragment_container, fragmentHome).commit()}
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.Fragment_container, fragmentHome).commit()
            navigationView.setCheckedItem(R.id.home)
        }
        initializeFragments()
    }

    override fun onBackPressed() {
        if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            drawerLayout!!.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {

        when (menuItem.itemId) {

            R.id.calendar -> supportFragmentManager.beginTransaction()
                .replace(R.id.Fragment_container, CalendarFragment()).commit()
            R.id.profile -> supportFragmentManager.beginTransaction()
                .replace(R.id.Fragment_container, fragmentUser).commit()
            R.id.news -> supportFragmentManager.beginTransaction()
                .replace(R.id.Fragment_container, fragmentAdvertisment).commit()
            R.id.settings -> supportFragmentManager.beginTransaction()
                    .replace(R.id.Fragment_container, fragmentSettings).commit()
            R.id.help -> supportFragmentManager.beginTransaction()
                .replace(R.id.Fragment_container, fragmentFaq).commit()
            R.id.contacts -> supportFragmentManager.beginTransaction()
                .replace(R.id.Fragment_container, fragmentContacts).commit()
            }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun initializeFragments() {
        bundleUser.putString("username", userName.text.toString())
        bundleUser.putString("email", userEmail.text.toString())
        bundleUser.putString("um1", "Kg")
        bundleUser.putString("um2", "Cm")
        bundleUser.putInt("color", R.color.primary_color)

        bundleHome.putInt("color", R.color.primary_color)

        bundleAdvertisment.putInt("color", R.color.primary_color)

        bundleFaq.putInt("color", R.color.primary_color)

        bundleContacts.putInt("color", R.color.primary_color)

        fragmentUser.arguments = bundleUser
        fragmentHome.arguments = bundleHome
        fragmentAdvertisment.arguments = bundleAdvertisment
        fragmentFaq.arguments = bundleFaq
        fragmentContacts.arguments = bundleContacts
    }

    override fun um_update(um_1: String, um_2: String) {
        bundleUser.putString("um1", um_1)
        bundleUser.putString("um2", um_2)
        fragmentUser.arguments = bundleUser
    }

    override fun theme(index : Int) {
        val homeBtn = findViewById<ImageButton>(R.id.home_button)
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        val header = navigationView.getHeaderView(0)
        val tlbar: Toolbar = findViewById(R.id.toolbar)

        if (index == 0) {
            header.setBackgroundResource(R.color.primary_color)
            tlbar.setBackgroundResource(R.color.primary_color)
            homeBtn.setBackgroundResource(R.color.primary_color)
            bundleUser.putInt("color", R.color.primary_color)
            bundleHome.putInt("color", R.color.primary_color)
            bundleAdvertisment.putInt("color", R.color.primary_color)
            bundleFaq.putInt("color", R.color.primary_color)
            bundleContacts.putInt("color", R.color.primary_color)
        } else if (index == 1) {
            header.setBackgroundResource(R.color.primary_color_2)
            tlbar.setBackgroundResource(R.color.primary_color_2)
            homeBtn.setBackgroundResource(R.color.primary_color_2)
            bundleUser.putInt("color", R.color.primary_color_2)
            bundleHome.putInt("color", R.color.primary_color_2)
            bundleAdvertisment.putInt("color", R.color.primary_color_2)
            bundleFaq.putInt("color", R.color.primary_color_2)
            bundleContacts.putInt("color", R.color.primary_color_2)
        } else if (index == 2) {
            header.setBackgroundResource(R.color.primary_color_3)
            tlbar.setBackgroundResource(R.color.primary_color_3)
            homeBtn.setBackgroundResource(R.color.primary_color_3)
            bundleUser.putInt("color", R.color.primary_color_3)
            bundleHome.putInt("color", R.color.primary_color_3)
            bundleAdvertisment.putInt("color", R.color.primary_color_3)
            bundleFaq.putInt("color", R.color.primary_color_3)
            bundleContacts.putInt("color", R.color.primary_color_3)
        }
    }

}