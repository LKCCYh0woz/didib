package com.didichuxing.doraemonkit.okhttp_api

import okhttp3.*
import okio.BufferedSink
import okio.Sink
import java.net.URL

/**
 * ================================================
 * 作    者：jint（金台）
 * 版    本：1.0
 * 创建日期：2020/10/19-14:55
 * 描    述：
 * 修订历史：
 * ================================================
 */
object OkHttpWrap {
    fun createHttpUrl(url: String?): HttpUrl? {
        return if (isV4()) {
            OkHttpWrapV4.createHttpUrl(url)
        } else {
            OkHttpWrapV3.createHttpUrl(url)
        }

    }


    fun toUrl(httpUrl: HttpUrl?): URL? {
        return if (isV4()) {
            OkHttpWrapV4.toUrl(httpUrl)
        } else {
            OkHttpWrapV3.toUrl(httpUrl)
        }
    }

    fun toResponseBody(response: Response?): ResponseBody? {
        return if (isV4()) {
            OkHttpWrapV4.toResponseBody(response)
        } else {
            OkHttpWrapV3.toResponseBody(response)
        }
    }


    fun toHttpQuery(httpUrl: HttpUrl?): String? {
        return if (isV4()) {
            OkHttpWrapV4.toHttpQuery(httpUrl)
        } else {
            OkHttpWrapV3.toHttpQuery(httpUrl)
        }
    }


    fun toEncodedPath(httpUrl: HttpUrl?): String? {
        return if (isV4()) {
            OkHttpWrapV4.toEncodedPath(httpUrl)
        } else {
            OkHttpWrapV3.toEncodedPath(httpUrl)
        }
    }


    fun toRequestHost(httpUrl: HttpUrl?): String? {
        return if (isV4()) {
            OkHttpWrapV4.toRequestHost(httpUrl)
        } else {
            OkHttpWrapV3.toRequestHost(httpUrl)
        }
    }

    fun toRequestHost(request: Request): String? {
        return if (isV4()) {
            OkHttpWrapV4.toRequestHost(request)
        } else {
            OkHttpWrapV3.toRequestHost(request)
        }
    }

    fun toResponseHost(response: Response): String? {
        return if (isV4()) {
            OkHttpWrapV4.toResponseHost(response)
        } else {
            OkHttpWrapV3.toResponseHost(response)
        }
    }


    fun toScheme(httpUrl: HttpUrl): String {
        return if (isV4()) {
            OkHttpWrapV4.toScheme(httpUrl)
        } else {
            OkHttpWrapV3.toScheme(httpUrl)
        }
    }


    fun toResponseCode(response: Response): Int {
        return if (isV4()) {
            OkHttpWrapV4.toResponseCode(response)
        } else {
            OkHttpWrapV3.toResponseCode(response)
        }
    }


    fun toMediaType(contentType: String?): MediaType? {
        return if (isV4()) {
            OkHttpWrapV4.toMediaType(contentType)
        } else {
            OkHttpWrapV3.toMediaType(contentType)
        }
    }

    fun toRequestBody(content: String?, mediaType: MediaType?): RequestBody? {
        return if (isV4()) {
            OkHttpWrapV4.toRequestBody(content, mediaType)
        } else {
            OkHttpWrapV3.toRequestBody(content, mediaType)
        }
    }

    fun getVersion(): String? {
        return if (isV4()) {
            OkHttpWrapV4.getVersion()
        } else {
            OkHttpWrapV3.getVersion()
        }
    }


    /**
     * 判断okhttp是否是V4版本
     */
    fun isV4(): Boolean {
        try {
            Class.forName("okhttp3.OkHttp")
            return true
        } catch (e: ClassNotFoundException) {
            return false
        }
    }


    fun createByteCountBufferedSink(sink: Sink, byteCount: Long): BufferedSink {
        return if (isV4()) {
            ByteCountBufferedSinkV4(sink, byteCount)
        } else {
            ByteCountBufferedSinkV3(sink, byteCount)
        }
    }
}