package com.yulu.zhaoxinpeng.mytreasuremap.activity.user.account;

import android.util.Log;

import com.yulu.zhaoxinpeng.mytreasuremap.R;
import com.yulu.zhaoxinpeng.mytreasuremap.activity.user.UserPrefs;
import com.yulu.zhaoxinpeng.mytreasuremap.net.NetClient;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/4/10.
 */

public class AccountPresenter {

    private AccountView mAccountView;

    public AccountPresenter(AccountView accountView) {
        mAccountView = accountView;
    }

    // 上传及更新头像
    public void uploadPhoto(File file){

        // 显示进度
        mAccountView.showProgress();

        // 构建上传的文件的部分
        MultipartBody.Part part = MultipartBody.Part.createFormData("file","photo.png", RequestBody.create(null,file));

        Call<UploadResult> upload = NetClient.getInstance().getTreasureApi().upload(part);
        upload.enqueue(mResultCallback);
    }

    private Callback<UploadResult> mResultCallback =  new Callback<UploadResult>() {

        @Override
        public void onResponse(Call<UploadResult> call, Response<UploadResult> response) {
            if (response.isSuccessful()){
                UploadResult uploadResult = response.body();
                if (uploadResult==null){
                    // 显示信息
                    mAccountView.showToast("未知的错误");
                    return;
                }
                // 显示信息
                mAccountView.showToast(uploadResult.getMsg());
                if (uploadResult.getCount()!=1){
                    return;
                }
                // 可以拿到头像地址数据
                String photoUrl = uploadResult.getUrl();

                // 可以存储到用户仓库里面、展示出来
                UserPrefs.getInstance().setPhoto(NetClient.BaseUrl+photoUrl);
                // 将上传的头像在页面上展示出来
                mAccountView.updatePhoto(NetClient.BaseUrl+photoUrl);

                // 更新数据
                // 需要截取一下
                String substring = photoUrl.substring(photoUrl.lastIndexOf("/") + 1, photoUrl.length());
                Update update = new Update(UserPrefs.getInstance().getTokenid(),substring);
                Call<UpdateResult> resultCall = NetClient.getInstance().getTreasureApi().update(update);
                resultCall.enqueue(mUpdateCallback);
            }
        }

        @Override
        public void onFailure(Call<UploadResult> call, Throwable t) {
            // 隐藏进度
            mAccountView.hideProgress();
            // 提示信息
            mAccountView.showToast("上传失败："+t.getMessage());
        }
    };

    // 更新的回调
    private Callback<UpdateResult> mUpdateCallback = new Callback<UpdateResult>() {

        @Override
        public void onResponse(Call<UpdateResult> call, Response<UpdateResult> response) {
            // 隐藏进度
            mAccountView.hideProgress();
            if (response.isSuccessful()){
                UpdateResult updateResult = response.body();
                if (updateResult==null){
                    // 提示信息
                    mAccountView.showToast("未知的错误");
                    return;
                }
                // 提示信息
                mAccountView.showToast(updateResult.getMsg());
                if (updateResult.getCode()!=1){
                    return;
                }
                // 其实也可以在这里去处理头像的展示等。
            }
        }

        @Override
        public void onFailure(Call<UpdateResult> call, Throwable t) {
            // 进度隐藏
            mAccountView.hideProgress();
            // 提示信息
            mAccountView.showToast("更新失败："+t.getMessage());
        }
    };
}
