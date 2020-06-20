package com.app.nl.graphql.gqldemo

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.rx3.Rx3Apollo
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.IOException

/**
 * Important note: functions below are not the "tests" by any means.
 * First of all: they're intentionally failing. :)
 * Second: you wouldn't "test" api like that.
 *
 * It's just the fastest way to model some actual query.
 * Also, it can be helpful when you're debugging some api issues.
 */
class GQLDemo {

    //just for simplicity (not production code)
    @Suppress("SameParameterValue")
    @Throws(IOException::class)
    private fun post(url: String, content: String, contentType: String? = null): String? {
        val body: RequestBody = content.toRequestBody(contentType?.toMediaTypeOrNull())
        val request: Request = Request.Builder()
            .url(url)
            .post(body)
            .build()
        client.newCall(request).execute().use { response -> return response.body?.string() }
    }

    companion object {
        private const val url = "https://swapi-graphql.netlify.app/.netlify/functions/index"
        private val client = OkHttpClient()
        private val apolloClient = ApolloClient.builder()
            .okHttpClient(client)
            .serverUrl(url)
            .build()
    }


    /**
     * Request via good old pal - okhttp :)
     */
    @Test
    fun swapi_first_two_people_request() {
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
            "",
            post(request, "")
        )
    }

    /**
     * Same request using apollo
     */
    @Test
    fun swapi_first_two_people_apollo() {
        val firstTwoPeopleQuery = FirstTwoPeopleQuery()
        val result = Rx3Apollo.from(apolloClient.query(firstTwoPeopleQuery)).blockingFirst()
        Assertions.assertEquals("", result.data.toString())
    }

    /**
     * Example of request variables and fragments
     */
    @Test
    fun swapi_connecting_people_with_params() {
        val numPeopleToCollect = 1
        val peopleConnectionQuery = ConnectedPeopleQuery(numPeopleToCollect)
        val result = Rx3Apollo.from(apolloClient.query(peopleConnectionQuery)).blockingFirst()
        Assertions.assertEquals("", result.data.toString())
    }

}