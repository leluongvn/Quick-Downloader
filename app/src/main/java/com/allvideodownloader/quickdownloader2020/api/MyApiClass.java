package com.allvideodownloader.quickdownloader2020.api;

import android.app.Activity;

import com.allvideodownloader.quickdownloader2020.model.TiktokModel;
import com.allvideodownloader.quickdownloader2020.model.TwitterResponse;
import com.allvideodownloader.quickdownloader2020.util.Utils;
import com.google.gson.JsonObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MyApiClass {
    private static Activity mActivity;
    private static MyApiClass myApiClass;

    public static MyApiClass getInstance(Activity activity) {
        if (myApiClass == null) {
            myApiClass = new MyApiClass();
        }
        mActivity = activity;
        return myApiClass;
    }

    public void callResult(final DisposableObserver observer, String url, String cookie) {
        if (Utils.isNullOrEmpty(cookie)) {
            cookie = "";
        }
        RestClient.getInstance(mActivity).getService().callResult(url, cookie,
                "Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(JsonObject o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void twitterApi(final DisposableObserver observer, String url, String twitterModel) {
        RestClient.getInstance(mActivity).getService().callTwitter(url, twitterModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TwitterResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(TwitterResponse o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void getTiktokVideo(final DisposableObserver observer, String url) {
        RestClient.getInstance(mActivity).getService().getTiktokData(Utils.TikTokUrl,url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TiktokModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(TiktokModel o) {
                        observer.onNext(o);
                    }
                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }
                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

}