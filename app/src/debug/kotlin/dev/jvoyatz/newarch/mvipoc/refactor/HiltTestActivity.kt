package dev.jvoyatz.newarch.mvipoc.refactor

import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HiltTestActivity : AppCompatActivity(){
    init {

        println("initialized!!!!!!!!!!")
    }
}