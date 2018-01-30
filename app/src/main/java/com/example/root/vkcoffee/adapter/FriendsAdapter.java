package com.example.root.vkcoffee.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.root.vkcoffee.MainActivity;
import com.example.root.vkcoffee.R;
import com.example.root.vkcoffee.retrofit.Friend;

import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;

/**
 * Created by root on 24.1.18.
 */


/**
 * Created by root on 24.12.17.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsAdapterViewHolder> {
    public static final String TAG = FriendsAdapter.class.getSimpleName();

    private List<Friend> jcAudioList;

    private static OnItemClickListener mListener ;
    public MainActivity activity;

    // Define the mListener interface
    public interface OnItemClickListener {
        void onItemClick(int position);

        void onSongItemDeleteClicked(int position);
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }



    public FriendsAdapter(List<Friend> jcAudioList, Context context) {
        this.jcAudioList = jcAudioList;
        activity = (MainActivity) context;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(jcAudioList.get(position).getId());
    }

    @Override
    public FriendsAdapter.FriendsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friends, parent, false);
        return new FriendsAdapterViewHolder(view);
//        audiosViewHolder.itemView.setOnClickListener(this);
//        return audiosViewHolder;
    }

    @Override
    public void onBindViewHolder(FriendsAdapterViewHolder holder, final int position) {

        IImageLoader imageLoader1;
        String title = jcAudioList.get(position).getName();
        holder.name.setText(title);
        imageLoader1 = new PicassoLoader();
        if(jcAudioList.get(position).getImg().contains("http"))imageLoader1.loadImage(holder.icon, jcAudioList.get(position).getImg(), "загрузка...");
        else imageLoader1.loadImage(holder.icon, "https://vk.com/images/camera_100.png", "загрузка...");
    }


    @Override
    public int getItemCount() {
        return jcAudioList == null ? 0 : jcAudioList.size();
    }


    static class FriendsAdapterViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private AvatarView  icon;

        public FriendsAdapterViewHolder(View view){
            super(view);
            this.name = (TextView) view.findViewById(R.id.friend_name);
            this.icon = (AvatarView) view.findViewById(R.id.header_avatar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (mListener != null) mListener.onItemClick(getAdapterPosition());
                }
            });


        }
    }
}

