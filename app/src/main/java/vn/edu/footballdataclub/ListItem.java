package vn.edu.footballdataclub;

public abstract class ListItem {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_MATCH = 1;

    public abstract int getType();

    // ---------- HEADER ----------
    public static class Header extends ListItem {
        private String league;
        private String country;

        public Header(String league, String country) {
            this.league = league;
            this.country = country;
        }

        public String getLeague() {
            return league;
        }

        public String getCountry() {
            return country;
        }

        @Override
        public int getType() {
            return TYPE_HEADER;
        }
    }

    // ---------- MATCH ----------
    public static class Match extends ListItem {
        private String id;
        private String time;
        private String homeTeam;
        private String awayTeam;
        private Integer homeScore;
        private Integer awayScore;
        private String homeLogoUrl;
        private String awayLogoUrl;


        public Match(String id, String time, String homeTeam, String awayTeam,
                     Integer homeScore, Integer awayScore,
                     String homeLogoUrl, String awayLogoUrl) {
            this.id = id;
            this.time = time;
            this.homeTeam = homeTeam;
            this.awayTeam = awayTeam;
            this.homeScore = homeScore;
            this.awayScore = awayScore;
            this.homeLogoUrl = homeLogoUrl;
            this.awayLogoUrl = awayLogoUrl;

        }

        public String getId() { return id; }
        public String getTime() { return time; }
        public String getHomeTeam() { return homeTeam; }
        public String getAwayTeam() { return awayTeam; }
        public Integer getHomeScore() { return homeScore; }
        public Integer getAwayScore() { return awayScore; }
        public String getHomeLogoUrl() { return homeLogoUrl; }
        public String getAwayLogoUrl() { return awayLogoUrl; }


        @Override
        public int getType() {
            return TYPE_MATCH;
        }
    }
}