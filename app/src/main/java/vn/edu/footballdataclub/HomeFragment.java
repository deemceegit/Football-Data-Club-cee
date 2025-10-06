package vn.edu.footballdataclub;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private ViewPager2 viewPager2;
    private Handler handler;
    private int currentPage = 0;
    private int[] imgs = {R.drawable.img1, R.drawable.img2, R.drawable.img3};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager2 = view.findViewById(R.id.slideShowContainer);
        SlideShowAdapter slideShowAdapter = new SlideShowAdapter(imgs);
        viewPager2.setAdapter(slideShowAdapter);
        handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (currentPage == imgs.length) {
                    currentPage = 0;
                }
                viewPager2.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);

        RecyclerView recyclerView = view.findViewById(R.id.viewClubs);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Club> clubs = new ArrayList<>();
        clubs.add(new Club(1, "Real Madrid", "   ESP", 480, R.drawable.ic_real_madrid, R.drawable.ic_spain));
        clubs.add(new Club(2, "Inter Milan", "   ITA", 469, R.drawable.ic_inter_milan, R.drawable.ic_italy));
        clubs.add(new Club(3, "Paris Saint-Germain", "   FRA", 462, R.drawable.ic_psg, R.drawable.ic_france));
        clubs.add(new Club(4, "Barcelona", "   ESP", 434 , R.drawable.ic_barcelona, R.drawable.ic_spain));
        clubs.add(new Club(5, "Bayern Munich", "   DEU", 411, R.drawable.ic_bayern_munich, R.drawable.ic_germany));
        clubs.add(new Club(6, "Chelsea", "   ENG", 386, R.drawable.ic_chelsea, R.drawable.ic_england));
        clubs.add(new Club(7, "Arsenal", "   ENG", 362, R.drawable.ic_arsenal, R.drawable.ic_england));
        clubs.add(new Club(8, "Atlético Madrid", "   ESP", 361, R.drawable.ic_atletico_madrid, R.drawable.ic_spain));
        clubs.add(new Club(9, "Borussia Dortmund", "   DEU", 349, R.drawable.ic_borussia_dortmund, R.drawable.ic_germany));
        clubs.add(new Club(10, "Liverpool", "   ENG", 337, R.drawable.ic_liverpool, R.drawable.ic_england));
        HomeRankingAdapter homeRankingAdapter = new HomeRankingAdapter(clubs);
        recyclerView.setAdapter(homeRankingAdapter);
        return view;
    }
}