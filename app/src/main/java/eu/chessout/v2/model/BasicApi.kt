package eu.chessout.v2.model

import eu.chessout.shared.dao.BasicApiResponse
import eu.chessout.shared.model.MyPayLoad
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface BasicApi {

    @POST("api/gameResultUpdated")
    fun gameResultUpdated(@Body myPayLoad: MyPayLoad): Single<BasicApiResponse>
}