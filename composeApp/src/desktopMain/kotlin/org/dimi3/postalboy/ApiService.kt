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

    suspend fun fetchData(url: String): String {
        val uri = parseUrl(url)

        val result = httpClient.get {
            url {
                protocol = (uri?.protocol ?: "https") as URLProtocol
                host = uri?.host.orEmpty()
                path(uri?.fullPath.orEmpty())
            }
        }

        return if (result.status.isSuccess()) {
            result.bodyAsText()
        } else result.status.description
    }
}
