package ru.mirea.chmykhovam.looper

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log

class MyLoooper : Thread("Custom Thread") {
    lateinit var mHandler: Handler
    override fun run() {
        Looper.prepare()
        mHandler = object : Handler(Looper.myLooper()!!) {
            override fun handleMessage(msg: Message) {
                val profession = msg.data.getString("KEY")
                val age = msg.data.getInt("AGE")
                Log.d(
                    MyLoooper::class.java.simpleName,
                    "Inside handler, your profession is $profession, you are $age years old. Starting sleep"
                )
                Thread.sleep((age*1000).toLong())
                Log.d(MyLoooper::class.java.simpleName, "Finished sleep, your age is $age")
            }
        }
//        mHandler = object : Handler(Looper.myLooper()!!) {
//            override fun handleMessage(msg: Message) {
//                // process incoming messages here
//            }
//        }
        Looper.loop()
    }
}
