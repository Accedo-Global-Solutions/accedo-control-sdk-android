package tv.accedo.control

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*

internal val client = HttpClient(OkHttp) {
    engine {
        // this: OkHttpConfig
        config {
            // this: OkHttpClient.Builder
            followRedirects(true)
            // ...
        }
//        addInterceptor(interceptor)
//        addNetworkInterceptor(interceptor)
//
//        preconfigured = okHttpClientInstance
    }
}