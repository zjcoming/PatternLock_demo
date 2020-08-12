package com.example.patternlock_demo

import android.content.Context
import android.content.SharedPreferences


//用到单例设计模式私有化构造函数
class SharedPreferenceUtil private constructor(){
    companion object{
        private val FILE_NAME = "password"
        private val KEY = "passwordKey"
        private var instance: SharedPreferenceUtil? = null
        private  var mContext:Context? = null
        public fun getInstance(context: Context):SharedPreferenceUtil{
            mContext = context
            if (instance==null){
                synchronized(this){
                    instance = SharedPreferenceUtil()
                }
            }
            return instance!!
        }
    }
    //通过sharedPreference来写和读数据
    public fun savePassword(pwd: String){
        //获取sharedPreference对象
        val sharedPreferences = mContext?.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        //获取edit对象来写入数据
        val edit :SharedPreferences.Editor? = sharedPreferences?.edit()
        //写入数据
        edit?.putString(KEY,pwd)
        //提交
        edit?.apply()
    }
    public fun getPassword():String?{
        //获取sharedPreference对象
        val sharedPreferences = mContext?.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences?.getString("KEY",null)
    }

}