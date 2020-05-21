package com.example.glumacfilmovi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.glumacfilmovi.R;
import com.example.glumacfilmovi.net.model1.Search;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterListaFilmova extends RecyclerView.Adapter<AdapterListaFilmova.MyViewHolder> {

    private Context context;
    private List<Search> filmItem;
    private OnItemClickListener listener;


    private OnItemLongClickListener longClickListener;


    public AdapterListaFilmova(Context context, List<Search> film, OnItemClickListener listener, OnItemLongClickListener longClickListener) {
        this.context = context;
        this.filmItem = film;
        this.listener = listener;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.film_row, parent, false );

        return new MyViewHolder( view, listener,longClickListener  );
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvNaziv.setText( filmItem.get( position ).getTitle() );
        holder.tvGodina.setText( filmItem.get( position ).getYear() );
        holder.tvType.setText( filmItem.get( position ).getType() );///
        Picasso.with( context ).load( filmItem.get( position ).getPoster() ).into( holder.ivSlika );

    }

    @Override
    public int getItemCount() {
        return filmItem.size();
    }

    public Search get(int position) {
        return filmItem.get( position );
    }

    public void removeAll() {
        filmItem.clear();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvNaziv;
        private TextView tvGodina;
        private TextView tvType;
        private ImageView ivSlika;
        private OnItemClickListener vhListener;


        private OnItemLongClickListener longClickListener;


        MyViewHolder(@NonNull View itemView, OnItemClickListener vhListener, final OnItemLongClickListener longClickListener) {
            super( itemView );

            tvNaziv = itemView.findViewById( R.id.tvTitle );
            tvGodina = itemView.findViewById( R.id.tvYear );
            tvType = itemView.findViewById( R.id.tvType );
            ivSlika = itemView.findViewById( R.id.ivPoster );
            this.vhListener = vhListener;
            itemView.setOnClickListener( this );



            // TODO ovde ga setujem
            this.longClickListener = longClickListener;

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longClickListener.onItemLongClick( getAdapterPosition() );
                    return true;
                }
            });

        }

        @Override
        public void onClick(View v) {
            vhListener.onItemClick( getAdapterPosition() );
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }


}


