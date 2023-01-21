package jp.example.permission_move_to_setting_app_2

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
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
                    val toast =
                        Toast.makeText(applicationContext, "カメラを使用できます。", Toast.LENGTH_SHORT)
                    toast.show()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                    // 許可しなかった場合true,今後表示しないの場合false
                    requestPermissionLauncher.launch(
                        Manifest.permission.CAMERA
                    )
                }
                !shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                    showSettingsAppDialog()
                }
                else -> {
                    requestPermissionLauncher.launch(
                        Manifest.permission.CAMERA
                    )
                }
            }
        }
    }

    private fun showSettingsAppDialog() {
        val alertDialog: AlertDialog = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton("設定アプリへ",
                    DialogInterface.OnClickListener { dialog, id ->
                        startSettingApp()
                    })
                setNegativeButton("閉じる",
                    DialogInterface.OnClickListener { dialog, id ->
                        val toast =
                            Toast.makeText(applicationContext, "カメラの権限がありません。", Toast.LENGTH_SHORT)
                        toast.show()
                    })
            }
            builder.setMessage("カメラの権限がありません。設定アプリに移動して権限を許可してください。")

            builder.create()
        }
        alertDialog.show()
    }

    private fun startSettingApp() {
        val settingsIntent = Intent(
            android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:$packageName")
        )
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(settingsIntent)
    }
}