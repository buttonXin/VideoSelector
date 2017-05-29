package cn.xgg.videoselector;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.xgg.videoselector.adapter.BaseAdapterRV;
import cn.xgg.videoselector.adapter.SelectorVideoAdapter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2017/5/26 0026.
 */

public class VideoFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private List<VideoInfo> mVideoInfoList = new ArrayList<>();
    private SelectorVideoAdapter mAdapter;
    private Button mBtn_ok;
    private VideoInfo mVideoInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_video, container , false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_selector_video);
        mBtn_ok = (Button) view.findViewById(R.id.btn_ok);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
        getVideoInfoList();
    }

    private void initView() {
        GridLayoutManager linearLayoutManager =  new GridLayoutManager(getActivity() , 3);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new SelectorVideoAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setItemOnClickListener(new BaseAdapterRV.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, Object o) {
                VideoInfo info = (VideoInfo) o;

                mAdapter.setSelectorPosition(position);

                if (mAdapter.getSelectorPosition() != -1){
                    mVideoInfo = info ;
                    mBtn_ok.setText("已选中");
                }else {
                    mVideoInfo = null ;
                    mBtn_ok.setText("请选择");
                }

            }
        });

        mBtn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVideoInfo == null){
                    Toast.makeText(getActivity(), "请您选择视频", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    Toast.makeText(getActivity(), "视频名称："+mVideoInfo.getDisplayName(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    /**
     * 获取本地视频资源是耗时操作！！！！可以不使用rxjava 线程中操作也可以
     */
    private void getVideoInfoList() {
        Cursor cursor = getActivity().getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null,
                null, null);

        if (cursor == null) return;
        Observable.just(cursor)
                .map(new Func1<Cursor, List<VideoInfo>>() {
                    @Override
                    public List<VideoInfo> call(Cursor cursor) {
                        return cursorToList(cursor);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<VideoInfo>>() {
                    @Override
                    public void call(List<VideoInfo> videoInfos) {
                        mAdapter.addData(videoInfos);

                        mAdapter.notifyDataSetChanged();

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    /**
     * 将扫描的视频添加到集合中
     * @param cursor
     * @return
     */
    private List<VideoInfo> cursorToList(Cursor cursor) {

        mVideoInfoList.clear();
        VideoInfo videoInfo;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media._ID));
            String title = cursor
                    .getString(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
            String album = cursor
                    .getString(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM));

            String artist = cursor
                    .getString(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));
            String displayName = cursor
                    .getString(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
            String mimeType = cursor
                    .getString(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
            String path = cursor
                    .getString(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
            long duration = cursor
                    .getInt(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
            long size = cursor
                    .getLong(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));

            videoInfo = new VideoInfo(id, title,album, artist, displayName,
                    mimeType, path, size, duration );
            mVideoInfoList.add(videoInfo);
        }
        cursor.close();

        return mVideoInfoList;
    }
}
