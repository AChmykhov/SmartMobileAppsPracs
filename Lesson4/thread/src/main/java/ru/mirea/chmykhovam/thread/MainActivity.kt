package ru.mirea.chmykhovam.thread

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.mirea.chmykhovam.thread.databinding.ActivityMainBinding
import java.util.Arrays


class MainActivity : AppCompatActivity() {
    private var counter = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())
        val infoTextView = binding.textView
//        val infoTextView = findViewById<TextView>(R.id.textView)
        val mainThread = Thread.currentThread()
        infoTextView.text = "Имя текущего потока: " + mainThread.name
// Меняем имя и выводим в текстовом поле
        mainThread.name = "МОЙ НОМЕР ГРУППЫ: 05, НОМЕР ПО СПИСКУ: 31, МОЙ ЛЮБИМЫЙ ФИЛЬМ: John Wick"
        infoTextView.append("""Новое имя потока: ${mainThread.name}""")
        Log.d(
            MainActivity::class.java.simpleName, "Stack: " + Arrays.toString(mainThread.stackTrace)
        )
//        binding.buttonMirea.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(v: View) {
//                val endTime = System.currentTimeMillis() + 20 * 1000
//                while (System.currentTimeMillis() < endTime) {
//                    synchronized(this) {
//                        try {
//                            Thread.sleep(endTime - System.currentTimeMillis())
//                        } catch (e: Exception) {
//                            throw RuntimeException(e)
//                        }
//                    }
//                }
//            }
//        })
        binding.buttonMirea.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                Thread(object : Runnable {
                    override fun run() {
                        val numberThread = counter++
                        Log.d(
                            "ThreadProject", String.format(
                                "Запущен поток No %d студентом группы No %s номер по списку No %d ",
                                numberThread,
                                "БСБО-05-21",
                                31
                            )
                        )
                        val endTime = System.currentTimeMillis() + 20 * 1000
                        while (System.currentTimeMillis() < endTime) {
                            synchronized(this) {
                                try {
                                    Thread.sleep(endTime - System.currentTimeMillis())
                                    Log.d(
                                        MainActivity::class.java.simpleName, "Endtime: $endTime"
                                    )
                                } catch (e: Exception) {
                                    throw RuntimeException(e)
                                }
                            }
                            Log.d("ThreadProject", "Выполнен поток No " + numberThread)
                        }
                    }
                }).start()
            }
        })
        Log.d(MainActivity::class.java.simpleName, "Group: " + mainThread.threadGroup)
        binding.btnCalc.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                Thread(object : Runnable {
                    override fun run() {
                        val days = binding.editTextDays.text.toString().toIntOrNull()
                        val lessons = binding.editTextLessons.text.toString().toIntOrNull()
                        if ((days != null) or (lessons != null)){
                            val total = lessons!!/days!!
                            runOnUiThread{infoTextView.append(String.format("В среднем пар в день:%f", total))}
                        } else{
                            runOnUiThread{Toast.makeText(applicationContext, "Вы не ввели числа!", Toast.LENGTH_LONG).show()}
                        }


                    }
                }).start()
            }
        })
    }

}