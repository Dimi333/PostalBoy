package org.dimi3.postalboy

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeoutException

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

    fun isValidUrl(url: String): Boolean {
        val urlRegex = """^(https?|ftp)://[\w\-]+(\.[\w\-]+)+[/#?]?.*$""".toRegex()
        return urlRegex.matches(url)
    }

    suspend fun fetchData(urlParameter: String, method: String, attrs: String, bearer: String): ApiResponse {
        try {
            var result: HttpResponse?
            val httpBuilder = HttpRequestBuilder()

            if (isValidUrl(urlParameter).not()) {
                return ApiResponse.Initial
            }

            val uri = parseUrl(urlParameter) as Url

            httpBuilder.url.takeFrom(urlParameter)
            httpBuilder.host = uri.host
            httpBuilder.method = HttpMethod.parse(method)
            httpBuilder.url.path(uri.fullPath)
            httpBuilder.url.protocol = uri.protocol

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
        } catch (e: TimeoutException) {
            return ApiResponse(
                body = "Požiadavka vypršala: ${e.message}",
                status = HttpStatusCode.GatewayTimeout.value,
                statusText = "Gateway Timeout",
                headers = emptyMap()
            )
        } catch (e: Exception) {
            return ApiResponse(
                body = "Chyba pri spracovaní požiadavky: ${e.message}",
                status = HttpStatusCode.InternalServerError.value,
                statusText = "Internal Server Error",
                headers = emptyMap()
            )
        }

    }
}
