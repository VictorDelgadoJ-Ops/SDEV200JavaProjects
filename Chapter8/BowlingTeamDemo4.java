//victor Delgado
//p. 282
import java.util.*;

public class BowlingTeamDemo4 {
    public static void getTeamData(BowlingTeam[] teams) {
        String name;
        int x;
        int y;
        final int numTeamMembers = 4;
        Scanner input = new Scanner(System.in);

        for (y = 0; y < teams.length; ++y) {
            teams[y] = new BowlingTeam();
            System.out.print("Enter team name >> ");
            name = input.nextLine();
            teams[y].setTeamName(name);
            for (x = 0; x < numTeamMembers; ++x) {
                System.out.print("Enter team member's name >> ");
                name = input.nextLine();
                teams[y].setMember(x, name);
            }
        }
    }

    public static void main(String[] args) {
        String name;
        final int numTeams = 4;
        BowlingTeam[] teams = new BowlingTeam[numTeams];
        int x;
        int y;
        final int numTeamMembers = 4;
        Scanner input = new Scanner(System.in);

        getTeamData(teams);

        for (y = 0; y < numTeams; ++y) {
            System.out.println("\nMembers of team " + teams[y].getTeamName());
            for (x = 0; x < numTeamMembers; ++x)
                System.out.print(teams[y].getMember(x) + " ");
            System.out.println();
        }
        System.out.print("\n Enter a team name to display its members >> ");
        if (input.hasNextLine()) {
            name = input.nextLine();
            for (y = 0; y < numTeams; ++y) {
                if (name.equals(teams[y].getTeamName())) {
                    System.out.println("\nMembers of team " + teams[y].getTeamName());
                    for (x = 0; x < numTeamMembers; ++x)
                        System.out.print(teams[y].getMember(x) + " ");
                    System.out.println();
                }
            }
        } else {
            System.out.println("\nNo team name was entered.");
        }
    }
}
