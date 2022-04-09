package com.example.snakegreenbeansoup20220408

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 *  學對之路:Kotlin Android APP 開發 貪食蛇遊戲MVVM
 *    12-2 客製元件設計Custom view,如何擴充現有元件
 *    1.  snakegreenbeansoup20220408新建GameView.kt (New > Kotlin File/Class)
 *    2.  繼承View EX : class GameView(context: Context , attrs: AttributeSet) : View(context, attrs) { }
 *    3.  新增客製化元件 : content_snake_main.xml > 新增/拖曳一個<view>元件 > 打上GameView > 並放置在分格線上方 , 並修改ID : game_view , 以及將view修改成正方形(右邊工具列找尋layout_constraintDimensionRatio)EX : app:layout_constraintDimensionRatio="1:1"
 *
 */
class GameView(context: Context , attrs: AttributeSet) : View(context, attrs) {

    var snakeBody: List<Position>? = null
    var size = 0
    private val paint = Paint().apply { color = Color.BLACK } // 畫筆的顏色 , 因為後續還要對畫筆做其他事,所以用.apply

    // 為了畫圖,覆寫(快捷鍵Ctrl + o) , canvas參數就是繪圖的元件
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.run {
            // 如果snakeBody不是null才做後續畫身體的工作,當我不是null就進行一個一個一個的取出,因為蛇初始化雖然是4格,但之後可能會變長,所以用forEach方式來做,每一個都是it(等於Position),要畫正方形所以用drawRect,drawRect(區塊的左浮點數,上浮點數,右浮點數,下浮點數,及畫筆)
            snakeBody?.forEach {
                drawRect((it.x * size).toFloat(), (it.y * size).toFloat(), ((it.x + 1) * size).toFloat(), ((it.y + 1) * size).toFloat(), paint)
            }
        }
    }

    // 為了畫面上寬高是多少,覆寫(快捷鍵Ctrl + o),會知道寬度高度,順便可以改變現在知道的寬度高度是多少
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        size = width / 20 // 可以得到寬度是多少 , getWidth()得到元件寬度 , getWidth在Kotlin裡面直接用width即可 , 除20是因為畫面寬總共有20格
    }
}