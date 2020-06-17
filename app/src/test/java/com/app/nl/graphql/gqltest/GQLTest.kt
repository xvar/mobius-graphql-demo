package com.app.nl.graphql.gqltest

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.IOException


class GQLTest {

    //just for simplicity (not production code)
    @Throws(IOException::class)
    private fun post(url: String, content: String): String? {
        val client = OkHttpClient()
        val body: RequestBody = content.toRequestBody()
        val request: Request = Request.Builder()
            .url(url)
            .post(body)
            .build()
        client.newCall(request).execute().use { response -> return response.body?.string() }
    }

    @Test
    fun swapi_request() {
        val url = "https://graphql.org/swapi-graphql/"
        val query = "query {\n" +
                "  allPeople(first: 2) {\n" +
                "    people {\n" +
                "      name,\n" +
                "      birthYear,\n" +
                "      eyeColor,\n" +
                "      homeworld {\n" +
                "        name\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}"
        val request = "$url?query=$query"
        Assertions.assertEquals("", post(request, "application/json"))
    }
}