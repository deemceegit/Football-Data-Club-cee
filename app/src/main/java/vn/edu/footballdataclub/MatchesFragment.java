package vn.edu.footballdataclub;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MatchesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MatchesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String uri = "https://api.football-data.org/v4/matches";

    // Recycler data
    private RecyclerView recycler;
    private TextView tvNoMatches; // added empty-state TextView
    private ScoreAdapter adapter;
    private List<ListItem> items = new ArrayList<>();

    public MatchesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MatchesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MatchesFragment newInstance(String param1, String param2) {
        MatchesFragment fragment = new MatchesFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_matches, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views using the inflated view
        recycler = view.findViewById(R.id.recycler);
        tvNoMatches = view.findViewById(R.id.tvNoMatches);

        // Setup adapter and layout manager with a proper Context
        adapter = new ScoreAdapter(items);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        recycler.setAdapter(adapter);

        // Instantiate the RequestQueue with a proper Context
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        MatchRequest request = new MatchRequest(Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("uri", "response length=" + (response != null ? response.length() : 0));
                try {
                    JSONObject root = new JSONObject(response);
                    JSONArray matches = root.optJSONArray("matches");
                    if (matches == null || matches.length() == 0) {
                        items.clear();
                        adapter.notifyDataSetChanged();
                        recycler.setVisibility(View.GONE);
                        tvNoMatches.setVisibility(View.VISIBLE);
                    } else {
                        recycler.setVisibility(View.VISIBLE);
                        tvNoMatches.setVisibility(View.GONE);

                        items.clear();
                        for (int i = 0; i < matches.length(); i++) {
                            JSONObject m = matches.getJSONObject(i);

                            JSONObject competition = m.optJSONObject("competition");
                            String league = competition != null ? competition.optString("name", "") : "";

                            JSONObject place = m.optJSONObject("area");
                            String country = place != null ? place.optString("name", "") : "";

                            String status = m.optString("status", "");
                            String timeDisplay = "";
                            String utcDate = m.optString("utcDate", "");
                            if ("TIMED".equals(status)) {
                                if (utcDate != null && !utcDate.isEmpty()) {
                                    timeDisplay = formatUtcToLocalTime(utcDate);
                                }
                            } else if ("IN_PLAY".equals(status)) {
                                timeDisplay = "LIVE";
                            } else if ("FINISHED".equals(status)) {
                                timeDisplay = "FT";
                            } else {
                                if (utcDate != null && !utcDate.isEmpty()) {
                                    timeDisplay = formatUtcToLocalTime(utcDate);
                                }
                            }

                            JSONObject home = m.optJSONObject("homeTeam");
                            JSONObject away = m.optJSONObject("awayTeam");
                            String homeName = "";
                            String awayName = "";
                            if (home != null) {
                                homeName = home.optString("name", home.optString("Name", ""));
                            }
                            if (away != null) {
                                awayName = away.optString("name", away.optString("Name", ""));
                            }

                            String homeLogo = null;
                            String awayLogo = null;
                            if (home != null) {
                                String temp = home.optString("crest", "");
                                if (temp == null || temp.isEmpty()) temp = home.optString("crestUrl", "");
                                if (temp == null || temp.isEmpty()) temp = home.optString("logo", "");
                                if (temp != null && !temp.isEmpty()) homeLogo = temp;
                            }
                            if (away != null) {
                                String temp = away.optString("crest", "");
                                if (temp == null || temp.isEmpty()) temp = away.optString("crestUrl", "");
                                if (temp == null || temp.isEmpty()) temp = away.optString("logo", "");
                                if (temp != null && !temp.isEmpty()) awayLogo = temp;
                            }

                            Integer homeScore = null;
                            Integer awayScore = null;
                            JSONObject score = m.optJSONObject("score");
                            if (score != null) {
                                JSONObject full = score.optJSONObject("fullTime");
                                if (full != null) {
                                    if (!full.isNull("home") && full.has("home")) {
                                        try { homeScore = full.getInt("home"); } catch (JSONException ignored) {}
                                    }
                                    if (!full.isNull("away") && full.has("away")) {
                                        try { awayScore = full.getInt("away"); } catch (JSONException ignored) {}
                                    }
                                }
                            }

                            items.add(new ListItem.Header(league, country));
                            Log.d("HeaderAdded", "league='" + league + "' country='" + country + "'");
                            items.add(new ListItem.Match(
                                    String.valueOf(m.optInt("id", i)),
                                    timeDisplay,
                                    homeName,
                                    awayName,
                                    homeScore,
                                    awayScore,
                                    homeLogo,
                                    awayLogo
                            ));
                        }
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    Log.e("uri", "JSON parse error", e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("uri", "onErrorResponse: " + volleyError.getLocalizedMessage());
            }
        });

        queue.add(request);
    }

    private String formatUtcToLocalTime(String utcDate) {
        String[] patterns = new String[]{
                "yyyy-MM-dd'T'HH:mm:ss'Z'",
                "yyyy-MM-dd'T'HH:mm:ssX",
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                "yyyy-MM-dd'T'HH:mm:ss.SSSX"
        };

        Date parsed = null;
        for (String p : patterns) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(p, Locale.US);
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                parsed = sdf.parse(utcDate);
                if (parsed != null) break;
            } catch (ParseException ignored) {}
        }

        if (parsed == null) {
            int t = utcDate.indexOf('T');
            if (t >= 0 && utcDate.length() >= t + 6) {
                return utcDate.substring(t + 1, t + 6);
            }
            return utcDate;
        }

        SimpleDateFormat out = new SimpleDateFormat("HH:mm", Locale.getDefault());
        out.setTimeZone(TimeZone.getDefault());
        return out.format(parsed);
    }
}