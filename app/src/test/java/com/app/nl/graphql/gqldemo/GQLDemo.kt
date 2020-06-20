package com.app.nl.graphql.gqldemo

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.IOException


class GQLDemo {

    //just for simplicity (not production code)
    @Suppress("SameParameterValue")
    @Throws(IOException::class)
    private fun post(url: String, content: String, contentType: String? = null): String? {
        val client = OkHttpClient()
        val body: RequestBody = content.toRequestBody(contentType?.toMediaTypeOrNull())
        val request: Request = Request.Builder()
            .url(url)
            .post(body)
            .build()
        client.newCall(request).execute().use { response -> return response.body?.string() }
    }

    companion object {
        private const val url = "https://swapi-graphql.netlify.app/.netlify/functions/index"
        private const val FIRST_TWO_PEOPLE : String =
            "{" +
                "\"data\":{" +
                    "\"allPeople\":{" +
                        "\"people\":[" +
                            "{" +
                                "\"name\":\"Luke Skywalker\"," +
                                "\"birthYear\":\"19BBY\"," +
                                "\"eyeColor\":\"blue\"," +
                                "\"homeworld\":" +
                                    "{" +
                                        "\"name\":\"Tatooine\"" +
                                    "}" +
                            "}," +
                            "{" +
                                "\"name\":\"C-3PO\"," +
                                "\"birthYear\":\"112BBY\"," +
                                "\"eyeColor\":\"yellow\"," +
                                "\"homeworld\":" +
                                    "{" +
                                        "\"name\":\"Tatooine\"" +
                                    "}" +
                            "}" +
                        "]" +
                    "}" +
                "}" +
            "}"

    }


    @Test
    fun swapi_request() {
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
        Assertions.assertEquals(
            FIRST_TWO_PEOPLE,
            post(request, "")
        )
    }

}