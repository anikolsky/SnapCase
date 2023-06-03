package com.omtorney.snapcase.common.presentation

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.azhon.appupdate.manager.DownloadManager
import com.omtorney.snapcase.BuildConfig
import com.omtorney.snapcase.R
import com.omtorney.snapcase.common.presentation.theme.SnapCaseTheme
import com.omtorney.snapcase.common.util.NotificationHelper
import com.omtorney.snapcase.common.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var versionResult: Resource<String>
    private val baseUrl = "http://188.225.60.116/"
    private val versionFile = "SnapCaseVersion.txt"
    private val apkUpdateFile = "SnapCaseApp.apk"
    private val apkDownloadedFileName = "SnapCaseUpdate.apk"
    private var downloadManager: DownloadManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val notificationHelper = NotificationHelper(applicationContext)
        notificationHelper.createNotificationChannel()

        lifecycleScope.launch {
            versionResult = withContext(Dispatchers.IO) {
                 getRemoteVersionCode()
            }
            when (versionResult) {
                is Resource.Success -> {
                    if (versionResult.data!!.toInt() > BuildConfig.VERSION_CODE) {
                        logd("Версия приложения на сервере: ${versionResult.data}")
                        showDialog()
                    }
                }
                is Resource.Error -> {
                    logd("Ошибка при проверке наличия обновления: ${versionResult.message}")
                }
                else -> {}
            }
        }

        setContent {
            SnapCaseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost(onGoToAppSettingsClick = ::openAppSettings)
                }
            }
        }
    }

    private fun getRemoteVersionCode(): Resource<String> {
        return try {
            val url = URL(baseUrl + versionFile)
            val connection = url.openConnection()
            connection.connectTimeout = 5000
            val inputStream = connection.getInputStream()
            val versionCode = inputStream.bufferedReader().use { it.readText().trim().toIntOrNull() }
            inputStream.close()
            Resource.Success(data = "$versionCode")
        } catch (e: Exception) {
            Resource.Error(message = e.localizedMessage ?: "Unexpected error")
        }
    }

    private fun showDialog() {
        AlertDialog.Builder(this@MainActivity)
            .setTitle("Доступно обновление")
            .setMessage("Пожалуйста, установите актуальную версию приложения")
            .setNegativeButton("Отмена") { dialog, _ -> dialog.cancel() }
            .setPositiveButton("Обновить") { _, _ -> updateMute() }
            .show()
    }

    private fun updateMute() {
        downloadManager = DownloadManager.Builder(this)
            .apkUrl(baseUrl + apkUpdateFile)
            .apkName(apkDownloadedFileName)
            .smallIcon(R.drawable.ic_round_case)
//            .onDownloadListener(object : OnDownloadListenerAdapter() {
//                override fun downloading(max: Int, progress: Int) {
//                    Toast.makeText(this@MainActivity, "$progress of $max downloaded", Toast.LENGTH_SHORT).show()
//                }
//            })
            .build()
        downloadManager?.download()
    }
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}

fun Any.logd(message: String) {
    Log.d("TESTLOG", "[${this.javaClass.simpleName}] ${Thread.currentThread().stackTrace[3].methodName}: $message")
}
