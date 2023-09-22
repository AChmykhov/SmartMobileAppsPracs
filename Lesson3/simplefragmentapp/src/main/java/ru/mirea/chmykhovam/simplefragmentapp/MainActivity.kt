package ru.mirea.chmykhovam.simplefragmentapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


class MainActivity : AppCompatActivity() {
    var fragment1: Fragment? = null
    var fragment2: Fragment? = null
    var fragmentManager: FragmentManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragment1 = FirstFragment()
        fragment2 = SecondFragment()
    }
    fun onClick(view: View) {
        fragmentManager = supportFragmentManager

        when (view.id) {
            R.id.btnFirstFragment -> fragmentManager!!.beginTransaction().replace(R.id.fragmentContainer, fragment1!!).commit()

            R.id.btnSecondFragment -> fragmentManager!!.beginTransaction().replace(R.id.fragmentContainer, fragment2!!).commit()

            else -> {}
        }
    }
}