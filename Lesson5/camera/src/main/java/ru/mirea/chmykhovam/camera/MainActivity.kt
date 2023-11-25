package ru.mirea.chmykhovam.camera

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import ru.mirea.chmykhovam.camera.databinding.ActivityMainBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE_PERMISSION = 200
    private var isWork = false
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageUri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val callback: ActivityResultCallback<ActivityResult?> =
            object : ActivityResultCallback<ActivityResult?> {
                override fun onActivityResult(result: ActivityResult?) {
                    if (result == null) return
                    if (result.resultCode == Activity.RESULT_OK) {
//                        val data: Intent? = result.data
                        binding.imageView.setImageURI(imageUri)
                    }
                }
            }

        val cameraActivityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult(), callback
        )
        binding.imageView.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            // проверка разрешений
            val cameraPermission =
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_PERMISSION
                )
            } else {
                isWork = true
            }
            // проверили
            if (isWork) {
                try {
                    val photoFile = createImageFile()
                    val authorities = applicationContext.packageName + ".fileprovider"
                    imageUri = FileProvider.getUriForFile(this@MainActivity, authorities, photoFile!!)
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                    cameraActivityResultLauncher.launch(cameraIntent)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
// permission granted
            isWork =
                permissions.contains(Manifest.permission.CAMERA) && grantResults[permissions.indexOf(
                    Manifest.permission.CAMERA
                )] == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * Производится генерирование имени файла на основе текущего времени и создание файла
     * в директории Pictures на ExternelStorage.
     * class.
     * @return File возвращается объект File .
     * @exception IOException если возвращается ошибка записи в файл
     */
    @Throws(IOException::class)
    private fun createImageFile(): File? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        val imageFileName = "IMAGE_" + timeStamp + "_"
        val storageDirectory: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(imageFileName, ".jpg", storageDirectory)
    }
}