package org.dimi3.postalboy

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

public class ApiService {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 15000
        }
    }

    suspend fun fetchData(url: String, path: String): String {
        val result = httpClient.get {
            url {
                protocol = URLProtocol.HTTPS
                host = url
                path(path)
            }
        }

        return if (result.status.isSuccess()) {
            result.bodyAsText()
        } else result.status.description
    }
}
