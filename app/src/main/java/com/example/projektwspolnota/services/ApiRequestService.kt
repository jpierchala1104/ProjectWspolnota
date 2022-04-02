package com.example.projektwspolnota.services

import android.util.Log
import com.example.projektwspolnota.resolution.mvi.ResolutionItem
import com.example.projektwspolnota.resolution.mvi.Message
import com.example.projektwspolnota.results.mvi.ResultsItem
import com.example.projektwspolnota.vote.mvi.VoteItem
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.lang.reflect.Type
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class ApiRequestService {
    private val client = OkHttpClient()
    private val gson = GsonBuilder().create()

    fun loadResolutions(user: User): Observable<Collection<ResolutionItem>> {
        return Observable
            .just(Unit)
            .subscribeOn(Schedulers.io())
            .map {

                val url = "http://10.0.2.2:51897/api/WspolnotaApi/getPendingRes/${user.userId}"

                val request = Request.Builder().url(url).build()

                var body: String? = ""

                val countDownLatch = CountDownLatch(1)
                client.newCall(request).enqueue(object : Callback {
                    override fun onResponse(call: Call, response: Response) {
                        body = response.body?.string()
                        countDownLatch.countDown()
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        Log.e("loadResoluton", e.toString())
                        countDownLatch.countDown()
                    }
                })

                val type: Type = TypeToken.getParameterized(Collection::class.java, ResolutionItem::class.java).type
                countDownLatch.await()
                val result = gson.fromJson(body, type) as Collection<ResolutionItem>
                return@map result

//                return@map Repository
//                    .getAllResolutions()
//                    .filter { r ->
//                        //it.finishDate < LocalDateTime.now().toString() &&
//                        Repository.getPointsInResolution(r.resolutionId).filter { p ->
//                            Repository.getUserPointsByPointId(p.pointId).filter { up ->
//                                up.userId == user.userId
//                            }.isEmpty()
//                        }.isNotEmpty()
//                }
            }.delay(500, TimeUnit.MILLISECONDS)
    }

    fun loadMessage(): Observable<Message> {
        return Observable
            .just(Unit)
            .subscribeOn(Schedulers.io())
            .map {
                val url = "http://10.0.2.2:51897/api/WspolnotaApi/getMessage"

                val request = Request.Builder().url(url).build()

                var body: String? = ""

                val countDownLatch = CountDownLatch(1)
                client.newCall(request).enqueue(object : Callback {
                    override fun onResponse(call: Call, response: Response) {
                        body = response.body?.string()
                        countDownLatch.countDown()
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        Log.e("loadMessage", e.toString())
                        countDownLatch.countDown()
                    }
                })
                countDownLatch.await()
                return@map gson.fromJson(body, Message::class.java)
            }.delay(500, TimeUnit.MILLISECONDS)
    }

    fun loadResults(skip: Int, take: Int): Observable<Collection<ResolutionItem>> {
        return Observable
            .just(Unit)
            .subscribeOn(Schedulers.io())
            .map {
                val url = "http://10.0.2.2:51897/api/WspolnotaApi/getFinished"

                val request = Request.Builder().url(url).build()

                var body: String? = ""

                val countDownLatch = CountDownLatch(1)
                client.newCall(request).enqueue(object : Callback {
                    override fun onResponse(call: Call, response: Response) {
                        body = response.body?.string()
                        countDownLatch.countDown()
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        Log.e("loadResults", e.toString())
                        countDownLatch.countDown()
                    }
                })
//                val type: Type = TypeToken<Collection<ResolutionItem>>(){}.getType()
                val type: Type = TypeToken.getParameterized(Collection::class.java, ResolutionItem::class.java).type
                countDownLatch.await()
                val result = gson.fromJson(body, type) as Collection<ResolutionItem>
                return@map result.drop(skip)
                    .take(take) as Collection<ResolutionItem>

//                return@map Repository.getAllResolutions().filter { r ->
//                    r.resolutionId < 3
//                }
//                    .drop(skip)
//                    .take(take) as Collection<ResolutionItem>
            }.delay(500, TimeUnit.MILLISECONDS)
    }

    fun loadPoints(resolutionId: Int): Observable<VoteItem> {
        return Observable
            .just(Unit)
            .subscribeOn(Schedulers.io())
            .map {
                val url = "http://10.0.2.2:51897/api/WspolnotaApi/getPoints/$resolutionId"

                val request = Request.Builder().url(url).build()

                var body: String? = ""

                val countDownLatch = CountDownLatch(1)
                client.newCall(request).enqueue(object : Callback {
                    override fun onResponse(call: Call, response: Response) {
                        body = response.body?.string()
                        countDownLatch.countDown()
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        Log.e("loadPoints", e.toString())
                        countDownLatch.countDown()
                    }
                })

                countDownLatch.await()
                val results = gson.fromJson(body, VoteItem::class.java)
                return@map results

//                val resolution = Repository.getResolutionById(resolutionId)
//                val pointsResults = Repository.getPointsInResolution(resolutionId).map {
//                    VotePointItem(it.pointId, it.number, it.text, null)
//                }
//                return@map VoteItem(resolutionId, resolution.title, resolution.date, pointsResults)
            }.delay(500, TimeUnit.MILLISECONDS)
    }

    fun sendMessage(text: String): Observable<Unit> {
        return Observable
            .just(Unit)
            .subscribeOn(Schedulers.io())
            .map {
                val url = "http://10.0.2.2:51897/api/WspolnotaApi/postMessage"

                val bodyString = "{\"text\": \"$text\"}"

                val body : RequestBody = bodyString.toRequestBody("application/json; charset=utf-8".toMediaType())
                val request = Request.Builder().url(url).post(body).build()
                try {
                    val response = client.newCall(request).execute()
                    if(!response.isSuccessful) throw IOException("Unexpected code $response")

                    Log.d("POST", response.body?.toString())
                }catch (e:IOException)
                {
                    Log.e("Post", e.toString())
                }
                return@map
            }
    }

    fun sendResolution(user: User, voteItem: VoteItem): Observable<Unit> {
        return Observable
            .just(Unit)
            .subscribeOn(Schedulers.io())
            .map {
                val url = "http://10.0.2.2:51897/api/WspolnotaApi/postVote/${user.userId}&${voteItem.resolutionId}"

                var bodyString: String = "["
//                [
//                    {
//                        "pointId": 8,
//                        "vote": true
//                    },
//                    {
//                        "pointId": 9,
//                        "vote": false
//                    },
//                    {
//                        "pointId": 10,
//                        "vote": null
//                    }
//                ]

                voteItem.points.forEach {
                    bodyString += "{\"pointId\": ${it.pointId}, \"vote\": ${it.pointVote}},"
                }
                bodyString = bodyString.dropLast(1)
                bodyString += "]"

                val body : RequestBody = bodyString.toRequestBody("application/json; charset=utf-8".toMediaType())
                val request = Request.Builder().url(url).post(body).build()
                try {
                    val response = client.newCall(request).execute()
                    if(!response.isSuccessful) throw IOException("Unexpected code $response")

                    Log.d("POST", response.body?.toString())
                }catch (e:IOException)
                {
                    Log.e("Post", e.toString())
                }
                return@map
            }
        }

    fun loadPointsResults(resolutionId: Int): Observable<ResultsItem> {
        return Observable
            .just(Unit)
            .subscribeOn(Schedulers.io())
            .map {
                val url = "http://10.0.2.2:51897/api/WspolnotaApi/getResults/$resolutionId"

                val request = Request.Builder().url(url).build()

                var body: String? = ""

                val countDownLatch = CountDownLatch(1)
                client.newCall(request).enqueue(object : Callback {
                    override fun onResponse(call: Call, response: Response) {
                        body = response.body?.string()
                        countDownLatch.countDown()
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        Log.e("loadPointsResults", e.toString())
                        countDownLatch.countDown()
                    }
                })

                countDownLatch.await()
                val result = gson.fromJson(body, ResultsItem::class.java)
                return@map result

//                val resolution = Repository.getResolutionById(resolutionId)
//                val pointsResults = Repository.getPointsInResolution(resolutionId).map { p ->
//                    val votes = Repository.getUserPointsByPointId(p.pointId).count()
//                    val yesVotes = Repository.getUserPointsByPointId(p.pointId).filter { up ->
//                        up.vote == true
//                    }.count()
//                    val noVotes = Repository.getUserPointsByPointId(p.pointId).filter { up ->
//                        up.vote == false
//                    }.count()
//                    val abstained = Repository.getUserPointsByPointId(p.pointId).filter { up ->
//                        up.vote == null
//                    }.count()
//                    ResultsPointItem(
//                        p.number,
//                        p.text,
//                        yesVotes * 100 / votes,
//                        noVotes * 100 / votes,
//                        abstained * 100 / votes
//                    )
//                }
//                return@map ResultsItem(resolutionId, resolution.title, resolution.date, pointsResults)
            }.delay(500, TimeUnit.MILLISECONDS)
    }

    fun logIn(login: String, password: String): Observable<User?> {
        return Observable
            .just(Unit)
            .subscribeOn(Schedulers.io())
            .map {
                val loginTemp = if(login == "")"0" else login

                val passwordTemp = if(password == "")"0" else password

                val url = "http://10.0.2.2:51897/api/WspolnotaApi/getLogin/$loginTemp&$passwordTemp"
                val request = Request.Builder().url(url).build()

                var body: String? = ""

                SessionService.setCurrentUser(null)
                val countDownLatch = CountDownLatch(1)
                client.newCall(request).enqueue(object : Callback {
                    override fun onResponse(call: Call, response: Response) {
                        body = response.body?.string()
                        countDownLatch.countDown()
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        Log.e("login", e.toString())
                        countDownLatch.countDown()
                    }
                })

                countDownLatch.await()
                val user: User = gson.fromJson(body, User::class.java)
                SessionService.setCurrentUser(user)
                return@map user
            }.delay(500, TimeUnit.MILLISECONDS)
    }
}