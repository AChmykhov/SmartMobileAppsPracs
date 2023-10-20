package ru.mirea.chmykhovam.cryptoloader

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import ru.mirea.chmykhovam.cryptoloader.databinding.ActivityMainBinding
import java.security.InvalidParameterException


class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<String> {
    lateinit var binding: ActivityMainBinding
    private var key = MyCrypto.generateKey()
    val TAG = this.javaClass.simpleName
    private val LoaderID = 1234
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onLoaderReset(loader: Loader<String?>) {
        Log.d(TAG, "onLoaderReset")
    }

    override fun onCreateLoader(i: Int, bundle: Bundle?): Loader<String?> {
        if (i == LoaderID) {
            Toast.makeText(this, "onCreateLoader:$i", Toast.LENGTH_SHORT).show()
            return MyLoader(this, bundle)
        }
        throw InvalidParameterException("Invalid loader id")
    }

    override fun onLoadFinished(loader: Loader<String?>, s: String) {
        if (loader.id == LoaderID) {
            Log.d(TAG, "onLoadFinished: $s")
            Toast.makeText(this, "onLoadFinished: $s", Toast.LENGTH_SHORT).show()
        }
    }

    fun onButtonClick(v: View) {
        val shiper = MyCrypto.encryptMsg(binding.editTextText.text.toString(), key)
        val bundle = Bundle()
        bundle.putByteArray(MyLoader.ARG_WORD, shiper)
        bundle.putByteArray("key", key!!.encoded)
        LoaderManager.getInstance(this).initLoader(LoaderID, bundle, this)
    }

}