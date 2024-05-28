import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


fun main() {
    val serverUrl = "http://localhost:8080"

    if (isServerRunning(serverUrl)) {
        val username = "user"
        val password = "pass"
        if (areCredentialsValid(serverUrl, username, password)) {
            val text = getTextFromServer(serverUrl)
            println("Server responded with text: $text")
        } else {
            println("Credentials are wrong")
        }
    } else {
        println("Server is not running")
    }
}

fun isServerRunning(serverUrl: String): Boolean {
    return try {
        val url = URL("$serverUrl/status")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.responseCode == 200
    } catch (e: Exception) {
        false
    }
}

fun areCredentialsValid(serverUrl: String, username: String, password: String): Boolean {
    return try {
        val url = URL("$serverUrl/login")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.doOutput = true
        connection.setRequestProperty("Content-Type", "application/json")

        val credentials = Credentials(username, password)
        val jsonCredentials = Json.encodeToString(credentials)

        val out = OutputStreamWriter(connection.outputStream)
        out.write(jsonCredentials)
        out.flush()
        out.close()

        connection.responseCode == 200
    } catch (e: Exception) {
        false
    }
}

fun getTextFromServer(serverUrl: String): String? {
    return try {
        val url = URL("$serverUrl/text")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        connection.inputStream.bufferedReader().readText()
    } catch (e: Exception) {
        null
    }
}
