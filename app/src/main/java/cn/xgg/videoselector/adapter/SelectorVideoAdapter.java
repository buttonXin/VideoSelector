package cn.xgg.videoselector.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.xgg.videoselector.R;
import cn.xgg.videoselector.VideoInfo;
import cn.xgg.videoselector.VideoUtils;


/**
 * Created by Administrator on 2017/5/24 0024.
 */

public class SelectorVideoAdapter extends BaseAdapterRV<VideoInfo> {


    private Context mContext;
    private int mSelectorPosition = -1;

    @Override
    public RecyclerView.ViewHolder createVHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new SelectorVideoHolder(LayoutInflater.
                from(parent.getContext()).inflate(R.layout.item_selector_video , parent ,false));
    }

    @Override
    protected void onBindVH(RecyclerView.ViewHolder viewHolder, int position, VideoInfo videoInfo) {

        SelectorVideoHolder holder= (SelectorVideoHolder) viewHolder;

        String path = videoInfo.getPath();
        //使用path操作获取图片 很费时！要使用glide 或者在子线程中获取
        if (!TextUtils.isEmpty(path)){
            Glide.with(mContext).load(path).asBitmap().into(holder.mImageView);
            //这个方法是通过path获取视频第一帧，也是耗时操作！放到子线程 要不界面很卡
            // holder.mImageView.setImageBitmap(VideoUtils.getVideoThumbnail(path));
        }

        if (position == mSelectorPosition){
            holder.mSelectorView.setBackground(mContext.getResources().
                    getDrawable(R.drawable.video_check_select));
        }else {
            holder.mSelectorView.setBackground(mContext.getResources()
                    .getDrawable(R.drawable.video_check));
        }

    }

    /**
     * 设置选中哪一个
     * @param position
     */
    public void setSelectorPosition(int position){
        if (mSelectorPosition == position){
            mSelectorPosition = -1;
        } else {
            mSelectorPosition = position ;
        }

        notifyDataSetChanged();
    }

    /**
     * 获取被选中的position
     * @return
     */
    public int getSelectorPosition() {
        return mSelectorPosition;
    }


    class SelectorVideoHolder extends Holder {
        private final ImageView mImageView;
        private final View mSelectorView;

        public SelectorVideoHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image_item_video);
            mSelectorView = itemView.findViewById(R.id.view_selector);
        }
    }
}
