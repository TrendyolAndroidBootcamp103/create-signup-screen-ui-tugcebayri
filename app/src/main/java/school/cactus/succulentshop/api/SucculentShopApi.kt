package school.cactus.succulentshop.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import school.cactus.succulentshop.api.login.LoginRequest
import school.cactus.succulentshop.api.login.LoginResponse
import school.cactus.succulentshop.api.product.Product
import school.cactus.succulentshop.api.register.RegisterRequest
import school.cactus.succulentshop.api.register.RegisterResponse

interface SucculentShopApi {
    @POST("/auth/local")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("/auth/local/register")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @GET("/products")
    fun listAllProducts(): Call<List<Product>>
}