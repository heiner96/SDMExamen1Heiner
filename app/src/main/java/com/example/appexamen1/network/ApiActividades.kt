package com.example.appexamen1.network

import com.example.appexamen1.model.Actividad
import io.reactivex.Observable
import io.reactivex.Completable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiActividades
{
    @GET("Actividad") fun getActividades():Observable<List<Actividad>>
    @GET("Actividad/{id}") fun getActividad():Observable<Actividad>
    @POST("Actividad") fun addActividad(@Body actividad:Actividad): Completable
    @DELETE("Actividad/{id}") fun deleteActividad(@Path("id")id:Int): Completable
    @PUT("Actividad/{id}") fun updateActividad(@Path("id")id:Int, @Body actividad: Actividad): Completable

    companion object{
        fun create():ApiActividades{
            val interceptor= HttpLoggingInterceptor()
            interceptor.setLevel((HttpLoggingInterceptor.Level.BODY))
            var client= OkHttpClient.Builder().addInterceptor(interceptor)
                .addInterceptor(interceptor).build()
            var retrofit= Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://10.0.2.2:5048/api/")//esta url para local
                .client(client)
                .build()
            return retrofit.create(ApiActividades::class.java)
        }
    }
}