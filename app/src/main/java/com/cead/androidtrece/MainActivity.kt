package com.cead.androidtrece

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import com.cead.androidtrece.fragments.BlankFragment
import com.cead.androidtrece.fragments.MapFragment

class MainActivity : AppCompatActivity() {


    var currentFragment: Fragment ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null){
            //savedInstanceState es un bundle que le llega al oncreate por defecto si es la primera ves que se abre ese bundle llega vacio
            // cuando gira el dispositivo entra en el metodo onCreate y volveria a entrar a ese fragmento
            currentFragment = BlankFragment()
            changeFragment(currentFragment!!)
        }


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId)
        {
            R.id.menu_welcome -> {
                currentFragment = BlankFragment()
            }
            R.id.menu_map -> {
                currentFragment = MapFragment()
            }
        }
        changeFragment(currentFragment!!)
        return super.onOptionsItemSelected(item)
    }

    private fun changeFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
                              .replace(R.id.fragment_container, fragment).commit()
    }
}
