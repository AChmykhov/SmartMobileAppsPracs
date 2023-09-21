package ru.mirea.chmykhovam.favoritebook

//import android.R.attr.text
import android.content.Intent
import android.os.Bundle
//import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class ShareActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        val extras = intent.extras
        if (extras != null) {
            val ageView = findViewById<TextView>(R.id.textViewBook)
            val university = extras.getString(MainActivity.KEY)
//            Log.d(ShareActivity::class.java.simpleName, intent.getStringExtra(MainActivity.KEY).toString())
//            Log.d(ShareActivity::class.java.simpleName, university.toString())
            ageView.text = String.format("Мой любимая книга: %s", university)
        }
    }

    fun onClickSubmitBook(view: View) {
        val text = findViewById<EditText>(R.id.editTextText).text.toString()
        val data = Intent()
//        Log.d(ShareActivity::class.java.simpleName, text.toString())
        data.putExtra(MainActivity.USER_MESSAGE, text)
//        Log.d(ShareActivity::class.java.simpleName, data.getStringExtra(MainActivity.USER_MESSAGE).toString())
        setResult(RESULT_OK, data)
        finish()
    }
}