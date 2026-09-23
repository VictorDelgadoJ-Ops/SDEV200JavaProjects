//victor Delgado
//p. 281
import java.util.*;

public class BowlingTeamDemo {
    public static void main(String[] args) {
        String name;
        BowlingTeam bowlTeam = new BowlingTeam();
        int x;
        final int numTeamMembers = 4;
        Scanner input = new Scanner(System.in);
        System.out.print("Enter team name >> ");
        name = input.nextLine();
        bowlTeam.setTeamName(name);
        for (x = 0; x < numTeamMembers; ++x) {
            System.out.print("Enter team member's name >> ");
            name = input.nextLine();
            bowlTeam.setMember(x, name);
        }
        System.out.println("\nMembers of team " + bowlTeam.getTeamName());
        for (x = 0; x < numTeamMembers; ++x)
            System.out.print(bowlTeam.getMember(x) + " ");
        System.out.println();
    }
}
