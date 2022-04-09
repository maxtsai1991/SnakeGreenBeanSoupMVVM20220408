package com.example.snakegreenbeansoup20220408

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SnakeViewModel : ViewModel(){
    val body = MutableLiveData<List<Position>>() // 蛇的身體
    val apple = MutableLiveData<Position>()      // 小蘋果
    val score = MutableLiveData<Int>()           // 分數
    private val snakeBody = mutableListOf<Position>() // 蛇的身體區塊

    // 開始遊戲
    fun start() {
        // 畫出蛇的身體區塊(snakeBody) , 假設日後還要對蛇的身體做其他事情,及使用.apply方法
        snakeBody.apply {
            add(Position(10 , 10))  // 將蛇身體區塊初始位置放在畫面中間處,蛇一開始身體區塊為橫向4格,
            add(Position(11 , 10))
            add(Position(12 , 10))
            add(Position(13 , 10))
        }.also {
            body.value = it              // 將蛇身體區塊初始位置,給予body屬性的值,就會改變body值
        }
    }

    // 重設遊戲
    fun reset() {

    }

    // 蛇移動
    fun move(dir: Direction) {

    }
}

// 蛇的位置(橫向&直向(x & y)) , Position(座標位置)
data class Position(val x : Int , val y : Int)

// 上下左右移動列舉 , Direction(方向)
enum class Direction {
    TOP , DOWN , LEFT , RIGHT
}