package com.example.omikuji

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.fortune.*
import kotlinx.android.synthetic.main.omikuji.*

class OmikujiActivity : AppCompatActivity(), SensorEventListener {

    lateinit var manager: SensorManager

    val omikujiShelf = Array<OmikujiParts>(20)
        { OmikujiParts(R.drawable.result2, R.string.contents1) }

    var omikujiNumber = -1

    val omikujiBox = OmikujiBox()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.omikuji)

        manager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // 設定反映
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val value = pref.getBoolean("button", false)

        button.visibility = if (value) View.VISIBLE else View.INVISIBLE

        // おみくじの初期化
        omikujiBox.omikujiView = imageView

        omikujiShelf[0].drawID = R.drawable.result1
        omikujiShelf[0].fortuneID = R.string.contents2

        omikujiShelf[1].drawID = R.drawable.result3
        omikujiShelf[1].fortuneID = R.string.contents9

        omikujiShelf[2].fortuneID = R.string.contents3
        omikujiShelf[3].fortuneID = R.string.contents4
        omikujiShelf[4].fortuneID = R.string.contents5
        omikujiShelf[5].fortuneID = R.string.contents6
    }


    fun onButtonClick(v: View) {
        omikujiBox.shake()
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            if (omikujiNumber < 0 && omikujiBox.finish) {
                drawResult()
            }
        }
        return super.onTouchEvent(event)
    }


    fun drawResult() {
        // 棚番号を生成
        omikujiNumber = omikujiBox.number

        // 結果を抽出
        val op = omikujiShelf[omikujiNumber]

        // 結果画面に移行
        setContentView(R.layout.fortune)

        // 画像と結果文字列を設定
        imageView2.setImageResource(op.drawID)
        textView.setText(op.fortuneID)
    }


    /*メニューの設定*/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val toast = Toast.makeText(this, item?.title, Toast.LENGTH_LONG)
//        toast.show()
        if(item?.itemId == R.id.item1) {
            val intent = Intent(this, OmikujiPreferenceActivity::class.java)
            startActivity(intent)
        }
        else {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onPause() {
        super.onPause()
        manager.unregisterListener(this)
    }


    override fun onResume() {
        super.onResume()
        val sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val value = event?.values?.get(0)
        if (value != null && 10 < value) {
            val toast = Toast.makeText(this, "加速度 : ${value}", Toast.LENGTH_LONG)
            toast.show()
        }
    }
}