package com.example.patternlock_demo

import android.graphics.Point
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.core.view.GravityCompat.apply
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    //使用懒加载创建数组保存点dot
    val dots: Array<ImageView> by lazy {
        arrayOf(sDot1, sDot2, sDot3, sDot4, sDot5, sDot6,sDot7, sDot8, sDot9)
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

    //创建一个数组保存已经点亮的点
    val allSelectedDots = mutableListOf<ImageView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                highLight(event)
            }
            MotionEvent.ACTION_MOVE -> {
                highLight(event)
            }
            MotionEvent.ACTION_UP -> {
                reset()
            }

        }
            return true
    }
    //将触摸点的坐标转化为相对于容器的坐标
    private fun changeLocation(event: MotionEvent):Point{
        val point = Point().apply {
            x = (event.x - mContainer.x).toInt()
            y = (event.y - barHeight - mContainer.y).toInt()
        }.also {
            return it
        }
    }
    //判断当前的点是否在视图里面
    private fun findViewContainsPoint(point: Point):ImageView?{
        for(dotView in dots){
            //判断这个视图是否包含点
            getRectForView(dotView).also {
                if(it.contains(point.x,point.y)){
                    return dotView
                }
            }
        }
        return null
    }
    //获取视图对应的Rect（）
    private fun getRectForView(v:ImageView):Rect = Rect(v.left,v.top,v.right,v.bottom)
    //点亮图片
    private fun highLight(event: MotionEvent){
        findViewContainsPoint(changeLocation(event)).also {
            if (it != null){
                it.visibility = View.VISIBLE
                allSelectedDots.add(it)
            }
        }
    }
    //清空allSelectedDots 和熄灭图片
    private fun reset(){
        for(item in allSelectedDots){
            item.visibility = View.INVISIBLE
        }
        allSelectedDots.clear()
    }
}

