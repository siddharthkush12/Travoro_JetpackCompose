package com.example.travelapp.data.remote.api

import com.example.travelapp.data.remote.dto.others.baseResponse.BaseResponse
import com.example.travelapp.data.remote.dto.suggestion.SuggestionResponse
import com.example.travelapp.data.remote.dto.auth.LoginRequest
import com.example.travelapp.data.remote.dto.auth.LoginResponse
import com.example.travelapp.data.remote.dto.auth.ResetPasswordRequest
import com.example.travelapp.data.remote.dto.auth.ResetPasswordResponse
import com.example.travelapp.data.remote.dto.auth.SignupRequest
import com.example.travelapp.data.remote.dto.auth.SignupResponse
import com.example.travelapp.data.remote.dto.home.profile.EditProfileResponse
import com.example.travelapp.data.remote.dto.home.profile.FetchProfileResponse
import com.example.travelapp.data.remote.dto.message.ChatGroupResponse
import com.example.travelapp.data.remote.dto.message.MessageResponse
import com.example.travelapp.data.remote.dto.travelAi.AcceptTripRequest
import com.example.travelapp.data.remote.dto.travelAi.AcceptTripResponse
import com.example.travelapp.data.remote.dto.travelAi.AddMemberRequest
import com.example.travelapp.data.remote.dto.travelAi.AddMemberResponse
import com.example.travelapp.data.remote.dto.travelAi.AiTripRequest
import com.example.travelapp.data.remote.dto.travelAi.AiTripResponse
import com.example.travelapp.data.remote.dto.travelAi.DeleteTripResponse
import com.example.travelapp.data.remote.dto.trips.CreateTripRequest
import com.example.travelapp.data.remote.dto.trips.CreateTripResponse
import com.example.travelapp.data.remote.dto.trips.SearchProfileResponse
import com.example.travelapp.data.remote.dto.weather.WeatherResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface TravelApiService {
    //--------------------------------AUTH API---------------------------

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>


    @POST("api/auth/signup")
    suspend fun signup(@Body request: SignupRequest): Response<SignupResponse>


    @POST("api/auth/forget-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<ResetPasswordResponse>


    //------------------------------Profile Api------------------------

    @GET("api/profile/fetch")
    suspend fun fetchProfile():Response<FetchProfileResponse>

    @Multipart
    @PUT("api/profile/edit")
    suspend fun editProfile(
        @Part profilePic: MultipartBody.Part?,
        @Part("fullname") fullname: RequestBody?,
        @Part("dob") dob: RequestBody?,
        @Part("gender") gender: RequestBody?,
        @Part("phone") phone: RequestBody?,
        @Part("city") city: RequestBody?,
        @Part("state") state: RequestBody?,
        @Part("country") country: RequestBody?
    ): Response<EditProfileResponse>


    @GET("api/profile/search-phone")
    suspend fun searchProfileByPhone(
        @Query("phone") phone: String
    ): Response<SearchProfileResponse>


    //------------------------------Message Api------------------------

    @GET("api/message/messagingGroup")
    suspend fun getChatGroup(): Response<ChatGroupResponse>

    @GET("api/message/getMessages/{groupId}")
    suspend fun getMessages(
        @Path("groupId") groupId: String
    ): Response<MessageResponse>


    //------------------------------Notification Api------------------------
    @POST("api/notification/save-token")
    suspend fun saveToken(
        @Body body: Map<String, String>
    ): Response<BaseResponse>


    //------------------------------Weather Api------------------------

    @GET("api/weather/weather")
    suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
    ): Response<WeatherResponse>


    //------------------------------Trips Api------------------------
    @POST("api/trips/create")
    suspend fun createTrip(
        @Body request: CreateTripRequest
    ): Response<CreateTripResponse>




    @POST("api/travelAi/generate-plan")
    suspend fun generateAiTrip(
        @Body request: AiTripRequest
    ): Response<AiTripResponse>





    @POST("api/travelAi/accept-trip")
    suspend fun acceptTrip(
        @Body request: AcceptTripRequest
    ): Response<AcceptTripResponse>


    @POST("api/travelAi/add-members/{tripId}")
    suspend fun addMembers(
        @Path("tripId") tripId: String,
        @Body request: AddMemberRequest
    ): Response<AddMemberResponse>


    @DELETE("api/travelAi/delete-trip/{tripId}")
    suspend fun deleteTrip(
        @Path("tripId") tripId: String
    ): Response<DeleteTripResponse>


    @GET("api/travelAi/suggestions/all")
    suspend fun getAllSuggestions(): Response<SuggestionResponse>



}