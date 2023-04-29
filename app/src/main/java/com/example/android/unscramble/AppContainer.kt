package com.example.android.unscramble.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
//interface AppContainer {
//    //Aqui se definen los elementos que va a tener este contenedor (repositorios, etc..)
//    val wordRepository: WordRepository
//}

/**
 * [AppContainer] implementation that provides instance of [WordRepository]
 * [AppContainer] implementation that provides instance of [WordDatabase]
 */
class AppDataContainer(private val context: Context) {
    val gameDatabase = WordDatabase.getInstance(context)
    val wordRepository: WordRepository
        get() = WordRepository(gameDatabase!!)
}