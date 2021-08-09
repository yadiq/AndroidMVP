package com.hqumath.androidmvp.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hqumath.androidmvp.R;
import com.hqumath.androidmvp.base.BaseRecyclerAdapter;
import com.hqumath.androidmvp.base.BaseRecyclerViewHolder;
import com.hqumath.androidmvp.bean.CommitEntity;
import com.hqumath.androidmvp.bean.ReposEntity;
import com.hqumath.androidmvp.bean.UserInfoEntity;
import com.hqumath.androidmvp.utils.CommonUtil;

import java.util.List;

public class MyAdapters {

    //我的跟随
    public static class FollowRecyclerAdapter extends BaseRecyclerAdapter<UserInfoEntity> {
        public FollowRecyclerAdapter(Context context, List<UserInfoEntity> mData) {
            super(context, mData, R.layout.recycler_item_followers);
        }

        @Override
        public void convert(BaseRecyclerViewHolder holder, int position) {
            UserInfoEntity data = mData.get(position);
            holder.setText(R.id.tv_name, data.getLogin());
            ImageView ivHead = holder.getView(R.id.iv_head);
            if (!TextUtils.isEmpty(data.getAvatar_url())) {
                Glide.with(CommonUtil.getContext())
                        .load(data.getAvatar_url())
                        .circleCrop()
                        .into(ivHead);
            }
        }
    }
    //我的仓库
    public static class ReposRecyclerAdapter extends BaseRecyclerAdapter<ReposEntity> {
        public ReposRecyclerAdapter(Context context, List<ReposEntity> mData) {
            super(context, mData, R.layout.recycler_item_repos);
        }

        @Override
        public void convert(BaseRecyclerViewHolder holder, int position) {
            ReposEntity data = mData.get(position);
            holder.setText(R.id.tv_name, data.getName());
            holder.setText(R.id.tv_description, data.getDescription());
            holder.setText(R.id.tv_author, data.getOwner().getLogin());
        }
    }
    //提交记录
    public static class CommitsRecyclerAdapter extends BaseRecyclerAdapter<CommitEntity> {
        public CommitsRecyclerAdapter(Context context, List<CommitEntity> mData) {
            super(context, mData, R.layout.recycler_item_commits);
        }

        @Override
        public void convert(BaseRecyclerViewHolder holder, int position) {
            CommitEntity data = mData.get(position);
            holder.setText(R.id.tv_name, data.getCommit().getCommitter().getName());
            holder.setText(R.id.tv_message, data.getCommit().getMessage());
            holder.setText(R.id.tv_sha, data.getSha());
            String time = data.getCommit().getCommitter().getDate();
            time = time.replace("T", " ").replace("Z", "");
            holder.setText(R.id.tv_time, time);
        }
    }
}