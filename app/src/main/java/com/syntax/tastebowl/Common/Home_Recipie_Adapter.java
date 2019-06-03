package com.syntax.tastebowl.Common;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.syntax.tastebowl.PojoClass.RecipieBean;
import com.syntax.tastebowl.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Home_Recipie_Adapter  extends RecyclerView.Adapter<Home_Recipie_Adapter.ViewHolder> {


    Context context;
    List<RecipieBean> ls;
    public Home_Recipie_Adapter(List<RecipieBean> getDataAdapter, Context context){


        super();
        this.ls = getDataAdapter;
        this.context = context;
    }

    @Override
    public Home_Recipie_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_home_recipies, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Home_Recipie_Adapter.ViewHolder holder, int position) {
        RecipieBean obj_recipe =  ls.get(position);

//        Log.d("adaptet data",obj_recipe.getItem_name()+"");
        holder.rid.setText(obj_recipe.getRid());
        holder.rname.setText(obj_recipe.getRecipie_name());
        holder.rating.setText(obj_recipe.getRating());
        holder.imag.getLayoutParams().height=250;
        holder.imag.getLayoutParams().width=250;
        holder.imag.setScaleType(ImageView.ScaleType.FIT_XY);

        String base = obj_recipe.getImage();


        try {
            byte[] imageAsBytes = Base64.decode(base.getBytes());

            holder.imag.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length) );
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return  ls.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView rid;
        public TextView rname;
        public TextView rating;
        public ImageView imag;



        public ViewHolder(View view) {

            super(view);

            rid = (TextView) view.findViewById(R.id.custom_home_recipie_id);
            rname = (TextView) view.findViewById(R.id.custom_home_recipie_name);
            rating = (TextView) view.findViewById(R.id.custom_home_recipie_rating);
            imag = (ImageView) view.findViewById(R.id.custom_home_recipie_img);



        }
    }
}
