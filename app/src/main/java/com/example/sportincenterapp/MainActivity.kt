package com.example.sportincenterapp



import android.content.Context
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
import com.example.sportincenterapp.utils.SessionManager
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, Communicator {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var sessionManager: SessionManager
    var context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Main navigation settings
        setContentView(R.layout.activity_main)
        drawerLayout = findViewById(R.id.nav_view)
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        val header = navigationView.getHeaderView(0)
        var user_name = header.findViewById<TextView>(R.id.nome_utente_nav_header)
        var user_email = header.findViewById<TextView>(R.id.email_nav_header)
        var login_button = header.findViewById<Button>(R.id.login_button)
        login_button.setOnClickListener { intent = Intent(context, LoginActivity::class.java)
            startActivity(intent) }


        sessionManager = SessionManager(this) //initialize session manager in this class

        if (!sessionManager.fetchUserName().isNullOrEmpty()) {
            user_name.text = sessionManager.fetchUserName()
            user_email.text = sessionManager.fetchUserEmail()
            user_name.visibility = View.VISIBLE
            login_button.visibility = View.GONE
            pass_data_user_page(sessionManager.fetchUserName())
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
            R.id.profile -> supportFragmentManager.beginTransaction()
                .replace(R.id.Fragment_container, UserPage()).commit()
            R.id.news -> supportFragmentManager.beginTransaction()
                .replace(R.id.Fragment_container, Advertisment ()).commit()
            R.id.settings -> supportFragmentManager.beginTransaction()
                    .replace(R.id.Fragment_container, Settings()).commit()
            R.id.help -> supportFragmentManager.beginTransaction()
                .replace(R.id.Fragment_container, Faq()).commit()
            R.id.contacts -> supportFragmentManager.beginTransaction()
                .replace(R.id.Fragment_container, Concacts()).commit()
            }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun user_name_update(editTextInput: String) {

        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        val header : View = navigationView.getHeaderView(0)
        val user_name = header.findViewById<TextView>(R.id.nome_utente_nav_header)
        user_name.text = editTextInput
    }

    override fun user_email_update(editTextInput: String) {

        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        val header : View = navigationView.getHeaderView(0)
        val user_email = header.findViewById<TextView>(R.id.email_nav_header)
        user_email.text = editTextInput
    }

    override fun pass_data(editTextInput: String) {

    }

    override fun pass_data_user_page(userName: String?) {
        var fragmentUserPage = UserPage()
        fragmentUserPage.user_name = userName
    }

}