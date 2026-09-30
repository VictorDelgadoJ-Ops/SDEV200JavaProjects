//victor delgado
//p. 157

import java.util.Scanner;

public class TestTeam {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);

		Team team1 = setTeamData(input);
		Team team2 = setTeamData(input);
		Team team3 = setTeamData(input);

		displayTeam(team1);
		displayTeam(team2);
		displayTeam(team3);
	}

	public static Team setTeamData(Scanner input) {
		System.out.print("Enter the high school name: ");
		String highSchool = input.nextLine();

		System.out.print("Enter the sport: ");
		String sport = input.nextLine();

		System.out.print("Enter the team name: ");
		String teamName = input.nextLine();

		System.out.print("Enter the team motto: ");
		String motto = input.nextLine();

		Team temporaryTeam = new Team(highSchool, sport, teamName, motto);
		return temporaryTeam;
	}

	public static void displayTeam(Team team) {
		System.out.println("\nHigh school: " + team.getHighSchool());
		System.out.println("Sport: " + team.getSport());
		System.out.println("Team name: " + team.getTeamName());
		System.out.println("Motto: " + team.getMotto());
	}
}
