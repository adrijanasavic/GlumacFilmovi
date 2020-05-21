package com.example.glumacfilmovi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.glumacfilmovi.R;
import com.example.glumacfilmovi.net.model1.Search;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterListaFilmova extends RecyclerView.Adapter<AdapterListaFilmova.MyViewHolder> {

    private Context context;
    private ArrayList<Search> searchItem;
    private OnItemClickListener listener;


    public AdapterListaFilmova(Context context, ArrayList<Search> searchItem, OnItemClickListener listener) {
        this.context = context;
        this.searchItem = searchItem;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.film_row, parent, false );

        return new MyViewHolder( view, listener );
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvTitle.setText( searchItem.get( position ).getTitle() );
        holder.tvYear.setText( searchItem.get( position ).getYear() );
        holder.tvType.setText( searchItem.get( position ).getType() );
        Picasso.with( context ).load( searchItem.get( position ).getPoster() ).into( holder.ivMalaSlika );

    }

    @Override
    public int getItemCount() {
        return searchItem.size();
    }

    public Search get(int position) {
        return searchItem.get( position );
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private TextView tvTitle;
        private TextView tvYear;
        private TextView tvType;

        private ImageView ivMalaSlika;
        private OnItemClickListener vhListener;


        MyViewHolder(@NonNull View itemView, OnItemClickListener vhListener) {
            super( itemView );

            ivMalaSlika = itemView.findViewById( R.id.ivPoster );
            tvTitle = itemView.findViewById( R.id.tvTitle );
            tvYear = itemView.findViewById( R.id.tvYear );
            tvType = itemView.findViewById( R.id.tvType );
            this.vhListener = vhListener;
            itemView.setOnClickListener( this );

        }

        @Override
        public void onClick(View v) {
            vhListener.onItemClick( getAdapterPosition() );
        }


    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
