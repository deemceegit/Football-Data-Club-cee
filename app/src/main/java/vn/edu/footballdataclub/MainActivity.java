package vn.edu.footballdataclub;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager2);

        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            ImageView imageView = new ImageView(this);
            switch (position) {
                case 0:
                    imageView.setImageResource(R.drawable.ic_home);
                    break;
                case 1:
                    imageView.setImageResource(R.drawable.ic_chart);
                    break;
                case 2:
                    imageView.setImageResource(R.drawable.ic_calendar);
                    break;
                case 3:
                    imageView.setImageResource(R.drawable.ic_global);
                    break;
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100, 100);
            imageView.setLayoutParams(params);
            tab.setCustomView(imageView);
        }).attach();
    }
}