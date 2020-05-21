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
import com.example.glumacfilmovi.db.model.FavoriteFIlmovi;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterFavoritFilm extends RecyclerView.Adapter<AdapterFavoritFilm.ViewHolder> {

    private OnItemClickListener listener;
    private List<FavoriteFIlmovi> filmovi;
    private Context context;

    public AdapterFavoritFilm(Context context, OnItemClickListener listener, List<FavoriteFIlmovi> filmovi) {
        this.context = context;
        this.listener = listener;
        this.filmovi = filmovi;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.film_row, parent, false );
        return new ViewHolder( view, listener );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvIme.setText( filmovi.get( position ).getmNaziv() );
        holder.tvGodina.setText( filmovi.get( position ).getmGodina() );
         holder.tvType.setText( filmovi.get( position ).getmType() );
        Picasso.with( context ).load( filmovi.get( position ).getmImage() ).into( holder.ivPoster );

    }

    @Override
    public int getItemCount() {
        return filmovi.size();
    }

    public void clear() {
        filmovi.clear();
    }

    public void addAll(List<FavoriteFIlmovi> list) {
        filmovi.addAll( list );
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivPoster;
        TextView tvIme;
        TextView tvGodina;
        TextView tvType;

        OnItemClickListener vhListener;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super( itemView );
            tvIme = itemView.findViewById( R.id.tvTitle );
            tvGodina = itemView.findViewById( R.id.tvYear );
              tvType = itemView.findViewById( R.id.tvType );
            ivPoster = itemView.findViewById( R.id.ivPoster );


            this.vhListener = listener;
            itemView.setOnClickListener( this );

        }

        @Override
        public void onClick(View v) {
            vhListener.myOnItemClick( getAdapterPosition() );
        }
    }

    public interface OnItemClickListener {
        void myOnItemClick(int position);
    }
}
