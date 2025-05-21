package org.dimi3.postalboy

data class ApiResponse(
    val body: String,
    val status: Int,
    val statusText: String,
    val headers: Map<String, List<String>>
) {
    companion object {
        val Initial = ApiResponse(
            body = "Čakám...",
            status = 0,
            statusText = "",
            headers = emptyMap()
        )
    }
}
