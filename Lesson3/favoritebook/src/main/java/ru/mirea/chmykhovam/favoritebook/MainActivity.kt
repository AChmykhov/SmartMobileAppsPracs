package ru.mirea.chmykhovam.favoritebook

import android.app.Activity
import android.content.Intent
import android.os.Bundle
//import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResult
//import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private var activityResultLauncher: ActivityResultLauncher<Intent>? = null
    companion object {
        val KEY: String = "book_name"
        val USER_MESSAGE: String = "MESSAGE"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textViewUserBook = findViewById<TextView>(R.id.textView)
//        val callback = ActivityResultCallback<ActivityResult>({
//            {result: ActivityResult ->
//                if (result.resultCode == Activity.RESULT_OK) {
//                    val data = result.data
//                    val userBook =
//                        "Название Вашей любимой книги: " + data!!.getStringExtra(USER_MESSAGE)
//                    textViewUserBook.text = userBook
//                }
//            }
//        })
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
//                Log.d(MainActivity::class.java.simpleName, data!!.getStringExtra(USER_MESSAGE).toString())
                val userBook =
                    "Название Вашей любимой книги: " + data!!.getStringExtra(USER_MESSAGE)
                textViewUserBook.text = userBook
            }
        }
    }

    fun onClickShareBook(view: View?) {
        val intent = Intent(this, ShareActivity::class.java)
        intent.putExtra(KEY, "Король Лир")
        activityResultLauncher!!.launch(intent)
    }
}