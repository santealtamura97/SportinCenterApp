package com.example.sportincenterapp



import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.SurfaceControl
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, Communicator {

    private lateinit var drawerLayout: DrawerLayout
    private var default_name: String? = "Andrea Forino"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Main navigation settings
        setContentView(R.layout.activity_main)
        drawerLayout = findViewById(R.id.nav_view)
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        val header = navigationView.getHeaderView(0)
        var user_name = header.findViewById<TextView>(R.id.nome_utente_nav_header)
        user_name.text = default_name

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

    override fun pass_data(editTextInput: String) {

    }

}