package ru.mirea.chmykhovam.cryptoloader

import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


class MyCrypto {
    companion object {
        @JvmStatic
        fun generateKey(): SecretKey? {
            return try {
                val sr = SecureRandom.getInstance("SHA1PRNG")
                sr.setSeed("any data used as random seed".toByteArray())
                val kg = KeyGenerator.getInstance("AES")
                kg.init(256, sr)
                SecretKeySpec(kg.generateKey().encoded, "AES")
            } catch (e: NoSuchAlgorithmException) {
                throw RuntimeException(e)
            }
        }

        @JvmStatic
        fun encryptMsg(message: String, secret: SecretKey?): ByteArray? {
            var cipher: Cipher? = null
            return try {
                cipher = Cipher.getInstance("AES")
                cipher.init(Cipher.ENCRYPT_MODE, secret)
                cipher.doFinal(message.toByteArray())
            } catch (e: NoSuchAlgorithmException) {
                throw java.lang.RuntimeException(e)
            } catch (e: NoSuchPaddingException) {
                throw java.lang.RuntimeException(e)
            } catch (e: InvalidKeyException) {
                throw java.lang.RuntimeException(e)
            } catch (e: BadPaddingException) {
                throw java.lang.RuntimeException(e)
            } catch (e: IllegalBlockSizeException) {
                throw java.lang.RuntimeException(e)
            }
        }

        @JvmStatic
        fun decryptMsg(
            cipherText: ByteArray?,
            secret: SecretKey?
        ): String? {/* Decrypt the message */
            return try {
                val cipher = Cipher.getInstance("AES")
                cipher.init(Cipher.DECRYPT_MODE, secret)
                cipher.doFinal(cipherText).toString()
            } catch (e: NoSuchAlgorithmException) {
                throw java.lang.RuntimeException(e)
            } catch (e: NoSuchPaddingException) {
                throw java.lang.RuntimeException(e)
            } catch (e: IllegalBlockSizeException) {
                throw java.lang.RuntimeException(e)
            } catch (e: BadPaddingException) {
                throw java.lang.RuntimeException(e)
            } catch (e: InvalidKeyException) {
                throw java.lang.RuntimeException(e)
            }
        }
    }
}