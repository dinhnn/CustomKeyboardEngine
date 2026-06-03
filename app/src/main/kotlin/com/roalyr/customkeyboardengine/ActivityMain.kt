package com.roalyr.customkeyboardengine

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roalyr.customkeyboardengine.ui.theme.CustomKeyboardEngineTheme
import java.io.File

/**
 * Main activity for the Custom Keyboard Engine.
 * Provides a UI for granting permissions, changing input methods, and copying default layouts/settings.
 */
class ActivityMain : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CustomKeyboardEngineTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()) // Enable scrolling
                            .padding(horizontal = 24.dp)
                            .padding(WindowInsets.systemBars.asPaddingValues()) // Respect notification bar
                            .imePadding()
                        , // Adjust for keyboard
                        verticalArrangement = Arrangement.spacedBy(8.dp), // Uniform spacing
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Spacer for Title
                        Spacer(modifier = Modifier.height(16.dp)) // Add initial padding for safe area



                        Button(
                            onClick = { requestOverlayPermission() },
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text(stringResource(R.string.btn_grant_overlay))
                        }

                        Button(
                            onClick = { openInputMethodSettings() },
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text(stringResource(R.string.btn_change_input))
                        }



                        // Text Input Field
                        val text = remember { mutableStateOf("") }
                        androidx.compose.material3.TextField(
                            value = text.value,
                            onValueChange = { text.value = it },
                            placeholder = { Text(stringResource(R.string.hint_test_input)) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp)) // Add space at the bottom
                    }
                }
            }
        }
    }

    private fun requestOverlayPermission() {
        ActivityPermissionRequest.startPermissionRequest(this, Constants.PermissionTypes.OVERLAY)
    }

    private fun requestStoragePermission() {
        ActivityPermissionRequest.startPermissionRequest(this, Constants.PermissionTypes.STORAGE)
    }


    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun openInputMethodSettings() {
        val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}
