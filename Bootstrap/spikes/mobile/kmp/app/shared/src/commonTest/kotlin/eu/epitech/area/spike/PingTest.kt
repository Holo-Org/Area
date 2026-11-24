package eu.epitech.area.spike

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.v2.runComposeUiTest
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlin.test.Test
import kotlin.test.assertEquals

class PingTest {
    private fun client(status: HttpStatusCode = HttpStatusCode.OK, body: String = """{"status":"ok"}""", onUrl: (String) -> Unit = {}) =
        PingClient(HttpClient(MockEngine { req -> onUrl(req.url.toString()); respond(body, status) }))

    // runTest skips virtual time, which would fire PingClient's withTimeout before the mock answers:
    // run the client on a real dispatcher instead.
    @Test
    fun pingParsesStatusOkOnGetPing() = runTest { withContext(Dispatchers.Default) {
        var called = ""
        val r = client(onUrl = { called = it }).ping("http://10.0.2.2:8080")
        assertEquals("http://10.0.2.2:8080/ping", called)
        assertEquals(PingResult.Ok("ok"), r)
    } }

    @Test
    fun pingReturnsErrorOnHttp500AndInvalidAddress() = runTest { withContext(Dispatchers.Default) {
        val c = client(status = HttpStatusCode.InternalServerError, body = "")
        assertEquals(PingResult.Error("HTTP 500"), c.ping("http://x:1"))
        assertEquals(PingResult.Error("Adresse invalide"), c.ping("pas une url"))
    } }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun screenTypesAddressTapsPingAndShowsStatus() = runComposeUiTest {
        setContent { App(client = client()) }
        // Accessibility: the text field is reachable by its visible label, as TalkBack announces it.
        onNode(hasText("Adresse du serveur") and hasSetTextAction()).performTextReplacement("http://10.0.2.2:8080")
        onNodeWithText("Ping").performClick()
        waitUntil(timeoutMillis = 5_000) { onAllNodes(hasText("Statut : ok")).fetchSemanticsNodes().isNotEmpty() }
    }
}
