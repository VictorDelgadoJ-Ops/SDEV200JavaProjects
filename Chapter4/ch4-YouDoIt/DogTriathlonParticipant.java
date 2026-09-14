//Victor Delgado
//p. 141

public class DogTriathlonParticipant {
    private final int Num_OF_EVENTS;
    private static int TotalCumulativeScore = 0;
    private String Name;
    private int ObedienceScore;
    private int ConformationScore;
    private int AgilityScore;
    private int Total;
    private double Average;

    public DogTriathlonParticipant(String name, int score1, int score2, int score3) {
        this(name, 3, score1, score2, score3);
    }

    public DogTriathlonParticipant(String name, int numEvents, int score1, int score2, int score3) {
        Name = name;
        Num_OF_EVENTS = numEvents;
        ObedienceScore = score1;
        ConformationScore = score2;
        AgilityScore = score3;
        Total = score1 + score2 + score3;
        Average = (double) Total / Num_OF_EVENTS;
        TotalCumulativeScore = TotalCumulativeScore + Total;
    }

    public void display() {
        System.out.println(Name + " participated in " + Num_OF_EVENTS +
                " events and has an average score of " + Average);
        System.out.println(Name + " has a total score of " + Total +
                " bringing the total cumulative score to " + TotalCumulativeScore);
    }
}
