//victor Delgado
//p. 282
import java.util.*;

public class BowlingTeamDemo2 {
    public static void main(String[] args) {
        String name;
        final int numTeams = 4;
        BowlingTeam[] teams = new BowlingTeam[numTeams];
        int x;
        int y;
        final int numTeamMembers = 4;
        Scanner input = new Scanner(System.in);

        for (y = 0; y < numTeams; ++y) {
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

        for (y = 0; y < numTeams; ++y) {
            System.out.println("\nMembers of team " + teams[y].getTeamName());
            for (x = 0; x < numTeamMembers; ++x)
                System.out.print(teams[y].getMember(x) + " ");
            System.out.println();
        }
    }
}
