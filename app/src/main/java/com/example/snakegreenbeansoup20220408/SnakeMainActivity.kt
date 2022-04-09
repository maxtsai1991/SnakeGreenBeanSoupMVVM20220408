package com.example.snakegreenbeansoup20220408

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.snakegreenbeansoup20220408.databinding.ActivitySnakeMainBinding

/**
 *  學對之路:Kotlin Android APP 開發 貪食蛇遊戲MVVM
 *  12-1 建立貪食蛇Android專案與類別庫導入,畫面設計
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
 *      3-1.添加畫面分格線 : Guidelines(上方按鈕) > 選擇Horizontal Guidelines > 分格線點擊兩下變成百分比 > 拉到70%左右
 *      3-2.拉一個TextView , ID : score ,命名 : 分數(Score)
 *      3-3.拉ImageView , 放在分格線下方 , 並修改對應 ImageView ID
 */

/**
 *  12-3 MVVM架構設計
 *      該章節重點整理 :
 *          產生ViewModel , 在Activity取得ViewModel物件 , 之後貪食蛇規劃功能 ; 功能如何規劃 : 讓ViewModel可以去接收現在開始遊戲(EX : fun start()),就可以去做準備,包含準備蛇的身體,蛇的身體就是一連串的位置,位置怎麼做設計,設計部分後面章節會談到,
 *          蛇以後會移動,移動部分就是蛇更改了它的位置,這個時候SnakeMainActivity也會得知,再來小蘋果出現了也會得知,畫面右上的分數改變了它也會得知,撞牆有改變了它也會得知
 *
 *      Activity & ViewModel 關係 :
 *          ViewModel設計屬性及方法 , 屬性使用MutableLiveData , 而在Activity先取得ViewModel物件後,就可以使用observe方法去監聽觀察ViewModel屬性的變動(資料有改變的時候)
 *
 *      1.   產生/設計ViewModel類別(命名: SnakeViewModel , snakegreenbeansoup20220408 > New > Kotlin File/Class) ,繼承ViewModel()並呼叫建構子
 *      1-1. 設計蛇的位置: 一隻蛇的位置,有橫向跟直向(就是X & Y),能夠保存XY的東西,就是寫個類別(EX : data class Position(val x : Int , val y : Int)) , 設計蛇的初始化位置,蛇的身體是個集合,這個身體使用MutableLiveData,以後變動的時候要提醒別人EX : val body = MutableLiveData<List<Position>>()
 *      1-2. 設計小蘋果: EX : val apple = MutableLiveData<Position>()
 *      1-3. 設計分數: EX : val score = MutableLiveData<Int>()
 *      1-4. 設計啟動遊戲: EX : fun start(){ }
 *      1-5. 設計重設遊戲: EX : fun reset(){ }
 *      1-6. 設計移動蛇:  EX : fun reset(){ } , 使用列舉將移動的上下左右放入 EX : enum class Direction { TOP , DOWN , LEFT , RIGHT }
 *      2.   SnakeMainActivity.kt:
 *      2-1. 取得ViewModel(EX : val viewModel = ViewModelProvider(this).get(SnakeViewModel::class.java))
 *      2-2. 觀察/監聽蛇的身體 EX :  viewModel.body.observe(this, Observer {  })
 *      2-3. 觀察/監聽身上的分數 EX : viewModel.score.observe(this, Observer {  })
 *      2-4. 觀察/監聽小蘋果(小紅點) EX : viewModel.apple.observe(this, Observer {  })
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

        // 取得ViewModel
        val viewModel = ViewModelProvider(this).get(SnakeViewModel::class.java)

        // 觀察/監聽蛇的身體
        viewModel.body.observe(this, Observer {

        })

        // 觀察/監聽身上的分數
        viewModel.score.observe(this, Observer {

        })

        // 觀察/監聽小蘋果(小紅點)
        viewModel.apple.observe(this, Observer {

        })

    }


}