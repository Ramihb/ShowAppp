package com.example.showapp.Activity

import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.showapp.Fragment.*
import com.example.showapp.R
import com.example.showapp.Utils.NetworkConnection
import com.example.showapp.databinding.ActivityUserBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {

    lateinit var binding: ActivityUserBinding
    private var fragment: Fragment? = null
    private lateinit var cld: NetworkConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        checkNetworkConnection()

        setUpBar()


    }

    private fun checkNetworkConnection() {
        cld = NetworkConnection(application)

        cld.observe(this) { isConnected ->

            if (isConnected) {
                Log.i("connected", "good")

            } else {
                Snackbar.make(UserActivityy, "Check your connection", Snackbar.LENGTH_SHORT)
                    .setIcon(
                        getDrawable(R.drawable.wifi_off)!!,
                        resources.getColor(android.R.color.white, theme)
                    )
                    .show()
            }

        }
    }


    //test
    private fun Snackbar.setIcon(drawable: Drawable, @ColorInt colorTint: Int): Snackbar {
        return this.apply {
            setAction(" ") {}
            val textView =
                view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action)
            textView.text = ""

            drawable.setTint(colorTint)
            drawable.setTintMode(PorterDuff.Mode.SRC_ATOP)
            textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        }
    }

    private fun setUpBar() {

        binding.bottomNavBar.setOnItemSelectedListener {
            when (it) {
                R.id.Home_bottom -> {
                    fragment = HomeFragment()
                }
                R.id.Search_bottom -> {
                    fragment = SearchFragment()
                }
                R.id.Favourite_bottom -> {
                    fragment = FavouriteFragment()
                }
                R.id.Profile_bottom -> {
                    fragment = ProfileFragment()
                }
                R.id.Cart_bottom -> {
                    fragment = CartFragment()
                }

            }

            fragment!!.let {
                supportFragmentManager.beginTransaction().replace(R.id.main_fragment, fragment!!)
                    .addToBackStack(null)
                    .commit()
            }
        }


    }

    override fun onBackPressed() {
        finish()
        val intent = Intent(this@UserActivity, MainActivity::class.java)
        startActivity(intent)
    }


}