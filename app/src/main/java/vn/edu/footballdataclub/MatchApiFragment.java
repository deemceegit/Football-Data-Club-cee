package vn.edu.footballdataclub;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;

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

public class MatchApiFragment extends Fragment {
    private static final String uri = "https://api.football-data.org/v4/matches";

    private RecyclerView recycler;
    private TextView tvNoMatches;
    private ScoreAdapter adapter;
    private List<ListItem> items = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_matchapi, container, false);

        recycler = view.findViewById(R.id.recycler);
        tvNoMatches = view.findViewById(R.id.tvNoMatches);
        adapter = new ScoreAdapter(items);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        recycler.setAdapter(adapter);

        loadMatches();

        return view;
    }


    private void loadMatches() {
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
                            } else if (utcDate != null && !utcDate.isEmpty()) {
                                timeDisplay = formatUtcToLocalTime(utcDate);
                            }

                            JSONObject home = m.optJSONObject("homeTeam");
                            JSONObject away = m.optJSONObject("awayTeam");
                            String homeName = home != null ? home.optString("name", "") : "";
                            String awayName = away != null ? away.optString("name", "") : "";

                            String homeLogo = extractLogo(home);
                            String awayLogo = extractLogo(away);

                            Integer homeScore = null, awayScore = null;
                            JSONObject score = m.optJSONObject("score");
                            if (score != null) {
                                JSONObject full = score.optJSONObject("fullTime");
                                if (full != null) {
                                    if (full.has("home")) homeScore = full.optInt("home");
                                    if (full.has("away")) awayScore = full.optInt("away");
                                }
                            }

                            items.add(new ListItem.Header(league, country));
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
                Log.e("uri", "Volley error: " + volleyError.getLocalizedMessage());
            }
        });

        queue.add(request);
    }

    private String extractLogo(JSONObject team) {
        if (team == null) return null;
        String[] keys = {"crest", "crestUrl", "logo"};
        for (String key : keys) {
            String val = team.optString(key, "");
            if (val != null && !val.isEmpty()) return val;
        }
        return null;
    }

    private String formatUtcToLocalTime(String utcDate) {
        String[] patterns = {
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

        if (parsed == null) return utcDate;
        SimpleDateFormat out = new SimpleDateFormat("HH:mm", Locale.getDefault());
        out.setTimeZone(TimeZone.getDefault());
        return out.format(parsed);
    }
}