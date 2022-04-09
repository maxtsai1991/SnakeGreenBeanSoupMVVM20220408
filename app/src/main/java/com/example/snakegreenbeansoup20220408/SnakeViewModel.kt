package com.example.snakegreenbeansoup20220408

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SnakeViewModel : ViewModel(){
    val body = MutableLiveData<List<Position>>() // 蛇的身體
    val apple = MutableLiveData<Position>()      // 小蘋果
    val score = MutableLiveData<Int>()           // 分數

    // 開始遊戲
    fun start() {

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