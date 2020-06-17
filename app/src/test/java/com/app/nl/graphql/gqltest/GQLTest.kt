package com.app.nl.graphql.gqltest

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.jupiter.api.Test
import java.io.IOException


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Throws(IOException::class)
    fun post(url: String, content: String): String? {
        val client = OkHttpClient()
        val body: RequestBody = content.toRequestBody()
        val request: Request = Request.Builder()
            .url(url)
            .post(body)
            .build()
        client.newCall(request).execute().use { response -> return response.body?.string() }
    }

    @Test
    fun addition_isCorrect() {

    }
}