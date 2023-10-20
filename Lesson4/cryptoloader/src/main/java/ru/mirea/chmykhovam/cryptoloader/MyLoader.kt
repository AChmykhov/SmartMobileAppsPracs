package ru.mirea.chmykhovam.cryptoloader

import android.content.Context
import android.os.Bundle
import androidx.loader.content.AsyncTaskLoader
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


class MyLoader(context: Context, args: Bundle?) : AsyncTaskLoader<String?>(context) {
    private var firstName: String? = null
    private lateinit var args: Bundle

    init {
        if (args != null) {
            firstName = args.getString(ARG_WORD)
            this.args = args
        }
    }

    override fun onStartLoading() {
        super.onStartLoading()
        forceLoad()
    }

    override fun loadInBackground(): String? {
        // Обработка данных в Loader
        val cryptText: ByteArray? = args.getByteArray(ARG_WORD)
        val key: ByteArray? = args.getByteArray("key")
        // Восстановление ключав
        val originalKey: SecretKey = SecretKeySpec(key, 0, key!!.size, "AES")
        return MyCrypto.decryptMsg(cryptText, originalKey)
    }

    companion object {
        const val ARG_WORD = "word"
    }
}