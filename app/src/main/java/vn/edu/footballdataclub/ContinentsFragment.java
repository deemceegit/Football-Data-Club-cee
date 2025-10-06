package vn.edu.footballdataclub;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContinentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContinentsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ContinentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContinentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContinentsFragment newInstance(String param1, String param2) {
        ContinentsFragment fragment = new ContinentsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_continents, container, false);
        ViewPager2 childViewPager = view.findViewById(R.id.vpChild);
        TabLayout tabLayoutContinents = view.findViewById(R.id.tlContinents);

        childViewPager.setAdapter(new ViewPagerAdapter4Continents(this));

        new TabLayoutMediator(tabLayoutContinents, childViewPager, (tab, position) -> {
            ImageView imageView = new ImageView(requireContext());
            switch (position) {
                case 0:
                    imageView.setImageResource(R.drawable.afc);
                    break;
                case 1:
                    imageView.setImageResource(R.drawable.caf);
                    break;
                case 2:
                    imageView.setImageResource(R.drawable.concacaf);
                    break;
                case 3:
                    imageView.setImageResource(R.drawable.conmebol);
                    break;
                case 4:
                    imageView.setImageResource(R.drawable.uefa);
                    break;
            }

            tab.setCustomView(imageView);
        }).attach();

        return view;
    }
}