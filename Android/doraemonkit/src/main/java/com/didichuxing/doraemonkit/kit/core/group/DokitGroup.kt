package com.didichuxing.doraemonkit.kit.core.group

/**
 * ================================================
 * 作    者：jint（金台）
 * 版    本：1.0
 * 创建日期：2020/4/29-10:48
 * 描    述：
 * 修订历史：
 * ================================================
 */
internal interface DokitGroup {
    fun groupName(): String

    fun isSystemGroup(): Boolean {
        return false
    }
}