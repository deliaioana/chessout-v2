package eu.chessout.v2.model

import eu.chessout.shared.dao.BasicApiResponse
import eu.chessout.shared.model.MyPayLoad
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.PUT

interface BasicApi {

    @PUT("api/gameResultUpdated")
    fun gameResultUpdated(@Body myPayLoad: MyPayLoad): Single<BasicApiResponse>
}