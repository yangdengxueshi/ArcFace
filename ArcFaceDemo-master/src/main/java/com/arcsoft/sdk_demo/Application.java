package com.arcsoft.sdk_demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.media.ExifInterface;
import android.util.Log;

import java.util.Objects;

public class Application extends android.app.Application {
    FaceDB mFaceDB;//人脸数据库
    Uri mImageUri;//图片Uri

    @Override
    public void onCreate() {
        super.onCreate();
        mFaceDB = new FaceDB(Objects.requireNonNull(getExternalCacheDir()).getPath());
    }

    public void setCaptureImage(Uri uri) {
        mImageUri = uri;
    }

    public Uri getCaptureImage() {
        return mImageUri;
    }

    /**
     * @param filePath 文件路径
     * @return Bitmap
     */
    @Nullable
    public static Bitmap decodeImage(String filePath) {
        try {
            //旋转和缩放
            Matrix matrix = new Matrix();
            int orientation = new ExifInterface(filePath).getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                matrix.postRotate(90);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                matrix.postRotate(180);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                matrix.postRotate(270);
            }

            BitmapFactory.Options lBitmapFactoryOptions = new BitmapFactory.Options();
            lBitmapFactoryOptions.inSampleSize = 1;
            lBitmapFactoryOptions.inJustDecodeBounds = false;
//            lBitmapFactoryOptions.inMutable = true;
            Bitmap lBitmap = BitmapFactory.decodeFile(filePath, lBitmapFactoryOptions);
            Bitmap lTempBitmap = Bitmap.createBitmap(lBitmap, 0, 0, lBitmap.getWidth(), lBitmap.getHeight(), matrix, true);
            if (!Objects.equals(lBitmap, lTempBitmap)) lBitmap.recycle();
            Log.d("com.arcsoft", "check target Image:" + lTempBitmap.getWidth() + "×" + lTempBitmap.getHeight());
            return lTempBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
