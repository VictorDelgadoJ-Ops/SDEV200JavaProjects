public class BowlingTeam {
    private String teamName;
    private String[] members = new String[4];

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setMember(int x, String name) {
        members[x] = name;
    }

    public String getMember(int x) {
        return members[x];
    }
}