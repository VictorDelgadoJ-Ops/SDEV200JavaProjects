//vicotr delgado
//p. 157

public class Team {
	private String highSchool;
	private String sport;
	private String teamName;
	private String motto;

	public Team(String highSchool, String sport, String teamName, String motto) {
		this.highSchool = highSchool;
		this.sport = sport;
		this.teamName = teamName;
		this.motto = motto;
	}

	public String getHighSchool() {
		return highSchool;
	}

	public String getSport() {
		return sport;
	}

	public String getTeamName() {
		return teamName;
	}

	public String getMotto() {
		return motto;
	}
}
