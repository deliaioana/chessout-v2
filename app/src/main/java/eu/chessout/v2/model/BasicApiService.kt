package eu.chessout.v2.model

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import eu.chessout.shared.dao.BasicApiResponse
import eu.chessout.shared.model.MyPayLoad
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BasicApiService {

    private val baseUrl = "my-base-url"

    private val api = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(BasicApi::class.java)

    fun gameResultUpdated(myPayLoad: MyPayLoad): Single<BasicApiResponse> {
        return api.gameResultUpdated(myPayLoad)
    }
}