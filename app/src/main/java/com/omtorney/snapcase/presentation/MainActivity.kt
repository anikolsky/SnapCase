package com.omtorney.snapcase.presentation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.azhon.appupdate.manager.DownloadManager
import com.omtorney.snapcase.presentation.common.PermissionDialog
import com.omtorney.snapcase.presentation.common.WriteStoragePermissionTextProvider
import com.omtorney.snapcase.presentation.ui.theme.SnapCaseTheme
import com.omtorney.snapcase.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.net.URL

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val baseUrl = "http://188.225.60.116/"
    private val versionFile = "version.txt"
    private val apkUpdateFile = "app3.apk"
    private val apkDownloadFile = "appUpdate.apk"
    private var downloadManager: DownloadManager? = null
    private val viewModel by viewModels<MainViewModel>()
    private val permissionsToRequest = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private lateinit var multiplePermissionResultLauncher: ActivityResultLauncher<Array<String>>



    private lateinit var versionResult: Resource<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        multiplePermissionResultLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissionsToRequest.forEach { permission ->
                viewModel.onPermissionResult(
                    permission = permission,
                    isGranted = permissions[permission] == true
                )
            }
        }

        runBlocking {
            withContext(Dispatchers.IO) {
                versionResult = getRemoteVersionCode()
            }
        }

        when (versionResult) {
            is Resource.Success -> {
                Toast.makeText(this, "Remote version is: ${versionResult.data}", Toast.LENGTH_SHORT).show()
//                if (versionResult.data!!.toInt() >= 2) {
//                    downloadManager = DownloadManager.Builder(this).run {
//                        apkUrl(baseUrl + apkUpdateFile)
//                        apkName(apkDownloadFile)
//                        smallIcon(R.drawable.ic_round_case)
//                        build()
//                    }
//                    downloadManager!!.download()
//                }
            }
            is Resource.Error -> {
                Toast.makeText(this, "Error: ${versionResult.message}", Toast.LENGTH_SHORT).show()
            }
            is Resource.Loading -> {}
        }

        setContent {
            SnapCaseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val dialogQueue = viewModel.visiblePermissionDialogQueue
                    AppNavHost()
                    multiplePermissionResultLauncher.launch(permissionsToRequest)
                    dialogQueue
                        .reversed()
                        .forEach { permission ->
                            PermissionDialog(
                                permissionTextProvider = when (permission) {
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                                        WriteStoragePermissionTextProvider()
                                    }
                                    else -> return@forEach
                                },
                                isPermanentlyDeclined = !shouldShowRequestPermissionRationale(permission),
                                onDismiss = viewModel::dismissDialog,
                                onOkClick = {
                                    viewModel.dismissDialog()
                                    multiplePermissionResultLauncher.launch(arrayOf(permission))
                                },
                                onGoToAppSettingsClick = ::openAppSettings
                            )
                        }
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
            Resource.Success(data = versionCode.toString())
        } catch (e: Exception) {
            Resource.Error(message = e.message.toString())
        }
    }
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}