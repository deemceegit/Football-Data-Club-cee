package vn.edu.footballdataclub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Color;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ScoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ListItem> items;

    public ScoreAdapter(List<ListItem> items) {
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == ListItem.TYPE_HEADER) {
            View view = inflater.inflate(R.layout.item_header, parent, false);
            return new HeaderVH(view);
        } else {
            View view = inflater.inflate(R.layout.item_match, parent, false);
            return new MatchVH(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListItem item = items.get(position);

        if (holder instanceof HeaderVH && item instanceof ListItem.Header) {
            ((HeaderVH) holder).bind((ListItem.Header) item);
        } else if (holder instanceof MatchVH && item instanceof ListItem.Match) {
            ((MatchVH) holder).bind((ListItem.Match) item);
        }
    }

    // ---------- Header ViewHolder ----------
    static class HeaderVH extends RecyclerView.ViewHolder {
        private TextView tvLeague, tvCountry;

        HeaderVH(View itemView) {
            super(itemView);
            tvLeague = itemView.findViewById(R.id.tvLeague);
            tvCountry = itemView.findViewById(R.id.tvCountry);
        }

        void bind(ListItem.Header header) {
            tvLeague.setText(header.getLeague());
            tvCountry.setText(header.getCountry());
        }
    }

    // ---------- Match ViewHolder ----------
    static class MatchVH extends RecyclerView.ViewHolder {
        private TextView tvTime, tvHome, tvAway, tvScoreHome, tvScoreAway;
        private ImageView ivHome, ivAway;


        MatchVH(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvHome = itemView.findViewById(R.id.tvHome);
            tvAway = itemView.findViewById(R.id.tvAway);
            tvScoreHome = itemView.findViewById(R.id.tvScoreHome);
            tvScoreAway = itemView.findViewById(R.id.tvScoreAway);
            ivHome = itemView.findViewById(R.id.ivHome);
            ivAway = itemView.findViewById(R.id.ivAway);

        }

        void bind(final ListItem.Match match) {
            tvTime.setText(match.getTime());
            // color LIVE red
            if (match.getTime() != null && "LIVE".equalsIgnoreCase(match.getTime())) {
                tvTime.setTextColor(Color.RED);
            } else {
                tvTime.setTextColor(Color.BLACK);
            }

            tvHome.setText(match.getHomeTeam());
            tvAway.setText(match.getAwayTeam());

            tvScoreHome.setText(match.getHomeScore() != null ? String.valueOf(match.getHomeScore()) : "-");
            tvScoreAway.setText(match.getAwayScore() != null ? String.valueOf(match.getAwayScore()) : "-");

            // Load logos with Glide (placeholder if null/empty)
            String hLogo = match.getHomeLogoUrl();
            String aLogo = match.getAwayLogoUrl();

            Glide.with(ivHome.getContext())
                    .load(hLogo)
                    .placeholder(R.drawable.img_5)
                    .into(ivHome);

            Glide.with(ivAway.getContext())
                    .load(aLogo)
                    .placeholder(R.drawable.img_5)
                    .into(ivAway);


        }
    }
}