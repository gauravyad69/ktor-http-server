import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


fun main(args:Array<String>) {
    val serverUrl = "http://localhost:8080"

    if (isServerRunning(serverUrl)) {

        val username = "user"
        val password = "pass"

        if (areCredentialsValid(serverUrl, username, password)) {
            val response = getTextFromServer(serverUrl, username, password)

            if (response!=null){
                println("Server responded with text: $response")
            }else{
                println("error machikne")
            }
        } else {
            println("Credentials are wrong")
            print("1. Register account, 2. Don't")
            var readline:Int = readln().toInt()  // readLine() is used to accept the String value and ".toInt()" will convert the string to  Int.
            if(readline==1){
             registerUser(serverUrl, "user1", "pass1")
            }
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

fun registerUser(serverUrl: String, username: String, password: String): Boolean {
    return try {
        val url = URL("$serverUrl/register")
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

    }catch (e: Exception){
    false
    }
}




fun getTextFromServer(serverUrl: String, username: String, password: String): String? {
    return try {
        val url = URL("$serverUrl/text")
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

        // Read the server's response
        val response = connection.inputStream.bufferedReader().use { it.readText() }

        response // Return the response

    } catch (e: Exception) {
        e.message // Return the exception message in case of an error
    }
}


