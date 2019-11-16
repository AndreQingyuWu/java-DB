
import java.util.*;

public class Client {

    protected Connector con=null;
    Scanner scan = null;
    Client()
    {;}

    public void start() {
        scan = new Scanner(System.in);
        con=new Connector();
        con.connect();
        while (true) {
            System.out.println("\nPlease choose your option:\n1.Select course\n2.Student management\n3.Course management\n4.Close");
            int choice = scan.nextInt();
            switch (choice) {
            case 1:
                selectCourse();
                break;
            case 2:
                studentManage();
                break;
            case 3:
                courseManage();
                break;
            case 4:
                con.disconnect();
                return;
            default:
                System.out.println("Please input a valid number.");
            }
        }
    }

    // is disconnected stop the client
    private void selectCourse() {
        long sID=con.showStuName();
        if(sID==0)
            return;
        while (true) {
            con.printCourseCList();
            System.out.println("\nPlease choose your option:\n1.Select\n2.Drop\n3.Return");
            int choice = scan.nextInt();
            switch (choice) {
            case 1:
                con.selectACourse(sID);
                break;
            case 2:
                con.dropACourse(sID);
                break;
            case 3:
                return;
            default:
                System.out.println("Please input a valid number.");
            }
        }
    }

    private void studentManage() {
        while (true) {
            System.out.println(
                    "\nPlease choose your option:\n1.Get student info\n2.Edit student info\n3.Add student\n4.Delete student\n5.return");
            int choice = scan.nextInt();
            switch (choice) {
            case 1:
                con.printStudent();
                break;
            case 2:
                con.editStudent();
                break;
            case 3:
                con.addStudent();
                break;
            case 4:
                con.deleteStudent();
            case 5:
                return;
            default:
                System.out.println("Please input a valid number.");
            }
        }
    }

    private void courseManage() {
        while (true) {
            con.printCourseCList();
            System.out.println("\nPlease choose your option:\n1.add\n2.delete\n3.Return");
            int choice = scan.nextInt();
            switch (choice) {
            case 1:
                con.addCourse();
                break;
            case 2:
                con.deleteCourse();
                break;
            case 3:
                return;
            default:
                System.out.println("Please input a valid number.");
            }
        }
    }
}