package com.example.patternlock_demo

import android.content.Intent
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
import java.lang.StringBuilder

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
    //创建一个数组保存临时的密码
    private val pwd = StringBuilder()
    //创建一个变量记录初始密码
    private var orgPwd:String?  = null
    //设置密码时的第一次密码保存
    private var firstPwd:String? = null
    //设置密码时的第2次密码保存
    private val secondPwd:String? = null

    //创建一个数组保存所有线的tag值
    val allLinesTag = arrayOf(
        12,23,45,56,78,89,//所有横着的线
        14,25,36,47,58,69,//所有竖着的线
        24,35,57,68,15,26,48,59//所有斜着的线
    )

    //创建一个变量保存最后一个触摸到的视图
    var lastSelectedView:ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        orgPwdIsNotExist()
        onLock()

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
                whetherToSetPwd()
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
    //点亮图片和保存当前的密码值
    private fun highLight(event: MotionEvent){
        findViewContainsPoint(changeLocation(event)).also {
            if (it != null && it.visibility == View.INVISIBLE){
                //首先判断是否为第一个被点亮的点，如果是，则直接点亮这个点；如果不是则需要与上次被点亮的点之间的线条点亮
                if(lastSelectedView == null){
                    it.visibility = View.VISIBLE
                    allSelectedDots.add(it)
                    pwd.append(it.tag.toString())
                    lastSelectedView = it
                }else{
                    val previews = lastSelectedView?.tag.toString().toInt()
                    val current = it.tag.toString().toInt()
                    //计算两点之间的tag值
                    val lineTag = if (previews > current) current*10+previews else previews*10+current
                    //判断有没有这两个点之间的线条，有就点亮
                    if (allLinesTag.contains(lineTag)){
                        mContainer.findViewWithTag<ImageView>(lineTag.toString()).apply {
                            //点亮这条线
                            visibility = View.VISIBLE
                            //点亮这个点
                            it.visibility = View.VISIBLE
                            //把选中的这个点加到容器中方便删除
                            allSelectedDots.add(it)
                            //把选中的这条线加到容器中方便删除
                            allSelectedDots.add(this)
                            pwd.append(it.tag.toString())
                            lastSelectedView = it
                        }
                    }
                }

            }
        }
    }
    //清空allSelectedDots 和熄灭图片 清除密码 清除上次使用时的最后一个视图记录
    private fun reset(){
        for(item in allSelectedDots){
            item.visibility = View.INVISIBLE
        }
        allSelectedDots.clear()
        pwd.clear()
        lastSelectedView = null
    }
    //判断是否存在密码
    fun orgPwdIsNotExist():Boolean{
        //获取密码
        SharedPreferenceUtil.getInstance(this).getPassword().also {
            if (it == null){
                textView2.text = "请设置密码："
                return true
            }else{
                orgPwd = it
                textView2.text = "请输入解锁密码图案"
                return false
            }
        }
    }
    //判断是否需要设置密码
    fun whetherToSetPwd(){
        if (orgPwdIsNotExist()){
            //是否为第一次设置密码
            if (firstPwd == null){
                firstPwd = pwd.toString()
            }else{
                //确认密码
                if (firstPwd == pwd.toString()){
                    textView2.text = "设置密码成功！"
                    SharedPreferenceUtil.getInstance(this).savePassword(pwd.toString())

                }else{
                    textView2.text = "两次输入不一致，设置失败！"
                    firstPwd =null
                    whetherToSetPwd()
                }
            }
        }
    }
    //解锁界面
fun onLock(){
    textView2.text = "请输入解锁密码图案"
    if(pwd.toString() == SharedPreferenceUtil.getInstance(this).getPassword()){
        startActivity(Intent(this,TurnActivity::class.java))
    }else{
        textView2.text = "密码错误"
    }
}
}

