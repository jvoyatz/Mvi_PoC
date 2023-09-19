package dev.jvoyatz.newarch.mvipoc.navigation

import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

class AppNavigator(
    private var navController: NavController?,
    private var activity: FragmentActivity?
): Navigator {

    init {
//        if(navController == null){ //breaks injection & test
//            navController = NavHostFragment.findNavController(activity.supportFragmentManager.findFragmentById(
//                androidx.navigation.fragment.R.id.navhost)!!)
//        }
    }

    override fun navigate(destination: Destination) {
        when(destination){
            is Destination.MovieDetails -> {
                //navController.navigate()
//                activity?.supportFragmentManager.beginTransaction()
//                    .add()
//                    .commit()
            }
            Destination.Movies -> {
                //navController.navigate()
            }
        }
    }


    //in case you are not able to inject navController through di framework
    //then use this for bind navcontroller in the activity onCreate
    override fun bind(navController: NavController) {
        this.navController = navController
    }

    override fun unbind() {
        navController = null
        activity = null
    }

    fun bind(activity: FragmentActivity){
        this.activity = activity
    }
}
