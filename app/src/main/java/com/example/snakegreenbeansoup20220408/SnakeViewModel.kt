package com.example.snakegreenbeansoup20220408

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.concurrent.fixedRateTimer
import kotlin.random.Random

/**
 * 12-5 蛇怎麼動 ? 動畫與Timer設計
 * 1.   GameView.kt ) 增加蛇身體間的間隔 , 添加一屬性 EX : val gap = 5 , 數字越大間隔越大 , 以及相對應程式碼,在onDraw方法裡 EX :  drawRect((it.x * size).toFloat() + gap, (it.y * size).toFloat() + gap , ((it.x + 1) * size).toFloat() - gap , ((it.y + 1) * size).toFloat() - gap , paint)
 * 2.   SnakeViewModel.kt ) 讓蛇移動(左移,程式邏輯:移動其實就是蛇身體的左邊加一個身體,右邊身體少一個,當吃到小蘋果時,左身體加一個,而右身體不減少一個),
 *      移動就是一個定時動作,使用FixedRateTimer 定時速率(速率計時器) , fixedRateTimer("自定義名字",是不是daemon類型的,第一次啟動要停多久啟動(單位毫秒),重複動作時間(單位毫秒,時間越長,蛇動的越慢)) EX :  fixedRateTimer("timer",true,500,500){ },
 *      移動找下一個位置程式碼,也就是下一個位置的第一個位置,使用snakeBody.first()方法,不想變動它,copy一份再往左邊移動,使用copy()方法,這個方法就是得到一個物件,再往左邊移,接下來要對她做一些處理,呼叫.apply,
 *      並且預設給蛇方向的屬性 EX :  private var direction = Direction.LEFT , 蛇移動的方向EX : when(direction){  Direction.LEFT -> x-- ;  Direction.RIGHT -> x++ ; Direction.TOP -> y-- ; Direction.DOWN -> y++ } ,
 *      再來蛇移動的反覆動作(蛇移動動畫)程式碼 (加蛇最前面的身體(左),移除最後面的身體(右)) , 增加身體 EX : snakeBody.add(0, pos) , 移掉身體 EX : snakeBody.removeLast() ,更動LiveData的Value值 EX : body.postValue(snakeBody)
 */

/**
 * 12-7 方向控制,產生小蘋果,蛇吃蘋果程式設計
 * 1.   SnakeViewModel.kt ) 產生亂數隨機位置的蘋果 EX : fun generateApple(){ }
 *      亂數產生蘋果座標,0~19範圍 EX :  private lateinit var applePos: Position ; applePos = Position(Random.nextInt(20), Random.nextInt(20))
 * 2.   在剛開始啟動時就需要產生蘋果, 在 fun start()方法裡 , 添加 generateApple()
 * 3.   GameView.kt ) 蘋果座標初始化 EX : var apple : Position? = null
 *      蘋果畫筆實作 EX : private val paintApple = Paint().apply { color = Color.RED }
 *      畫出蘋果 EX : apple?.run{  drawRect((x * size).toFloat() + gap, (y * size).toFloat() + gap , ((x + 1) * size).toFloat() - gap , ((y + 1) * size).toFloat() - gap , paintApple) }
 * 4.   SnakeMainActivity.kt ) 監聽蘋果方法實作 EX : viewModel.apple.observe(this, Observer {  game_view.apple = it ; game_view.invalidate() })
 * 5.   SnakeViewModel.kt ) 實作蘋果是否增加蛇身體的程式設計 EX :  if ( pos != applePos ) { snakeBody.removeLast() } else {  generateApple()  }
 */

/**
 * 12-8 重完與計分,完成貪食蛇遊戲
 * 1.   SnakeViewModel ) 當吃到一蘋果增加100分數及添加分數屬性 EX : private var point: Int = 0 ; if ( pos != applePos ) { snakeBody.removeLast() } else { point += 100 ; score.postValue(point) ; generateApple() }
 * 2.   SnakeMainActivity ) 觀察監聽分數實作 EX : viewModel.score.observe(this, Observer {  score.setText(it.toString()) })
 * 3.   做一個畫面的邊界 : drwable > New > Drawable Resuource File > 命名border.xml , 添加下面標籤 , 並在content_snake_main.xml 的view元件的背景(background),修改成border.xml
 *        <shape xmlns:android="http://schemas.android.com/apk/res/android">
 *           <solid android:color="#19494746"/>
 *           <stroke android:width="2dp"/>
 *       </shape>
 * 4.  SnakeViewModel ) 當撞到蛇自己身體也會結束遊戲 EX : snakeBody.contains(this)
 */

class SnakeViewModel : ViewModel(){

    val body = MutableLiveData<List<Position>>()      // 蛇的身體
    val apple = MutableLiveData<Position>()           // 小蘋果
    val score = MutableLiveData<Int>()                // 分數
    val gameState = MutableLiveData<GameState>()      // 遊戲狀態( 正在遊戲 , 遊戲結束 )
    private val snakeBody = mutableListOf<Position>() // 蛇的身體區塊
    private var direction = Direction.LEFT            // 預設蛇移動的方向(向左)
    private lateinit var applePos: Position           // 蘋果座標(亂數)
    private var point: Int = 0                        // 右上角分數

    // 開始遊戲
    fun start() {
        // 起始畫面分數為0
        score.postValue(point)
        // 畫出蛇的身體區塊(snakeBody) , 假設日後還要對蛇的身體做其他事情,及使用.apply方法
        snakeBody.apply {
            add(Position(10 , 10))  // 將蛇身體區塊初始位置放在畫面中間處,蛇一開始身體區塊為橫向4格,
            add(Position(11 , 10))
            add(Position(12 , 10))
            add(Position(13 , 10))
        }.also {
            body.value = it              // 將蛇身體區塊初始位置,給予body屬性的值,就會改變body值
        }

        // 產生隨機蘋果方法
        generateApple()

        // FixedRateTimer 定時速率 (速率計時器) , fixedRateTimer("自定義名字",是不是daemon類型的,第一次啟動要停多久啟動(單位毫秒),重複動作時間(單位毫秒,時間越長,蛇動的越慢))
        fixedRateTimer("timer",true,500,500){
            // 找下一個位置動作程式碼
            val pos = snakeBody.first().copy().apply {
                when(direction){             // 蛇移動方向
                    Direction.LEFT -> x--    // 向左
                    Direction.RIGHT -> x++   // 向右
                    Direction.TOP -> y--     // 向上
                    Direction.DOWN -> y++    // 向下
                }
                // 判斷蛇撞牆(畫面邊界)條件式 , 20代表畫面有20個格子 , x代表牆的左右 , y代表牆的上下 , 當撞到蛇自己身體也會結束遊戲
                if(snakeBody.contains(this) || x < 0 || x >= 20 || y < 0 || y >= 20) {
                    cancel() // 當撞到邊界後停止(取消)動作 , fixedRateTimer身上的一個停止方法
                    gameState.postValue(GameState.GAME_OVER)
                }
            }

            // 蛇移動的反覆動作(蛇移動動畫)程式碼 (加蛇最前面的身體(左),移除最後面的身體(右))
            snakeBody.add(0, pos)     // 增加身體
            if ( pos != applePos ) {        // pos 有無等於蘋果位置
                snakeBody.removeLast()      // 移掉身體
            } else {
                point += 100                // 當吃到一蘋果增加100分數 , 100數字可自定義
                score.postValue(point)
                generateApple()             // 產生下一個位置蘋果
            }
            body.postValue(snakeBody)   // 更動LiveData的Value值 , 這行沒寫 , 蛇不會移動
        }
    }

    // 產生亂數隨機位置的蘋果
    fun generateApple(){
        // 寫法一 : do..while語法(至少會執行一次)
        do{
            applePos = Position(Random.nextInt(20), Random.nextInt(20)) // 亂數產生蘋果座標,0~19範圍
        } while (snakeBody.contains(applePos)) // 判斷這顆蘋果是否有重疊到蛇的身體上

    //        // 寫法二 :
    //        val spots = mutableListOf<Position>().apply {
    //            for (i in 0..19){
    //                for (j in 0..19){
    //                    add(Position(i , j))
    //                }
    //            }
    //        }
    //        spots.removeAll(snakeBody)
    //        spots.shuffle()
    //        applePos = spots[0]
        apple.postValue(applePos) // 在執行緒裡面盡量用postValue
    }

    // 重設遊戲
    fun reset() {

    }

    // 蛇移動
    fun move(dir: Direction) {
        direction = dir
    }
}

// 蛇的位置(橫向&直向(x & y)) , Position(座標位置)
data class Position(var x : Int , var y : Int)

// 上下左右移動列舉 , Direction(方向)
enum class Direction {
    TOP , DOWN , LEFT , RIGHT
}

// 遊戲的狀態 ( 正在遊戲 , 遊戲結束 )
enum class GameState{
    ONGOING , GAME_OVER
}