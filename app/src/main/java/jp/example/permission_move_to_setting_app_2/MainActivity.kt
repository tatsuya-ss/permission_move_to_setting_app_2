package jp.example.permission_move_to_setting_app_2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import jp.example.permission_move_to_setting_app_2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                val toast = Toast.makeText(applicationContext, "カメラを使用できます。", Toast.LENGTH_SHORT)
                toast.show()
            } else {
                val toast = Toast.makeText(applicationContext, "カメラの権限がありません。", Toast.LENGTH_SHORT)
                toast.show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.cameraButton.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED -> {
                    Log.d("Tatsuya", "onCreate: PERMISSION_GRANTED")
                }
                shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                    // 許可しなかった場合true,今後表示しないの場合false
                    Log.d("Tatsuya", "onCreate: shouldShowRequestPermissionRationale")
                    requestPermissionLauncher.launch(
                        Manifest.permission.CAMERA
                    )
                }
                !shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                    Log.d("Tatsuya", "onCreate: 今後表示しないの時")
                    val intent = Intent(
                        android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:$packageName")
                    )
                    startActivity(intent)
                }
                else -> {
                    Log.d("Tatsuya", "onCreate: requestPermissionLauncher.launch")
                    requestPermissionLauncher.launch(
                        Manifest.permission.CAMERA
                    )
                }
            }
        }
    }
}