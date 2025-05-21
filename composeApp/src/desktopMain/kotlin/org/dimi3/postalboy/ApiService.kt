package org.dimi3.postalboy

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class ApiService {
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

    suspend fun fetchData(url: String, method: String, attrs: String, bearer: String): ApiResponse {
        val uri = parseUrl(url)
        var result: HttpResponse?
        val httpBuilder = HttpRequestBuilder()

        httpBuilder.url.takeFrom(uri!!)
        httpBuilder.host = uri.host
        httpBuilder.method = HttpMethod.parse(method)
        httpBuilder.url.path(uri.fullPath)
        httpBuilder.url.protocol = (uri.protocol ?: "https") as URLProtocol

        if (bearer.isNotEmpty()) {
            httpBuilder.headers.append("Authorization", "Bearer $bearer")
        }

        if (attrs.isNotEmpty()) httpBuilder.headers.append("Authorization", attrs)

        when (method) {
            "GET" -> {
                result = httpClient.get(httpBuilder)
            }

            "POST" -> {
                result = httpClient.post(httpBuilder)
            }

            "PUT" -> {
                result = httpClient.put(httpBuilder)
            }

            "DELETE" -> {
                result = httpClient.delete(httpBuilder)
            }

            "PATCH" -> {
                result = httpClient.patch(httpBuilder)
            }

            else -> {
                result = httpClient.get(httpBuilder)
            }
        }

        return ApiResponse(
            body = if (result.status.isSuccess()) result.bodyAsText() else result.status.description,
            status = result.status.value,
            statusText = result.status.description,
            headers = buildMap {
                result.headers.forEach { name, values ->
                    put(name, values)
                }
            }
        )
    }
}
