package com.yalantis.ucrop.callback;

import android.net.Uri;
import android.support.annotation.NonNull;


public interface BitmapCropCallback {

    void onBitmapCropped(Uri resultUri, int offsetX, int offsetY, int imageWidth, int imageHeight);

    void onCropFailure(@NonNull Throwable t);

}