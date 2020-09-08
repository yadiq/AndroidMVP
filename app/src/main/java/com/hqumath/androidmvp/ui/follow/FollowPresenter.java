package com.hqumath.androidmvp.ui.follow;

import com.hqumath.androidmvp.base.BasePresenter;
import com.hqumath.androidmvp.net.BaseApi;
import com.hqumath.androidmvp.net.HandlerException;
import com.hqumath.androidmvp.net.RetrofitClient;
import com.hqumath.androidmvp.net.listener.HttpOnNextListener;
import com.hqumath.androidmvp.net.service.MainService;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import io.reactivex.Observable;
import retrofit2.Retrofit;

/**
 * ****************************************************************
 * 文件名称: LoginPresenter
 * 作    者: Created by gyd
 * 创建时间: 2019/1/21 15:12
 * 文件描述:
 * 注意事项:
 * 版权声明:
 * ****************************************************************
 */
public class FollowPresenter extends BasePresenter<FollowContract.View> implements FollowContract.Presenter {
    private RxAppCompatActivity activity;

    public FollowPresenter(RxAppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public void getFollowers(int per_page, long page, int tag, boolean isShowProgress) {
        if (!isViewAttached()) {
            return;
        }
        BaseApi baseApi = new BaseApi(new HttpOnNextListener() {

            @Override
            public void onNext(Object o) {
                mView.onSuccess(o, tag);
            }

            @Override
            public void onError(HandlerException.ResponseThrowable e) {
                mView.onError(e.getMessage(), e.getCode(), tag);
            }
        }, activity) {
            @Override
            public Observable getObservable(Retrofit retrofit) {
                return retrofit.create(MainService.class).getFollowers(per_page, page);
            }
        };
        baseApi.setShowProgress(isShowProgress);
        RetrofitClient.getInstance().sendHttpRequest(baseApi);
    }

    @Override
    public void getUserInfo(String userName, int tag, boolean isShowProgress) {
        if (!isViewAttached()) {
            return;
        }
        BaseApi baseApi = new BaseApi(new HttpOnNextListener() {

            @Override
            public void onNext(Object o) {
                mView.onSuccess(o, tag);
            }

            @Override
            public void onError(HandlerException.ResponseThrowable e) {
                mView.onError(e.getMessage(), e.getCode(), tag);
            }
        }, activity) {
            @Override
            public Observable getObservable(Retrofit retrofit) {
                return retrofit.create(MainService.class).getUserInfo(userName);
            }
        };
        baseApi.setShowProgress(isShowProgress);
        RetrofitClient.getInstance().sendHttpRequest(baseApi);
    }
}
