package com.didichuxing.doraemonkit.kit.filemanager.action.sql

import com.didichuxing.doraemonkit.kit.filemanager.sqlite.DBManager
import com.didichuxing.doraemonkit.kit.filemanager.sqlite.bean.RowFiledInfo

/**
 * ================================================
 * 作    者：jint（金台）
 * 版    本：1.0
 * 创建日期：2020/6/28-11:36
 * 描    述：
 * 修订历史：
 * ================================================
 */
object DatabaseAction {
    fun allTablesRes(filePath: String, fileName: String): MutableMap<String, Any> {
        val response = mutableMapOf<String, Any>()
        val tables = DBManager.getAllTableName(filePath, fileName)
        response["code"] = 200
        response["data"] = tables
        return response
    }

    fun tableDatasRes(filePath: String, fileName: String, tableName: String): MutableMap<String, Any> {
        val response = mutableMapOf<String, Any>()
        val tables = DBManager.getTableData(filePath, fileName, tableName)
        response["code"] = 200
        response["data"] = tables
        return response
    }

    fun insertRowRes(filePath: String, fileName: String, tableName: String, rowDatas: List<RowFiledInfo>): Map<String, Any> {
        val response = mutableMapOf<String, Any>()
        val insertRow = DBManager.insertRow(filePath, fileName, tableName, rowDatas)
        if (insertRow == -1L) {
            response["code"] = 0
            response["success"] = false
        } else {
            response["code"] = 200
            response["success"] = true
        }
        return response
    }

    fun updateRowRes(filePath: String, fileName: String, tableName: String, rowDatas: List<RowFiledInfo>): Map<String, Any> {
        val response = mutableMapOf<String, Any>()
        val updateRow = DBManager.updateRow(filePath, fileName, tableName, rowDatas)
        if (updateRow == -1) {
            response["code"] = 0
            response["success"] = false
        } else {
            response["code"] = 200
            response["success"] = true
        }
        return response

    }

    fun deleteRowRes(filePath: String, fileName: String, tableName: String, rowDatas: List<RowFiledInfo>): Map<String, Any> {
        val response = mutableMapOf<String, Any>()
        val deleteRow = DBManager.deleteRow(filePath, fileName, tableName, rowDatas)
        if (deleteRow == -1) {
            response["code"] = 0
            response["success"] = false
        } else {
            response["code"] = 200
            response["success"] = true
        }
        return response
    }
}