package com.ntduc.navigationutils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

fun AppCompatActivity.setupNavigationWithActionBar(
  navController: NavController
): AppBarConfiguration {
  val appBarConfiguration = AppBarConfiguration(navController.graph)
  setupActionBarWithNavController(navController, appBarConfiguration)
  return appBarConfiguration
}

fun AppCompatActivity.setupNavigationWithActionBar(
  @IdRes idNavHostFragment: Int
): AppBarConfiguration {
  val navController = findNavController(idNavHostFragment)
  val appBarConfiguration = AppBarConfiguration(navController.graph)
  setupActionBarWithNavController(navController, appBarConfiguration)
  return appBarConfiguration
}

fun AppCompatActivity.setupNavigationWithToolBar(
  navController: NavController, toolbar: Toolbar
): AppBarConfiguration {
  setSupportActionBar(toolbar)
  val appBarConfiguration = AppBarConfiguration(navController.graph)
  setupActionBarWithNavController(navController, appBarConfiguration)
  return appBarConfiguration
}

fun AppCompatActivity.setupNavigationWithToolBar(
  @IdRes idNavHostFragment: Int, toolbar: Toolbar
): AppBarConfiguration {
  setSupportActionBar(toolbar)
  val navController = findNavController(idNavHostFragment)
  val appBarConfiguration = AppBarConfiguration(navController.graph)
  setupActionBarWithNavController(navController, appBarConfiguration)
  return appBarConfiguration
}

fun setupNavigationWithCollapsingToolbar(
  navController: NavController, collapsingToolbarLayout: CollapsingToolbarLayout, toolbar: Toolbar
): AppBarConfiguration {
  val appBarConfiguration = AppBarConfiguration(navController.graph)
  collapsingToolbarLayout.setupWithNavController(toolbar, navController, appBarConfiguration)
  return appBarConfiguration
}

fun AppCompatActivity.setupNavigationWithCollapsingToolbar(
  @IdRes idNavHostFragment: Int, collapsingToolbarLayout: CollapsingToolbarLayout, toolbar: Toolbar
): AppBarConfiguration {
  val navController = findNavController(idNavHostFragment)
  val appBarConfiguration = AppBarConfiguration(navController.graph)
  collapsingToolbarLayout.setupWithNavController(toolbar, navController, appBarConfiguration)
  return appBarConfiguration
}

fun AppCompatActivity.setupNavigationWithNavigationBar(
  navController: NavController,
  bottomNavigationView: BottomNavigationView,
  topLevelDestinationIds: Set<Int>,
): AppBarConfiguration {
  val appBarConfiguration = AppBarConfiguration(topLevelDestinationIds)
  setupActionBarWithNavController(navController, appBarConfiguration)
  bottomNavigationView.setupWithNavController(navController)
  return appBarConfiguration
}

fun AppCompatActivity.setupNavigationWithNavigationBar(
  @IdRes idNavHostFragment: Int,
  bottomNavigationView: BottomNavigationView,
  topLevelDestinationIds: Set<Int>,
): AppBarConfiguration {
  val appBarConfiguration = AppBarConfiguration(topLevelDestinationIds)
  val navController = findNavController(idNavHostFragment)
  setupActionBarWithNavController(navController, appBarConfiguration)
  bottomNavigationView.setupWithNavController(navController)
  return appBarConfiguration
}

fun AppCompatActivity.setupNavigationWithNavigationDrawer(
  navController: NavController,
  drawerLayout: DrawerLayout,
  navigationView: NavigationView,
  collapsingToolbarLayout: CollapsingToolbarLayout? = null,
  toolbar: Toolbar? = null,
  bottomNavigationView: BottomNavigationView? = null,
  topLevelDestinationIds: Set<Int>? = null,
): AppBarConfiguration {
  if (collapsingToolbarLayout == null && toolbar != null) setSupportActionBar(toolbar)
  val appBarConfiguration =
    if (topLevelDestinationIds == null) AppBarConfiguration(navController.graph, drawerLayout)
    else AppBarConfiguration(topLevelDestinationIds, drawerLayout)
  if (collapsingToolbarLayout == null || toolbar == null) {
    setupActionBarWithNavController(navController, appBarConfiguration)
  } else {
    collapsingToolbarLayout.setupWithNavController(toolbar, navController, appBarConfiguration)
  }
  bottomNavigationView?.setupWithNavController(navController)
  navigationView.setupWithNavController(navController)
  return appBarConfiguration
}

fun AppCompatActivity.setupNavigationWithNavigationDrawer(
  @IdRes idNavHostFragment: Int,
  drawerLayout: DrawerLayout,
  navigationView: NavigationView,
  collapsingToolbarLayout: CollapsingToolbarLayout? = null,
  toolbar: Toolbar? = null,
  bottomNavigationView: BottomNavigationView? = null,
  topLevelDestinationIds: Set<Int>? = null,
): AppBarConfiguration {
  if (collapsingToolbarLayout == null && toolbar != null) setSupportActionBar(toolbar)
  val navController = findNavController(idNavHostFragment)
  val appBarConfiguration =
    if (topLevelDestinationIds == null) AppBarConfiguration(navController.graph, drawerLayout)
    else AppBarConfiguration(topLevelDestinationIds, drawerLayout)
  if (collapsingToolbarLayout == null || toolbar == null) {
    setupActionBarWithNavController(navController, appBarConfiguration)
  } else {
    collapsingToolbarLayout.setupWithNavController(toolbar, navController, appBarConfiguration)
  }
  bottomNavigationView?.setupWithNavController(navController)
  navigationView.setupWithNavController(navController)
  return appBarConfiguration
}

fun navigateToDes(
  navController: NavController,
  @IdRes idDes: Int,
  args: Bundle? = null,
  navOptions: NavOptions? = DEFAULT_NAV_OPTION,
  navigatorExtras: Navigator.Extras? = null
) {
  navController.navigate(idDes, args, navOptions, navigatorExtras)
}

fun AppCompatActivity.navigateToDes(
  @IdRes idNavHostFragment: Int,
  @IdRes idDes: Int,
  args: Bundle? = null,
  navOptions: NavOptions? = DEFAULT_NAV_OPTION,
  navigatorExtras: Navigator.Extras? = null
) {
  val navController = findNavController(idNavHostFragment)
  navController.navigate(idDes, args, navOptions, navigatorExtras)
}

fun Fragment.navigateToDes(
  @IdRes idDes: Int,
  args: Bundle? = null,
  navOptions: NavOptions? = DEFAULT_NAV_OPTION,
  navigatorExtras: Navigator.Extras? = null
) {
  findNavController().navigate(idDes, args, navOptions, navigatorExtras)
}

private val DEFAULT_NAV_OPTION = navOptions {
  anim {
    enter = R.anim.slide_in_right
    exit = R.anim.slide_out_left
    popEnter = R.anim.slide_in_left
    popExit = R.anim.slide_out_right
  }
}

fun navigateToActionListener(
  @IdRes idAction: Int, args: Bundle? = null
): View.OnClickListener {
  return Navigation.createNavigateOnClickListener(idAction, args)
}

fun navigateToActionListener(
  directions: NavDirections
): View.OnClickListener {
  return Navigation.createNavigateOnClickListener(directions)
}

fun AppCompatActivity.createDeepLink(
  navController: NavController,
  @IdRes idDes: Int,
  args: Bundle? = null,
  title: String,
  body: String,
  @DrawableRes icon: Int,
  idChannel: String,
  nameChannel: String,
) {
  val deeplink =
    navController.createDeepLink().setDestination(idDes).setArguments(args).createPendingIntent()
  
  val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    notificationManager.createNotificationChannel(
      NotificationChannel(
        idChannel, nameChannel, NotificationManager.IMPORTANCE_HIGH
      )
    )
  }
  
  val builder =
    NotificationCompat.Builder(this, idChannel).setContentTitle(title).setContentText(body)
      .setSmallIcon(icon).setContentIntent(deeplink).setAutoCancel(true)
  notificationManager.notify(0, builder.build())
}

fun AppCompatActivity.createDeepLink(
  @IdRes idNavHostFragment: Int,
  @IdRes idDes: Int,
  args: Bundle? = null,
  title: String,
  body: String,
  @DrawableRes icon: Int,
  idChannel: String,
  nameChannel: String,
) {
  val navController = findNavController(idNavHostFragment)
  val deeplink =
    navController.createDeepLink().setDestination(idDes).setArguments(args).createPendingIntent()
  
  val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    notificationManager.createNotificationChannel(
      NotificationChannel(
        idChannel, nameChannel, NotificationManager.IMPORTANCE_HIGH
      )
    )
  }
  
  val builder =
    NotificationCompat.Builder(this, idChannel).setContentTitle(title).setContentText(body)
      .setSmallIcon(icon).setContentIntent(deeplink).setAutoCancel(true)
  notificationManager.notify(0, builder.build())
}

fun Fragment.createDeepLink(
  @IdRes idDes: Int,
  args: Bundle? = null,
  title: String,
  body: String,
  @DrawableRes icon: Int,
  idChannel: String,
  nameChannel: String,
) {
  val deeplink = findNavController().createDeepLink().setDestination(idDes).setArguments(args)
    .createPendingIntent()
  
  val notificationManager =
    requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    notificationManager.createNotificationChannel(
      NotificationChannel(
        idChannel, nameChannel, NotificationManager.IMPORTANCE_HIGH
      )
    )
  }
  
  val builder = NotificationCompat.Builder(requireContext(), idChannel).setContentTitle(title)
    .setContentText(body).setSmallIcon(icon).setContentIntent(deeplink).setAutoCancel(true)
  notificationManager.notify(0, builder.build())
}