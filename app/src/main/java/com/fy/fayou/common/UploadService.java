package com.fy.fayou.common;

import com.fy.fayou.utils.qiniu.Auth;
import com.luck.picture.lib.entity.LocalMedia;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;
import com.vondear.rxtool.RxTimeTool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class UploadService {

    private volatile static UploadService singleton = null;
    private static UploadManager uploadManager;
    private static String uploadToken;

    public UploadService() {
    }

    public static UploadService getInstance() {
        if (singleton == null) {
            synchronized (UploadService.class) {
                if (singleton == null) {
                    singleton = new UploadService();
                    uploadManager = new UploadManager(new Configuration.Builder().zone(FixedZone.zone2).build());
                    uploadToken = Auth.create(Constant.QiNiu.AccessKey, Constant.QiNiu.SecretKey).uploadToken(Constant.QiNiu.BUCKET_NAME);
                }
            }
        }
        return singleton;
    }

    public void uploadSingleFile(String path, OnUploadListener listener) {
        if (singleton != null) {
            File uploadFile = new File(path);
            uploadManager.put(uploadFile, getPictureName(), uploadToken, (key1, info, response) -> {
                if (info.isOK()) {
                    if (listener != null) listener.onSuccess(Constant.QiNiu.DOMAIN + key1);
                } else {
                    if (listener != null) listener.onFailure(info.error);
                }
            }, null);
        }
    }

    public void syncUploadMultiFile(final List<String> arrayPath, final OnUploadListener listener) {
        if (null == arrayPath || arrayPath.isEmpty()) {
            return;
        }
        Observable.just(arrayPath)
                .observeOn(Schedulers.io())
                .map(strings -> {
                    List<ResponseInfo> responseList = new ArrayList<>();
                    for (String path : strings) {
                        File uploadFile = new File(path);
                        responseList.add(uploadManager.syncPut(uploadFile, getPictureName(), uploadToken, null));
                    }
                    return responseList;
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    StringBuilder sb = new StringBuilder();
                    for (ResponseInfo response : list) {
                        if (response.isOK()) {
                            if (response.response.has("key")) {
                                sb.append(Constant.QiNiu.DOMAIN + response.response.optString("key") + ",");
                            }
                        } else {
                            listener.onFailure("图片上传失败");
                            return;
                        }
                    }
                    listener.onSuccess(sb.substring(0, sb.length() - 1));
                });

    }

    public void syncUploadMultiFileByMedia(final List<LocalMedia> arrayPath, final OnUploadListener listener) {
        if (null == arrayPath || arrayPath.isEmpty()) {
            return;
        }
        List<String> picList = new ArrayList<>();
        for (LocalMedia media : arrayPath) {
            picList.add(media.getPath());
        }
        syncUploadMultiFile(picList, listener);
    }

    public void uploadMultiFile(final List<String> arrayPath, final OnUploadListener listener) {
        if (null == arrayPath || arrayPath.isEmpty()) {
            return;
        }
        Observable.just(arrayPath)
                .observeOn(Schedulers.io())
                .flatMap((Function<List<String>, ObservableSource<List<String>>>) strings -> {
                    List<String> picArray = new ArrayList<>();
                    for (String path : strings) {
                        File uploadFile = new File(path);
                        uploadManager.put(uploadFile, getPictureName(), uploadToken, (key, info, response) -> {
                            if (info.isOK()) {
                                picArray.add(Constant.QiNiu.DOMAIN + key);
                            }
                        }, null);
                    }
                    return Observable.just(picArray);
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(picArray -> {
                    if (arrayPath.size() == picArray.size()) {
                        StringBuilder sb = new StringBuilder();
                        for (String pic : picArray) {
                            sb.append(pic + ",");
                        }
                        listener.onSuccess(sb.substring(0, sb.length() - 1));
                    } else {
                        listener.onFailure("上传图片失败");
                    }
                });
    }


    //图片名称
    public static final String getPictureName() {
        return RxTimeTool.getCurrentDateTime(DATE_FORMAT_LINK) + "_" + new Random().nextInt(1000);
    }

    //Date格式
    public static final String DATE_FORMAT_LINK = "yyyyMMddHHmmssSSS";


    public interface OnUploadListener {
        void onSuccess(String key);

        void onFailure(String error);
    }

}
