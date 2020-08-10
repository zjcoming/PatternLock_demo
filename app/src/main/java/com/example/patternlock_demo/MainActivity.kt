package com.example.patternlock_demo

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    //使用懒加载创建数组保存点dot
    val dots: Array<ImageView> by lazy {
        arrayOf(dot1, dot2, dot3, dot4, dot5, dot6, dot7, dot8, dot9)
    }

    //使用懒加载获取标题栏和状态栏的高度(只用加载一次)
    val barHeight: Int by lazy {
        //获取屏幕尺寸
        val display = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(display)

        //获取操作区域的尺寸
        val drawingRect = Rect()
        window.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT).getDrawingRect(drawingRect)

        display.heightPixels - drawingRect.height()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {

            }
            MotionEvent.ACTION_MOVE -> {

            }
            MotionEvent.ACTION_UP -> {

            }

        }
            return true
    }
    //将触摸点的坐标转化为相对于容器的坐标
    private fun changeLocation(event: MotionEvent?){

    }
}

