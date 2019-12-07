package com.fy.fayou.pufa

import android.content.Context
import android.widget.Toast
import com.fy.fayou.common.ApiUrl
import com.fy.fayou.common.UploadService
import com.vondear.rxui.view.dialog.RxDialogShapeLoading
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import org.json.JSONObject

class KtPublishForumHelper {

    fun handlerArrayData(context: Context, boardId: String, data: List<Any>, listener: OnForumPublishListener) {
        val dialog = RxDialogShapeLoading(context)
        dialog.show()

        UploadService.getInstance().uploadPublishImages(data, object : UploadService.OnPublishUploadListener {
            override fun onFailure(error: String?) {
                Toast.makeText(context, "上传失败", Toast.LENGTH_SHORT).show();
                dialog.dismiss()
            }

            override fun onSuccess(list: MutableList<Any>) {
                handlerData(context, list, boardId, dialog, listener)
            }
        })

    }

    private fun handlerData(context: Context, data: MutableList<Any>, boardId: String, dialog: RxDialogShapeLoading, listener: OnForumPublishListener) {

        var title = ""

        var sb: StringBuilder = StringBuilder()

        var textSB: StringBuilder = StringBuilder()

        var description = ""

        var thumb = ""

        var isExistThumb = false

        for (obj in data) {
            if (obj is TitleEntity) {
                title = obj.name
            } else if (obj is TextEntity) {
                sb.append(obj.content + "\n ")
                textSB.append(obj.content)
            } else if (obj is PicEntity) {
                if (!isExistThumb) {
                    thumb = obj.httpPath
                }
                isExistThumb = true
                sb.append("\n![](" + obj.httpPath + ")" + "\n ")
            }
        }

        if (textSB.toString().length > 50) {
            description = textSB.substring(0, 50);
        } else {
            description = textSB.toString()
        }

        requestPostForum(context, boardId, title, sb.toString(), description, thumb, dialog, listener)

    }

    private fun requestPostForum(context: Context, boardId: String, title: String, content: String, description: String, cover: String,
                                 dialog: RxDialogShapeLoading, listener: OnForumPublishListener) {

        var jsonObject = JSONObject()
        jsonObject.put("boardId", boardId)
        jsonObject.put("title", title)
        jsonObject.put("content", content)
        jsonObject.put("description", description)
        jsonObject.put("cover", cover)

        EasyHttp.post(ApiUrl.POST_FORUM)
                .upJson(jsonObject.toString())
                .execute(object : SimpleCallBack<String>() {
                    override fun onError(e: ApiException?) {
                        Toast.makeText(context, "上传失败", Toast.LENGTH_SHORT).show();
                        dialog.dismiss()
                    }

                    override fun onSuccess(t: String?) {
                        listener.onSuccess()
                    }
                })
    }

}