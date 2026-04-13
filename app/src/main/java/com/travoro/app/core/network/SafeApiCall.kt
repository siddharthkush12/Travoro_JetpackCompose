package com.travoro.app.core.network

import org.json.JSONObject
import retrofit2.Response

suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): ApiResult<T> {
    return try {
        val response = apiCall()

        if (response.isSuccessful) {
            response.body()?.let {
                ApiResult.Success(it)
            } ?: ApiResult.Error(
                code = response.code(),
                message = "Empty response",
            )
        } else {
            val errorJson =
                response.errorBody()
                    ?.charStream()
                    ?.readText()

            val message =
                try {
                    JSONObject(errorJson ?: "")
                        .optString("message", "Something went wrong")
                } catch (e: Exception) {
                    "Something went wrong"
                }

            ApiResult.Error(
                code = response.code(),
                message = message,
            )
        }
    } catch (e: Exception) {
        ApiResult.Exception(
            message = e.localizedMessage ?: "Network error",
        )
    }
}
