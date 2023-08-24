package com.yalantis.ucrop.model;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by Oleksii Shliama [https://github.com/shliama] on 6/21/16.
 */
public class CropParameters {

    private final int mMaxResultImageSizeX;
    private final int mMaxResultImageSizeY;

    private final Bitmap.CompressFormat mCompressFormat;
    private final int mCompressQuality;
    private final Uri mImageInputUri;
    private final String mImageOutputPath;
    private final ExifInfo mExifInfo;


    public CropParameters(int maxResultImageSizeX, int maxResultImageSizeY,
                          Bitmap.CompressFormat compressFormat, int compressQuality,
                          Uri imageInputUri, String imageOutputPath, ExifInfo exifInfo) {
        mMaxResultImageSizeX = maxResultImageSizeX;
        mMaxResultImageSizeY = maxResultImageSizeY;
        mCompressFormat = compressFormat;
        mCompressQuality = compressQuality;
        mImageInputUri = imageInputUri;
        mImageOutputPath = imageOutputPath;
        mExifInfo = exifInfo;
    }

    public int getMaxResultImageSizeX() {
        return mMaxResultImageSizeX;
    }

    public int getMaxResultImageSizeY() {
        return mMaxResultImageSizeY;
    }

    public Bitmap.CompressFormat getCompressFormat() {
        return mCompressFormat;
    }

    public int getCompressQuality() {
        return mCompressQuality;
    }

    public Uri getImageInputUri() {
        return mImageInputUri;
    }

    public String getImageOutputPath() {
        return mImageOutputPath;
    }

    public ExifInfo getExifInfo() {
        return mExifInfo;
    }

}
