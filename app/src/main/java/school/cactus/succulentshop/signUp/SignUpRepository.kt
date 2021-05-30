package school.cactus.succulentshop.signUp

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import school.cactus.succulentshop.api.GenericErrorResponse
import school.cactus.succulentshop.api.api
import school.cactus.succulentshop.api.register.RegisterRequest
import school.cactus.succulentshop.api.register.RegisterResponse

class SignUpRepository {
    fun sendRegisterRequest(
        mail: String,
        username: String,
        password: String,
        callback: RegisterRequestCallback
    ) {
        val request = RegisterRequest(mail, username, password)

        api.register(request).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                when (response.code()) {
                    200 -> callback.onSuccess(response.body()!!.jwt)
                    in 400..499 -> callback.onClientError(response.errorBody()!!.errorMessage())
                    else -> callback.onUnexpectedError()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                callback.onFailure()
            }
        })
    }

    private fun ResponseBody.errorMessage(): String {
        val errorBody = string()
        val gson: Gson = GsonBuilder().create()
        val loginErrorResponse = gson.fromJson(errorBody, GenericErrorResponse::class.java)
        return loginErrorResponse.message[0].messages[0].message
    }

    interface RegisterRequestCallback {
        fun onSuccess(jwt: String)
        fun onClientError(errorMessage: String)
        fun onUnexpectedError()
        fun onFailure()
    }
}