package eu.epitech.area.spike

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/** Shared screen (Android and iOS). [defaultServerUrl] comes from the platform build config. */
@Composable
fun App(defaultServerUrl: String = "", client: PingClient = remember { PingClient() }) {
    MaterialTheme {
        Surface(Modifier.fillMaxSize()) {
            var url by remember { mutableStateOf(defaultServerUrl) }
            var result by remember { mutableStateOf("—") }
            var loading by remember { mutableStateOf(false) }
            val scope = rememberCoroutineScope()

            Column(Modifier.fillMaxSize().safeContentPadding().padding(16.dp)) {
                Text(
                    "AREA — ping",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.semantics { heading() },
                )
                Spacer(Modifier.height(24.dp))
                OutlinedTextField(
                    value = url,
                    onValueChange = { url = it },
                    label = { Text("Adresse du serveur") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                    modifier = Modifier.fillMaxWidth().testTag("server-url"),
                )
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = {
                        scope.launch {
                            loading = true
                            result = when (val r = client.ping(url)) {
                                is PingResult.Ok -> "Statut : ${r.status}"
                                is PingResult.Error -> "Erreur : ${r.message}"
                            }
                            loading = false
                        }
                    },
                    enabled = !loading,
                    modifier = Modifier.fillMaxWidth().testTag("ping-button"),
                ) { Text(if (loading) "Ping…" else "Ping") }
                Spacer(Modifier.height(16.dp))
                Text(
                    result,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.testTag("ping-result").semantics { liveRegion = LiveRegionMode.Polite },
                )
            }
        }
    }
}
