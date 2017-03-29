package com.yulu.zhaoxinpeng.mytreasuremap;

import android.app.Fragment;
import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import com.yulu.zhaoxinpeng.mytreasuremap.commons.ActivityUtils;

import java.io.FileDescriptor;

/**
 * Created by Administrator on 2017/3/27.
 */

public class Mp4Fragment extends Fragment implements TextureView.SurfaceTextureListener {

    private ActivityUtils mactivityUtils;
    private TextureView mtextureView;
    private MediaPlayer mMediaPlayer;

    /**
     * 视频播放：
     * 1. MediaPlayer播放视频
     * 2. TextureView展示视图播放
     * 使用TextureView的时候我们需要SufaceTexure，渲染、呈现我们播放的内容
     */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mactivityUtils = new ActivityUtils(getActivity());

        // Fragment全屏显示播放视频的控件
        mtextureView = new TextureView(getActivity());

        return mtextureView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 当我们所要展示的视频播放的控件已经准备好的时候，就可以播放视频
        // 什么时候准备好呢，我们可以设置一个监听，看有没有准备好或者是有没有变化
        mtextureView.setSurfaceTextureListener(this);

    }

    @Override
    public void onSurfaceTextureAvailable(final SurfaceTexture surface, int width, int height) {
        /**
         * 视频展示的控件已经准备好了，可以播放视频了
         * 1. 找到播放的资源
         * 2. 什么时候可以播放：使用MediaPlayer播放，当他准备好的时候可以播放
         * 3. 基本设置在播放之前可以设置一下：播放到哪个上面、设置循环播放等
         */
        try {
            //打开播放的资源文件
            AssetFileDescriptor openFd = getActivity().getAssets().openFd("welcome.mp4");

            // 拿到MediaPlayer需要的资源类型
            FileDescriptor fileDescriptor = openFd.getFileDescriptor();

            mMediaPlayer = new MediaPlayer();

            // 设置播放的资源给MediaPlayer
            mMediaPlayer.setDataSource(fileDescriptor, openFd.getStartOffset(), openFd.getLength());

            //异步准备
            mMediaPlayer.prepareAsync();

            // 设置准备的监听：看一下有没有准备好，可不可以播放
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    Surface mSurface = new Surface(surface);

                    mMediaPlayer.setSurface(mSurface);

                    mMediaPlayer.setLooping(true);

                    mMediaPlayer.start();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            mactivityUtils.showToast("无法播放媒体文件！");
        }
    }

    // 视频展示的大小变化结束的时候
    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    // 销毁的时候
    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    // 更新的时候
    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mMediaPlayer != null) {
            //释放资源
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}

