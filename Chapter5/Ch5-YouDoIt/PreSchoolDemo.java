//victor delgado
//p. 189

public class PreSchoolDemo {
	public static void main(String[] args) {
		PreschoolStudent ps1 = new PreschoolStudent(2345, 5);
		PreschoolStudent ps2 = new PreschoolStudent(67890, 8);

		display(ps1);
		display(ps2);

		ps1.setAge(6);
		display(ps1);

		ps1.setAge(7);
		display(ps1);
	}

	public static void display(PreschoolStudent student) {
		System.out.println("Student ID " + student.getIdNum()
				+ "   Student age " + student.getAge());
	}
}
