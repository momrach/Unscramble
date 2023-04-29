package com.example.android.unscramble

import android.app.Application
import com.example.android.unscramble.data.AppDataContainer

class MyApplication: Application() {

    //La aplicación declara una clase contenedor para almacenar los objetos persistentes para toda la app
    //como repositorios
    lateinit var container: AppDataContainer

    //En el constructor de la aplicación se inicializará el container de la aplicación.
    override fun onCreate() {
        super.onCreate()
        //Hemos declarado una clase AppDataContainer
        container = AppDataContainer(this)
    }
}