package com.example.snakegreenbeansoup20220408

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.snakegreenbeansoup20220408.databinding.ActivitySnakeMainBinding

/**
 *  學對之路:Kotlin Android APP 開發
 *  12-1 建立貪食蛇Android專案與類別庫導入,畫面設計 (貪食蛇遊戲 MVVM)
 *      1.  添加Libs (build.gradle)
 *      plugins {
 *          id 'kotlin-kapt' // Android Jetpack
 *          id 'kotlin-android-extensions'
 *      }
 *
 *      dependencies {
 *          def lifecycle_version = "2.5.0-alpha04"
 *          // ViewModel
 *          implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
 *          // LiveData
 *          implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
 *          // Annotation processor (標示語法的類別庫)
 *          kapt("androidx.lifecycle:lifecycle-compiler:$lifecycle_version")
 *      }
 *      2.  下載/抓取貪食蛇上下左右圖示(參考網址:https://www.iconfinder.com/search?q=arrow&price=free),將圖示分別命名(down,left,right,top) , 並將圖示放在drawable資料夾下
 *      3.  content_snake_main.xml:
 *      3-1.添加畫面分割線 : Guidelines(上方按鈕) > 選擇Horizontal Guidelines > 分割線點擊兩下變成百分比 > 拉到70%左右
 *      3-2.拉一個TextView , ID : score ,命名 : 分數(Score)
 *      3-3.拉ImageView , 放在分割線下方 , 並修改對應 ImageView ID
 *
 */

class SnakeMainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivitySnakeMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySnakeMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }


}