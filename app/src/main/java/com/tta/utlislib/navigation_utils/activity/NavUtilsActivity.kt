package com.tta.utlislib.navigation_utils.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import com.ntduc.contextutils.inflater
import com.ntduc.navigationutils.setupNavigationWithNavigationDrawer
import com.tta.utlislib.MainNavigationDirections
import com.tta.utlislib.R
import com.tta.utlislib.databinding.ActivityNavUtilsBinding

class NavUtilsActivity : AppCompatActivity() {
  private lateinit var binding: ActivityNavUtilsBinding
  private lateinit var navController: NavController
  private lateinit var appBarConfiguration: AppBarConfiguration
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityNavUtilsBinding.inflate(inflater)
    setContentView(binding.root)
    
    navController = findNavController(R.id.my_nav_host_fragment)
    
    appBarConfiguration = setupNavigationWithNavigationDrawer(
      navController = navController,
      drawerLayout = binding.root,
      navigationView = binding.navView,
      toolbar = binding.toolbar,
      bottomNavigationView = binding.bottomNavView,
      topLevelDestinationIds = setOf(R.id.home_dest, R.id.deeplink_dest)
    )
    
    binding.bottomNavView.setOnItemSelectedListener {
      when (it.itemId) {
        R.id.home_dest -> {
          val action = MainNavigationDirections.actionGlobalHomeDest()
          navController.navigate(action)
          
          return@setOnItemSelectedListener true
        }
        else -> {
          NavigationUI.onNavDestinationSelected(
            it,
            navController
          )
          return@setOnItemSelectedListener true
        }
      }
    }
  }
  
  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.overflow_menu, menu)
    return true
  }
  
  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return item.onNavDestinationSelected(navController)
        || super.onOptionsItemSelected(item)
  }
  
  override fun onSupportNavigateUp(): Boolean {
    return navController.navigateUp(appBarConfiguration)
  }
}