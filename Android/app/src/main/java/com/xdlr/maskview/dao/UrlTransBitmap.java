package com.xdlr.maskview.dao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bumptech.glide.Glide;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UrlTransBitmap {

    public Bitmap returnBitMap(final String url, final Context myContext) {

        final Bitmap[] bm = new Bitmap[1];
        ExecutorService es = Executors.newCachedThreadPool();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    bm[0] = Glide.with(myContext).asBitmap().load(url).dontAnimate().submit().get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        es.submit(task);
        es.shutdown();

        while (true) {
            if (es.isTerminated()) {
                return bm[0];
            }
        }
    }
}
