package cn.xgg.videoselector;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/26 0026.
 */

public class VideoUtils {

    /**
     * 数据库操作，如果内容多了会阻塞线程， 尽量在线程中获取
     * @param context
     * @return
     */
    public static List<VideoInfo> getVideoInfoList(Context context){
        List<VideoInfo> list = null;
        if (context == null) return null;

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null,
                null, null);

        if (cursor == null) return null;

        list = new ArrayList<>();
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
            VideoInfo video = new VideoInfo(id, title, album, artist, displayName,
                                            mimeType, path, size, duration);
            list.add(video);
        }
        cursor.close();


        return list;
    }


    /**
     * 给出url，获取视频的第一帧
     *
     * @param fileName
     * @return
     */
    public static Bitmap getVideoThumbnail(String fileName) {
        Bitmap bitmap = null;
        //MediaMetadataRetriever 是android中定义好的一个类，提供了统一
        //的接口，用于从输入的媒体文件中取得帧和元数据；
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据文件路径获取缩略图
            retriever.setDataSource(fileName);
           // retriever.setDataSource(url , new HashMap());这是现上的url
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
    }

}
