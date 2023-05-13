package com.nekoid.smektuber.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nekoid.smektuber.R;
import com.nekoid.smektuber.helpers.utils.Utils;
import com.nekoid.smektuber.helpers.navigation.Navigator;
import com.nekoid.smektuber.models.ArticleModel;
import com.nekoid.smektuber.screen.home.article.DetailArticle;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterData extends RecyclerView.Adapter<AdapterData.MyViewHolder> {
    private final Activity activity;
    private final List<ArticleModel> articleModels;
    private AdapterData.Dialog dialog;

    public void setDialog(AdapterData.Dialog dialog) {
        this.dialog = dialog;
    }

    //    sintaks untuk mau membawa data dari card yang di klick
    public interface Dialog{
        void onClick(ArticleModel menuArtikelDashboard);
    }

    public AdapterData(Activity activity, List<ArticleModel> articles) {
        this.activity = activity;
        this.articleModels = articles;
    }

    @NonNull
    @Override
    public AdapterData.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_data, parent, false);
        return new AdapterData.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterData.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.DataTextArtikel.setText(articleModels.get(position).title != null ? articleModels.get(position).title : "");
        holder.ImageArtikel.setImageBitmap(Utils.downloadImage(articleModels.get(position).thumbnail));
        holder.articleModel = articleModels.get(position);
    }

    @Override
    public int getItemCount() {
        return articleModels.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ArticleModel articleModel;
        ImageView articleThumbnail;
        TextView articleDescription;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            articleThumbnail = itemView.findViewById(R.id.ImageArtikel);
            articleDescription = itemView.findViewById(R.id.DataTextArtikel);

            itemView.setOnClickListener(v -> {
                Navigator.of(activity).push(DetailArticle.class, articleModel);
                if (dialog!=null){
                    dialog.onClick(articleModels.get(getLayoutPosition()));
                }
            });
        }
    }
}