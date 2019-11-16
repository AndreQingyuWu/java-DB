package server;

import java.util.Scanner;


//server入口
//Esther
public class CourseServer {
    
    public static void main(String[] args) {

        ThreadPooledServer server = new ThreadPooledServer();
        Scanner scan=new Scanner(System.in);
        String tmp=null;
        boolean isStart=false;
        while(true)
        {
            if(scan.hasNext())
                tmp=scan.nextLine();
            if("start".equals(tmp))
                {
                    if(!isStart)
                    {
                        new Thread(server).start();
                        isStart=true;
                    }
                    else
                    {
                        System.out.println("Already started.");
                    }
                }
            else if("stop".equals(tmp))
            {
                if(isStart)
                {
                    server.stop();
                    break;
                }
                else
                {
                    System.out.println("Please start service first.");
                }  
            }
        }
        scan.close();
        return;
    }
}
