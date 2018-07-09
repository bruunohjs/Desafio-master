package br.com.brunocardoso.desafionw.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import br.com.brunocardoso.desafionw.R;
import br.com.brunocardoso.desafionw.model.Hero;

public class HeroesAdapter extends RecyclerView.Adapter<HeroesAdapter.HeroesViewHolder> {
    private List<Hero> heroList;
    private OnItemClickListener mOnItemClickListener;

    public HeroesAdapter() {
    }

    public HeroesAdapter(List<Hero> heroList,
                         OnItemClickListener onItemClickListener) {
        this.heroList = heroList;
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public HeroesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adaptar_heroes, parent, false);

        HeroesViewHolder vh = new HeroesViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull HeroesViewHolder holder, final int position) {
        Hero hero = heroList.get( position );

        holder.tvHeroeName.setText( hero.getName() );
        holder.tvHeroeClass.setText( hero.getClassName() );

        if (hero.getPhotos().size() > 0){

            Glide.with(holder.ivHeroeAvatar.getContext()).load(
                    String.format("http://heroes.qanw.com.br:7266/photos/%s", hero.getPhotos().get(0))
            )
                .into( holder.ivHeroeAvatar );

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return heroList.size();
    }

    class HeroesViewHolder extends RecyclerView.ViewHolder{

        private TextView tvHeroeName, tvHeroeClass;
        private ImageView ivHeroeAvatar;

        public HeroesViewHolder(View itemView) {
            super(itemView);

            tvHeroeName = itemView.findViewById(R.id.tv_name_id);
            tvHeroeClass = itemView.findViewById(R.id.tv_class_id);
            ivHeroeAvatar = itemView.findViewById(R.id.iv_avatar_id);

            tvHeroeName.setBackgroundColor(Color.TRANSPARENT);
            tvHeroeClass.setBackgroundColor(Color.TRANSPARENT);
            ivHeroeAvatar.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
}
