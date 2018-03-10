package me.rozkmin.generic.network

import io.reactivex.Single
import me.rozkmin.generic.Position
import me.rozkmin.generic.Wrapper
import me.rozkmin.generic.createmessage.NewMessageBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by jaroslawmichalik on 20.12.2017
 */
interface NetworkService {

    @GET("api/messages")
    fun getAllMessages(): Single<List<Wrapper>>

//    @GET("/pubs")
//    fun getPubList(): Single<List<PubModel>>
//
//    //    @GET("/userlocations?from=-10000&to=10000")
//    @GET("/userlocations")
//    fun getLocationFeed(): Single<List<UserLocationModel>>
//
//    @POST("/userlocations")
//    fun postMyLocation(@Body myLocation: PostRequestLocation): Single<String?>
//
//    @GET("/sales")
//    fun getPromotions(): Single<List<PromotionModel>>

    @POST("api/messages")
    fun postNewMessage(@Body newMessageBody : NewMessageBody) : Single<Any>
}