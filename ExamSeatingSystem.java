
import java.util.*;
import java.io.*;

public class ExamSeatingSystem {

    static Scanner sc = new Scanner(System.in);

    static ArrayList<Student> students = new ArrayList<>();
    static ArrayList<Room> rooms = new ArrayList<>();
    static ArrayList<Exam> exams = new ArrayList<>();
    static ArrayList<Allocation> allocations = new ArrayList<>();

    // Student class
    static class Student {
        String rollNo, name;

        Student(String rollNo, String name) {
            this.rollNo = rollNo;
            this.name = name;
        }
    }

    // Room class
    static class Room {
        String roomNo;
        int capacity;

        Room(String roomNo, int capacity) {
            this.roomNo = roomNo;
            this.capacity = capacity;
        }
    }

    // Examination class
    static class Exam {
        String examCode, subject, date, time;

        Exam(String examCode, String subject,
             String date, String time) {
            this.examCode = examCode;
            this.subject = subject;
            this.date = date;
            this.time = time;
        }
    }

    // Allocation class
    static class Allocation {
        String examCode, rollNo, roomNo;
        int seatNo;

        Allocation(String examCode, String rollNo,
                   String roomNo, int seatNo) {
            this.examCode = examCode;
            this.rollNo = rollNo;
            this.roomNo = roomNo;
            this.seatNo = seatNo;
        }
    }

    // Add Student
    static void addStudent() {
        System.out.print("Enter Roll Number: ");
        String roll = sc.nextLine();

        for (Student s : students) {
            if (s.rollNo.equalsIgnoreCase(roll)) {
                System.out.println("Student already exists!");
                return;
            }
        }

        System.out.print("Enter Student Name: ");
        String name = sc.nextLine();

        students.add(new Student(roll, name));
        System.out.println("Student added successfully!");
    }

    // Display Students
    static void displayStudents() {
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        System.out.println("\n--- STUDENT LIST ---");

        for (Student s : students) {
            System.out.println(
                "Roll No: " + s.rollNo +
                " | Name: " + s.name
            );
        }
    }

    // Add Room
    static void addRoom() {
        System.out.print("Enter Room Number: ");
        String roomNo = sc.nextLine();

        for (Room r : rooms) {
            if (r.roomNo.equalsIgnoreCase(roomNo)) {
                System.out.println("Room already exists!");
                return;
            }
        }

        System.out.print("Enter Room Capacity: ");
        int capacity = readInt();

        if (capacity <= 0) {
            System.out.println("Invalid room capacity!");
            return;
        }

        rooms.add(new Room(roomNo, capacity));
        System.out.println("Room added successfully!");
    }

    // Display Rooms
    static void displayRooms() {
        if (rooms.isEmpty()) {
            System.out.println("No rooms available.");
            return;
        }

        System.out.println("\n--- ROOM DETAILS ---");

        for (Room r : rooms) {
            System.out.println(
                "Room: " + r.roomNo +
                " | Capacity: " + r.capacity
            );
        }
    }

    // Add Examination
    static void addExam() {
        System.out.print("Enter Exam Code: ");
        String code = sc.nextLine();

        for (Exam e : exams) {
            if (e.examCode.equalsIgnoreCase(code)) {
                System.out.println("Exam already exists!");
                return;
            }
        }

        System.out.print("Enter Subject Name: ");
        String subject = sc.nextLine();

        System.out.print("Enter Exam Date: ");
        String date = sc.nextLine();

        System.out.print("Enter Exam Time: ");
        String time = sc.nextLine();

        exams.add(new Exam(code, subject, date, time));

        System.out.println("Examination added successfully!");
    }

    // Display Examinations
    static void displayExams() {
        if (exams.isEmpty()) {
            System.out.println("No examinations found.");
            return;
        }

        System.out.println("\n--- EXAMINATION DETAILS ---");

        for (Exam e : exams) {
            System.out.println(
                "Code: " + e.examCode +
                " | Subject: " + e.subject +
                " | Date: " + e.date +
                " | Time: " + e.time
            );
        }
    }

    // Allocate Seats
    static void allocateSeats() {
        if (students.isEmpty() || rooms.isEmpty()) {
            System.out.println(
                "Please add students and rooms first."
            );
            return;
        }

        if (exams.isEmpty()) {
            System.out.println("Please add an examination first.");
            return;
        }

        System.out.print("Enter Exam Code: ");
        String code = sc.nextLine();

        Exam selectedExam = null;

        for (Exam e : exams) {
            if (e.examCode.equalsIgnoreCase(code)) {
                selectedExam = e;
                break;
            }
        }

        if (selectedExam == null) {
            System.out.println("Examination not found!");
            return;
        }

        // Prevent duplicate allocation
        for (Allocation a : allocations) {
            if (a.examCode.equalsIgnoreCase(code)) {
                System.out.println(
                    "Seats are already allocated for this exam."
                );
                return;
            }
        }

        // Sort students by roll number
        ArrayList<Student> sortedStudents =
            new ArrayList<>(students);

        sortedStudents.sort(
            Comparator.comparing(s -> s.rollNo)
        );

        int studentIndex = 0;

        System.out.println("\nAllocating Seats...");

        for (Room room : rooms) {

            int seat = 1;

            while (seat <= room.capacity &&
                   studentIndex < sortedStudents.size()) {

                Student s = sortedStudents.get(studentIndex);

                allocations.add(
                    new Allocation(
                        code, s.rollNo, room.roomNo, seat
                    )
                );

                System.out.println(
                    s.rollNo + " -> Room " +
                    room.roomNo + " -> Seat " + seat
                );

                seat++;
                studentIndex++;
            }
        }

        int unallocated =
            sortedStudents.size() - studentIndex;

        System.out.println("\nAllocation Completed!");

        if (unallocated > 0) {
            System.out.println(
                unallocated + " student(s) could not be allocated."
            );

            System.out.println("Unallocated Students:");

            while (studentIndex < sortedStudents.size()) {
                System.out.println(
                    sortedStudents.get(studentIndex).rollNo
                );
                studentIndex++;
            }
        }
    }

    // Display Seating Plan
    static void displaySeatingPlan() {
        System.out.print("Enter Exam Code: ");
        String code = sc.nextLine();

        boolean found = false;

        System.out.println("\n--- ROOM-WISE SEATING PLAN ---");

        for (Room room : rooms) {

            boolean roomFound = false;

            for (Allocation a : allocations) {

                if (a.examCode.equalsIgnoreCase(code) &&
                    a.roomNo.equalsIgnoreCase(room.roomNo)) {

                    if (!roomFound) {
                        System.out.println(
                            "\nRoom Number: " + room.roomNo
                        );
                        roomFound = true;
                        found = true;
                    }

                    Student student = findStudent(a.rollNo);

                    if (student != null) {
                        System.out.println(
                            "Seat " + a.seatNo +
                            " | Roll No: " + student.rollNo +
                            " | Name: " + student.name
                        );
                    }
                }
            }
        }

        if (!found) {
            System.out.println("No seating plan found.");
        }
    }

    // Search Student
    static void searchStudent() {
        System.out.print("Enter Exam Code: ");
        String code = sc.nextLine();

        System.out.print("Enter Student Roll Number: ");
        String roll = sc.nextLine();

        for (Allocation a : allocations) {

            if (a.examCode.equalsIgnoreCase(code) &&
                a.rollNo.equalsIgnoreCase(roll)) {

                System.out.println("\nAllocation Found!");
                System.out.println("Roll Number: " + a.rollNo);
                System.out.println("Room Number: " + a.roomNo);
                System.out.println("Seat Number: " + a.seatNo);
                return;
            }
        }

        System.out.println("Student allocation not found.");
    }

    // Find Student
    static Student findStudent(String roll) {
        for (Student s : students) {
            if (s.rollNo.equalsIgnoreCase(roll)) {
                return s;
            }
        }
        return null;
    }

    // Read integer safely
    static int readInt() {
        while (true) {
            try {
                int value = Integer.parseInt(sc.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }

    // Save Data to Files
    static void saveData() {
        try {
            PrintWriter sw = new PrintWriter("students.txt");
            for (Student s : students) {
                sw.println(s.rollNo + "|" + s.name);
            }
            sw.close();

            PrintWriter rw = new PrintWriter("rooms.txt");
            for (Room r : rooms) {
                rw.println(r.roomNo + "|" + r.capacity);
            }
            rw.close();

            PrintWriter ew = new PrintWriter("exams.txt");
            for (Exam e : exams) {
                ew.println(
                    e.examCode + "|" + e.subject +
                    "|" + e.date + "|" + e.time
                );
            }
            ew.close();

            PrintWriter aw = new PrintWriter("allocations.txt");
            for (Allocation a : allocations) {
                aw.println(
                    a.examCode + "|" + a.rollNo +
                    "|" + a.roomNo + "|" + a.seatNo
                );
            }
            aw.close();

            System.out.println("All records saved successfully!");

        } catch (IOException e) {
            System.out.println("Error saving records: " + e.getMessage());
        }
    }

    // Load Data from Files
    static void loadData() {
        try {
            File file = new File("students.txt");
            if (file.exists()) {
                Scanner fs = new Scanner(file);
                while (fs.hasNextLine()) {
                    String[] data = fs.nextLine().split("\\|", -1);
                    if (data.length == 2) {
                        students.add(new Student(data[0], data[1]));
                    }
                }
                fs.close();
            }

            file = new File("rooms.txt");
            if (file.exists()) {
                Scanner fs = new Scanner(file);
                while (fs.hasNextLine()) {
                    String[] data = fs.nextLine().split("\\|", -1);
                    if (data.length == 2) {
                        rooms.add(new Room(
                            data[0], Integer.parseInt(data[1])
                        ));
                    }
                }
                fs.close();
            }

            file = new File("exams.txt");
            if (file.exists()) {
                Scanner fs = new Scanner(file);
                while (fs.hasNextLine()) {
                    String[] data = fs.nextLine().split("\\|", -1);
                    if (data.length == 4) {
                        exams.add(new Exam(
                            data[0], data[1], data[2], data[3]
                        ));
                    }
                }
                fs.close();
            }

            file = new File("allocations.txt");
            if (file.exists()) {
                Scanner fs = new Scanner(file);
                while (fs.hasNextLine()) {
                    String[] data = fs.nextLine().split("\\|", -1);
                    if (data.length == 4) {
                        allocations.add(new Allocation(
                            data[0], data[1], data[2],
                            Integer.parseInt(data[3])
                        ));
                    }
                }
                fs.close();
            }

            System.out.println("Previous records loaded.");

        } catch (Exception e) {
            System.out.println("Error loading records: " + e.getMessage());
        }
    }

    // Main Method
    public static void main(String[] args) {

        loadData();

        int choice;

        do {
            System.out.println("\n==================================");
            System.out.println(" EXAM SEATING AND ROOM ALLOCATION");
            System.out.println("==================================");

            System.out.println("1. Add Student");
            System.out.println("2. Display Students");
            System.out.println("3. Add Examination Room");
            System.out.println("4. Display Rooms");
            System.out.println("5. Add Examination");
            System.out.println("6. Allocate Seats");
            System.out.println("7. Display Seating Plan");
            System.out.println("8. Search Student Allocation");
            System.out.println("9. Exit");

            System.out.print("\nEnter Your Choice: ");
            choice = readInt();

            switch (choice) {

                case 1:
                    addStudent();
                    break;

                case 2:
                    displayStudents();
                    break;

                case 3:
                    addRoom();
                    break;

                case 4:
                    displayRooms();
                    break;

                case 5:
                    addExam();
                    break;

                case 6:
                    allocateSeats();
                    break;

                case 7:
                    displaySeatingPlan();
                    break;

                case 8:
                    searchStudent();
                    break;

                case 9:
                    saveData();
                    System.out.println("Thank you for using the system!");
                    break;

                default:
                    System.out.println("Invalid choice! Try again.");
            }

        } while (choice != 9);

        sc.close();
    }
}
