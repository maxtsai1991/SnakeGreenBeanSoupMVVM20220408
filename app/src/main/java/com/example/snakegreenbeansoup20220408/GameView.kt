package com.example.snakegreenbeansoup20220408

import android.content.Context
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

}