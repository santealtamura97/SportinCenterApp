package com.example.sportincenterapp.activities



import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.sportincenterapp.*
import com.example.sportincenterapp.data.models.User
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
        }

        navigationView.setNavigationItemSelectedListener(this)

        supportFragmentManager.beginTransaction().replace(R.id.Fragment_container, HomeFragment())
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_draw_open, R.string.navigation_draw_close)

        //Home button settings
        val homeButton : ImageButton = findViewById(R.id.home_button)
        homeButton.setOnClickListener {supportFragmentManager.beginTransaction()
            .replace(R.id.Fragment_container, HomeFragment()).commit()}
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.Fragment_container, HomeFragment()).commit()
            navigationView.setCheckedItem(R.id.home)
        }

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
            R.id.profile ->
                supportFragmentManager.beginTransaction()
                .replace(R.id.Fragment_container, initializeUserPage()).commit()
            R.id.news -> supportFragmentManager.beginTransaction()
                .replace(R.id.Fragment_container, Advertisment()).commit()
            R.id.settings -> supportFragmentManager.beginTransaction()
                    .replace(R.id.Fragment_container, Settings()).commit()
            R.id.help -> supportFragmentManager.beginTransaction()
                .replace(R.id.Fragment_container, Faq()).commit()
            R.id.contacts -> supportFragmentManager.beginTransaction()
                .replace(R.id.Fragment_container, Contacts()).commit()
            }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun initializeUserPage(): UserPage {
        val bundle = Bundle()
        val userPage = UserPage()
        bundle.putString("username", userName.text.toString())
        userPage.arguments = bundle

        return userPage
    }

    override fun um_update(um_1: String, um_2: String) {
        val bundle = Bundle()
        bundle.putString("um1", um_1)
        bundle.putString("um2", um_2)

        val fragmentUser = UserPage()
        fragmentUser.arguments = bundle
    }

}