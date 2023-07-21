package com.omtorney.snapcase.presentation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.azhon.appupdate.listener.OnDownloadListenerAdapter
import com.azhon.appupdate.manager.DownloadManager
import com.google.android.gms.auth.api.identity.Identity
import com.omtorney.snapcase.BuildConfig
import com.omtorney.snapcase.R
import com.omtorney.snapcase.presentation.components.UpdateDialog
import com.omtorney.snapcase.presentation.theme.SnapCaseTheme
import com.omtorney.snapcase.util.NotificationHelper
import com.omtorney.snapcase.util.Resource
import com.omtorney.snapcase.firebase.auth.GoogleAuthUiClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var versionResult: Resource<String>
    private val baseUrl = BuildConfig.SERVER_ADDRESS
    private val versionFile = BuildConfig.VERSION_FILE
    private val apkUpdateFile = BuildConfig.APK_UPDATE
    private val apkDownloadedFileName = BuildConfig.DOWNLOADED_UPDATE
    private var downloadManager: DownloadManager? = null
    private var downloadProgress by mutableFloatStateOf(0f)
    private var openDialog by mutableStateOf(false)

    private lateinit var navController: NavHostController
    private var isIntentHandled = false

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(Identity.getSignInClient(applicationContext))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val notificationHelper = NotificationHelper(applicationContext)
        notificationHelper.createNotificationChannel()

        lifecycleScope.launch {
            versionResult = withContext(Dispatchers.IO) {
                fetchRemoteVersionCode()
            }
            when (versionResult) {
                is Resource.Success -> {
                    if (versionResult.data!!.toInt() > BuildConfig.VERSION_CODE) {
                        openDialog = true
                        logd("Версия приложения на сервере: ${versionResult.data}")
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
                    navController = rememberNavController()
                    val mainViewModel = hiltViewModel<MainViewModel>()
                    val accentColor by mainViewModel.accentColor.collectAsStateWithLifecycle()
                    val signInState = mainViewModel.signInState.collectAsStateWithLifecycle().value
                    val launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartIntentSenderForResult(),
                        onResult = { result ->
                            if (result.resultCode == RESULT_OK) {
                                lifecycleScope.launch {
                                    val signInResult = googleAuthUiClient.signInWithIntent(
                                        intent = result.data ?: return@launch
                                    )
                                    mainViewModel.onSignInResult(signInResult)
                                }
                            }
                        }
                    )
                    AppNavHost(
                        navController = navController,
                        accentColor = accentColor,
                        signInState = signInState,
                        userData = googleAuthUiClient.getSignedInUser(),
                        onAppSettingsClick = ::openAppSettings,
                        onSignInClick = { signIn(launcher) },
                        onSignOutClick = { signOut() },
                        onResetState = { mainViewModel.resetState() }
                    )
                    if (openDialog && downloadProgress != 1f) {
                        UpdateDialog(
                            downloadProgress = downloadProgress,
                            onOkClick = { updateApp() },
                            onDismissClick = { openDialog = false }
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isIntentHandled && ::navController.isInitialized) {
            handleNotificationIntent(intent)
            isIntentHandled = true
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleNotificationIntent(intent)
    }

    private fun fetchRemoteVersionCode(): Resource<String> {
        return try {
            val url = URL(baseUrl + versionFile)
            val connection = url.openConnection()
            connection.connectTimeout = 5000
            val inputStream = connection.getInputStream()
            val versionCode = inputStream.bufferedReader().use { it.readText().trim().toIntOrNull() }
            inputStream.close()
            Resource.Success(data = "$versionCode")
        } catch (e: IOException) {
            Resource.Error(message = "Ошибка при запросе версии обновления: ${e.localizedMessage}")
        }
    }

    private fun updateApp() {
        downloadManager = DownloadManager.Builder(this)
            .apkUrl(baseUrl + apkUpdateFile)
            .apkName(apkDownloadedFileName)
            .smallIcon(R.drawable.ic_round_case)
            .showNotification(false)
            .showBgdToast(false)
            .onDownloadListener(object : OnDownloadListenerAdapter() {
                override fun downloading(max: Int, progress: Int) {
                    downloadProgress = progress.toFloat() / max.toFloat()
                }
            })
            .build()
        downloadManager?.download()
    }

    private fun handleNotificationIntent(intent: Intent?) {
        if (intent?.hasExtra("url") == true) {
            val url = intent.getStringExtra("url")
            val number = intent.getStringExtra("number")
            val hearingDateTime = intent.getStringExtra("hearingDateTime")
            val actDateForce = intent.getStringExtra("actDateForce")
            val actTextUrl = intent.getStringExtra("actTextUrl")
            val courtTitle = intent.getStringExtra("courtTitle")

            val detailScreenRoute = Screen.Detail.route +
                            "?url=${Uri.encode(url)}" +
                            "&number=${Uri.encode(number)}" +
                            "&hearingDateTime=${Uri.encode(hearingDateTime)}" +
                            "&actDateForce=${Uri.encode(actDateForce)}" +
                            "&actTextUrl=${Uri.encode(actTextUrl)}" +
                            "&courtTitle=${Uri.encode(courtTitle)}"

            val detailScreenIntent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra("route", detailScreenRoute)
            }
            startActivity(detailScreenIntent)
            finish()

//            navController.currentBackStackEntry?.let { currentScreen ->
//                navController.navigate(
//                    Screen.Detail.route +
//                            "?url=${Uri.encode(url)}" +
//                            "&number=${Uri.encode(number)}" +
//                            "&hearingDateTime=${Uri.encode(hearingDateTime)}" +
//                            "&actDateForce=${Uri.encode(actDateForce)}" +
//                            "&actTextUrl=${Uri.encode(actTextUrl)}" +
//                            "&courtTitle=${Uri.encode(courtTitle)}"
//                ) {
//                    popUpTo(currentScreen.id) { inclusive = true }
//                }
//            }
        }
    }

    private fun signIn(launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>) {
        lifecycleScope.launch {
            val signInIntentSender = googleAuthUiClient.signIn()
            launcher.launch(
                IntentSenderRequest.Builder(
                    signInIntentSender ?: return@launch
                ).build()
            )
        }
    }

    private fun signOut() {
        lifecycleScope.launch {
            googleAuthUiClient.signOut()
            Toast.makeText(applicationContext,"Signed out", Toast.LENGTH_LONG).show()
        }
    }
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}

fun Any.logd(message: String) {
    val tag = "TESTLOG"
    val className = "[${this.javaClass.simpleName}]"
    val methodName = Thread.currentThread().stackTrace[3].methodName
    Log.d(tag, "$className $methodName: $message")
}
