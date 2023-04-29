/*
 * Copyright (c)2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.unscramble

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.android.unscramble.data.WordDatabase
import com.example.android.unscramble.data.WordRepository
import com.example.android.unscramble.ui.GameScreen
import com.example.android.unscramble.ui.GameViewModel
import com.example.android.unscramble.ui.GameViewModelFactory
import com.example.android.unscramble.ui.theme.UnscrambleTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope

class MainActivity : ComponentActivity() {

    lateinit private var myApplication : MyApplication

    private lateinit var gameViewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myApplication = applicationContext as MyApplication
        gameViewModel = ViewModelProvider(
            owner = this,
            factory = GameViewModelFactory(WordRepository(WordDatabase.getInstance(context = applicationContext)!!))
        ).get(GameViewModel::class.java)

        setContent {
            UnscrambleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    GameScreen(gameViewModel =gameViewModel)
                }
            }
        }
    }




}

