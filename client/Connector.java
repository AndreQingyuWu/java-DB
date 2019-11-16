import java.io.*;
import java.net.Socket;
import java.util.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Connector{
    protected static String url;
    protected static int port;
    protected static String propertiesName = "Client.properties";
    protected BufferedReader in=null;
    protected PrintWriter out=null;
    protected Socket socket=null;
    protected Scanner scan=new Scanner(System.in,"UTF-8");
    static {
        FileInputStream in = null;
        try {
            Properties properties = new Properties();
            in = new FileInputStream(propertiesName);
            properties.load(in);
            port = Integer.parseInt(properties.getProperty("Port"));
            url = properties.getProperty("Url");
            System.out.println("Read properties success!");
        } catch (Exception e) {
            System.out.println("Falied to read properties!");
        }
    }

    public void connect()
    {
        try {
            socket = new Socket(url, port);
            socket.setKeepAlive(true);
            System.out.println("Client is ready. Welcome...");
        } catch (IOException e) {
            System.out.println("Falied to connect to server. Please restart.");
            return;
        }
        
        try {
             in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             out = new PrintWriter(socket.getOutputStream(), true);
            //business logic
            
        } catch (IOException e) {
            System.out.println("Server disconnected.");
        }
        return;
    }

    public void disconnect()
    {
        try {
            out.println("0");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exec(JSONObject json)
    {
        out.println(json.toString());
        String reply=null;
        try{
            reply=in.readLine();
        }
        catch(IOException e)
        {
            System.out.println("Disconnected. Please restart.");
            System.exit(0);
        }
        JSONObject jsonR=JSONObject.fromObject(reply);
        if(jsonR.getBoolean("return"))
        {
            System.out.println("Success!");
        }
        else
        {
            System.out.println("Failed!");
            System.out.println("Reason:"+jsonR.getString("reason"));
        }

    }

    private long getSID()
    {
        System.out.println("Please input your student ID:");
        long sID = 0;
        boolean flag1 = true;
        while (flag1) {
            try {
                sID = Long.parseLong(scan.nextLine());
                if (sID != 0)
                    flag1 = false;
            } catch (NumberFormatException e) {
                System.out.println("Please input a valid ID:");
            }
        }
        return sID;

    }

    private long getCID()
    {
        System.out.println("Please input course ID:");
        long cID = 0;
        boolean flag = true;
        while (flag) {
            try {
                cID = Long.parseLong(scan.nextLine());
                if (cID != 0)
                    flag = false;
            } catch (NumberFormatException e) {
                System.out.println("Please input a valid ID:");
            }
        }
        return cID;
    }

    public void printCourseCList() {
        JSONObject json = new JSONObject();
        json.put("classes", "Course");
        json.put("method", 2);
        out.println(json.toString());
        String reply = null;
        try {
            reply = in.readLine();
        } catch (IOException e) {
            System.out.println("Disconnected. Please restart.");
            System.exit(0);
        }
        if("1".equals(reply))
        {
            System.out.println("No course");
            return;
        }
        JSONObject jsonReply=JSONObject.fromObject(reply);
        JSONArray jsonArray = jsonReply.getJSONArray("courseList");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            Course c = new Course(obj);
            c.print();
        }
        return;
    }

    public long showStuName()
    {
        long sID=getSID();
        JSONObject json = new JSONObject();
        json.put("sID", sID);
        json.put("method", 5);
        json.put("classes", "Student");
        out.println(json.toString());
        String reply = null;
        try {
            reply = in.readLine();
        } catch (IOException e) {
            System.out.println("Disconnected. Please restart.");
            System.exit(0);
        }
        if ("1".equals(reply)) {
            System.out.println("This student doesn't exist.");
            return 0;
        }
        JSONObject jsonReply = JSONObject.fromObject(reply);
        System.out.println("Hello!" + jsonReply.getString("sName") + "\n");
        return sID;
    }

    public void selectACourse(long sID) {
        long cID=getCID();
        JSONObject json = new JSONObject();
        json.put("classes", "Select");
        json.put("method", 0);
        json.put("sID", sID);
        json.put("cID", cID);
        exec(json);
    }

    public void dropACourse(long sID) {
        long cID=getCID();
        JSONObject json = new JSONObject();
        json.put("classes", "Select");
        json.put("method", 1);
        json.put("sID", sID);
        json.put("cID", cID);
        exec(json);

    }

    public void addCourse()
    {
        System.out.println("Please input course ID Capacity and Name:");
        long cID = scan.nextLong();
        int cCapacity=scan.nextInt();
        String temp=scan.nextLine();
        String cName=scan.nextLine();
                
        JSONObject json = new JSONObject();
        json.put("classes", "Course");
        json.put("method", 0);
        json.put("cID", cID);
        json.put("cCapacity",cCapacity);
        json.put("cName",cName);
        exec(json);
    }

    public void deleteCourse()
    {
        long cID=getCID();
        JSONObject json = new JSONObject();
        json.put("classes", "Course");
        json.put("method", 1);
        json.put("cID", cID);
        exec(json);
    }

    public void printStudent()
    {
        long sID=getSID();
        JSONObject json = new JSONObject();
        json.put("classes", "Student");
        json.put("method", 5);
        json.put("sID", sID);
        out.println(json.toString());
        String reply = null;
        try {
            reply = in.readLine();
        } catch (IOException e) {
            System.out.println("Disconnected. Please restart.");
            System.exit(0);
        }
        if("1".equals(reply))
        {
            System.out.println("This student doesn't exist.");
            return;
        }

        JSONObject jsonReply = JSONObject.fromObject(reply);
        Student stu=new Student(jsonReply);
        stu.print();

        json.clear();

        json.put("classes", "Select");
        json.put("method", 2);
        json.put("sID", sID);
        out.println(json.toString());
        try {
            reply = in.readLine();
        } catch (IOException e) {
            System.out.println("Disconnected. Please restart.");
            System.exit(0);
        }
        if("1".equals(reply))
        {
            System.out.println("No select data.");
            return;
        }
        System.out.println("Select list:");
        jsonReply=JSONObject.fromObject(reply);
        JSONArray jsonArray = jsonReply.getJSONArray("selectArrayList");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            Select s = new Select(obj);
            System.out.println(s.getCourse());
        }
        
    }

    private void printStudent(long sID)
    {
        JSONObject json = new JSONObject();
        json.put("classes", "Student");
        json.put("method", 5);
        json.put("sID", sID);
        out.println(json.toString());
        String reply = null;
        try {
            reply = in.readLine();
        } catch (IOException e) {
            System.out.println("Disconnected. Please restart.");
            System.exit(0);
        }
        if("1".equals(reply))
        {
            System.out.println("This student doesn't exist.");
            return;
        }

        JSONObject jsonReply = JSONObject.fromObject(reply);
        Student stu=new Student(jsonReply);
        stu.print();
    }

    public void editStudent()
    {
        while (true) {
            System.out.println("\n5Please choose your option:\n1.Edit class\n2.Edit gender\n3.Edit name\n4.Return");
            int choice = scan.nextInt();
            switch (choice) {
            case 1:
                editStuClass();
                break;
            case 2:
                editStuGender();
                break;
            case 3:
                editStuName();
            case 4:
                return;
            default:
                System.out.println("Please input a valid number.");
            }
        }
    }

    public void addStudent()
    {
        System.out.println("Please input student ID Name Class Gender[male|female]:");
        long sID = scan.nextLong();
        String sName=scan.next();
        int sClass=scan.nextInt();
        String gender=scan.next();
        boolean sGender=true;
        if("male".equals(gender))
            sGender=false;

        JSONObject json = new JSONObject();
        json.put("classes", "Student");
        json.put("method", 0);
        json.put("sID", sID);
        json.put("sName", sName);
        json.put("sClass", sClass);
        json.put("sGender", sGender);
        exec(json);
    }

    public void deleteStudent()
    {
        long sID=getSID();
        JSONObject json = new JSONObject();
        json.put("classes", "Student");
        json.put("method", 1);
        json.put("sID", sID);
        exec(json);
    }

    private void editStuClass()
    {
        long sID=getSID();
        printStudent(sID);
        System.out.println("set class to:");
        int sClass=scan.nextInt();
        JSONObject json = new JSONObject();
        json.put("classes", "Student");
        json.put("method", 2);
        json.put("sID", sID);
        json.put("sClass",sClass);
        exec(json);
    }

    private void editStuGender()
    {
        long sID=getSID();
        printStudent(sID);
        System.out.println("set gender to:");
        String gender=scan.nextLine();
        JSONObject json = new JSONObject();
        json.put("classes", "Student");
        json.put("method", 3);
        json.put("sID", sID);
        if("male".equals(gender))
            json.put("sGender",false);
        else
            json.put("sGender",true);
        exec(json);
    }

    private void editStuName()
    {
        long sID=getSID();
        printStudent(sID);
        System.out.println("set name to:");
        String sName=scan.nextLine();
        JSONObject json = new JSONObject();
        json.put("classes", "Student");
        json.put("method", 4);
        json.put("sID", sID);
        json.put("sName",sName);
        exec(json);
    }

}