package com.bestnewsapp.sendnewsdata;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nixxmare on 1/21/2017.
 */

public class NewsSiteListAdapter extends RecyclerView.Adapter<NewsSiteListAdapter.MyViewHolder>{
    private LayoutInflater inflater;


    private List<NewsSitesList> newsSitesLists = new ArrayList<>();
    private Context context;

    public NewsSiteListAdapter(Context context , List<NewsSitesList> list) {
        inflater = LayoutInflater.from(context);
        this.newsSitesLists = list;
        this.context= context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_new_card, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String logoUrl;
        NewsSitesList newsSitesList = newsSitesLists.get(position);
        holder.sitesName.setText(newsSitesList.getNewsSiteName());
        logoUrl = newsSitesList.getNewsLogoUrl();
        if(logoUrl.isEmpty()){
            logoUrl = "http://www.company-name-generator.com/blog/wp-content/uploads/2010/10/BMW_logo_small.png";
        }
        Picasso.with(context).load(logoUrl).into(holder.logo);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context , SingleSiteAllData.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsSitesLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView logo;
        TextView sitesName;

        public MyViewHolder(View itemView) {
            super(itemView);

            logo = (ImageView) itemView.findViewById(R.id.newsLogo);
            sitesName = (TextView) itemView.findViewById(R.id.newsSiteName);

        }




    }
}
