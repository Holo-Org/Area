package eu.epitech.area.spike

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

sealed interface PingResult {
    data class Ok(val status: String) : PingResult
    data class Error(val message: String) : PingResult
}

private val validBaseUrl = Regex("""^https?://[^/\s]+(/.*)?$""")

class PingClient(private val http: HttpClient = HttpClient()) {
    suspend fun ping(baseUrl: String): PingResult {
        val base = baseUrl.trim()
        if (!validBaseUrl.matches(base)) return PingResult.Error("Adresse invalide")
        return try {
            val res = withTimeout(5_000) { http.get(base.trimEnd('/') + "/ping") }
            if (res.status.value != 200) return PingResult.Error("HTTP ${res.status.value}")
            val status = Json.parseToJsonElement(res.bodyAsText()).jsonObject["status"]?.jsonPrimitive?.contentOrNull
            if (status != null) PingResult.Ok(status) else PingResult.Error("Réponse inattendue")
        } catch (e: Exception) {
            PingResult.Error("Injoignable : ${e::class.simpleName}")
        }
    }
}
