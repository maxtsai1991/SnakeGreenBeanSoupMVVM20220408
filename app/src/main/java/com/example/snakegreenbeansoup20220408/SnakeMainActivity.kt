package com.example.snakegreenbeansoup20220408

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.snakegreenbeansoup20220408.databinding.ActivitySnakeMainBinding
import kotlinx.android.synthetic.main.content_snake_main.*

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

/**
 *   12-3 蛇怎麼畫出來的 ? 從ViewModel ,因為ViewModel是資料 (EX : viewModel.start())
 *      1.  準備蛇的資料(viewModel.start() = SnakeViewModel.kt > start()方法) ,當呼叫start()方法時 , 就要準備資料 , 改變body屬性的值(EX : val body = MutableLiveData<List<Position>>()),
 *          所以要保存蛇的身體區塊,應該要設計一個內部專用的一個集合資料EX : private val snakeBody = mutableListOf<Position>()
 *      2.  假設畫面的橫向跟直向各20格(x有20,y有20) ,將蛇身體區塊初始位置放在畫面中間處 , 蛇一開始身體區塊為橫向4格 , 在Start()方法裡 , 假設日後還要對蛇的身體做其他事情 , 及使用.apply方法 , 而且要改變body屬性值 , 所以在使用also的語法
 *          EX :  snakeBody.apply {  add(Position(10 , 10)) ; add(Position(11 , 10)) ; add(Position(12 , 10)) ; add(Position(13 , 10)) ; }.also { body.value = it }
 *      3. 當改變body屬性值之後, SnakeMainActivity.kt的 viewModel.body.observe(this, Observer { })方法會收到,因為有監聽,會收到it(等於List<Position>)之後在傳遞給別人,如何傳遞給別人?A:畫它的人要負責,所以它是game_view,
 *         game_view身上也要有snakeBody , 所以將剛剛收到的itit(等於List<Position>)在傳遞給game_view的snakeBody , 讓game_view自己畫 EX : game_view.snakeBody = it ,
 *         當game_view身上有變動時,需要重畫身上的內容,所以呼叫.invalidate()方法
 *      4. 創建剛剛的.snakeBody方法在GameView.kt(燈泡熱鍵 > 選Create member property 'GameView.snakeBody'),一開始沒有值,開始玩遊戲之後才會有這個值 EX : var snakeBody: List<Position>? = null
 *      5. 要讓一個客製化的view,身上可以畫出自己想要的東西,需覆寫onDraw方法 , canvas參數就是繪圖的元件 EX : override fun onDraw(canvas: Canvas?) { }
 *      6. 接下來客製化元件再覆寫onLayout方法 EX : override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) { } , 當想得知畫面總寬度是多少,但不能訂死每一個格子的寬度 , 所以這時候訂定一個size屬性 EX : var size = 0
 *      7. 接下來在回到onDraw方法裡面 , 如果canvas不是null的時候 , 執行run裡面的事件 , 才做後續的事情,所以canvas?呼叫run方法 ,如果snakeBody不是null才做後續畫身體的工作,當我不是null就進行一個一個一個的取出,
 *         因為蛇初始化雖然是4格,但之後可能會變長,所以用forEach方式來做,每一個都是it(等於Position),要畫正方形所以用drawRect,drawRect(區塊的左浮點數,上浮點數,右浮點數,下浮點數,及畫筆)
 *         EX : canvas?.run {  snakeBody?.forEach { drawRect((it.x * size).toFloat(), (it.y * size).toFloat(), ((it.x + 1) * size).toFloat(), ((it.y + 1) * size).toFloat(), paint)  }  }
 *      8. 準備畫筆的顏色 , 因為後續還要對畫筆做其他事,所以用.applyEX : private val paint = Paint().apply { color = Color.BLACK }
 */

/**
 *   12-6 邊界的判斷程式設計
 *      1. SnakeViewModel.kt )判斷蛇撞牆(畫面邊界)條件式 , 20代表畫面有20個格子 , x代表牆的左右 , y代表牆的上下 EX : if(x < 0 || x > 20 || y < 0 || y > 20) { }
 *      2. 當撞牆後告知已經結束 , 創建一遊戲屬性,並且裡面裝列舉型態( 正在遊戲 , 遊戲結束 ) EX : val gameState = MutableLiveData<GameState>() ; 遊戲狀態列舉型態 : enum class GameState{ ONGOING , GAME_OVER }
 *      3. 讓SnakeMainActivity監聽觀察遊戲狀態 , 當SnakeViewModel的遊戲狀態改變值時 EX : (SnakeViewModel)gameState.postValue(GameState.GAME_OVER) , 要用postValue方法,而不是Value方法  ; (SnakeMainActivity) viewModel.gameState.observe(this, Observer { })
 *      4. 增加遊戲結束的彈窗 EX :  AlertDialog.Builder(this@SnakeMainActivity) .setTitle("貪食蛇遊戲(SnakeGame)").setMessage("遊戲結束(Game Over)").setPositiveButton("OK" , null).show()
 *      5. SnakeMainActivity )增加上下左右按鈕的監聽器 EX :  top.setOnClickListener { viewModel.move(Direction.TOP) } ... 等等
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
            game_view.snakeBody = it
            game_view.invalidate() // 畫出蛇身體

        })

        // 觀察/監聽身上的分數
        viewModel.score.observe(this, Observer {
            score.setText(it.toString())
        })

        //  觀察/監聽遊戲狀態 , 當蛇撞到四邊牆(上下左右)時,跳出對話框
        viewModel.gameState.observe(this, Observer { gameState ->
            if(gameState == GameState.GAME_OVER){
                AlertDialog.Builder(this@SnakeMainActivity)
                    .setTitle("貪食蛇遊戲(SnakeGame)")
                    .setMessage("遊戲結束(Game Over)")
                    .setPositiveButton("OK" , null)
                    .show()
            }
        })

        // 觀察/監聽小蘋果(小紅點)
        viewModel.apple.observe(this, Observer {
            game_view.apple = it
            game_view.invalidate() // 畫出蘋果
        })

        // 開始貪食蛇遊戲(畫出蛇的身體)
        viewModel.start()

        // 上下左右按鈕的監聽器
        top.setOnClickListener { viewModel.move(Direction.TOP) }
        down.setOnClickListener { viewModel.move(Direction.DOWN) }
        left.setOnClickListener { viewModel.move(Direction.LEFT) }
        right.setOnClickListener { viewModel.move(Direction.RIGHT) }
    }


}