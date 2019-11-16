package test;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import net.sf.json.*;
public class DBRunnable implements Runnable{
    protected Socket socket = null;

    public DBRunnable(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            System.out.println(Thread.currentThread().getName()+" running");
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            String response = null;
            
            while (socket.isConnected()&&!socket.isClosed()) {
                String request = in.readLine();
                 //TODO 测试用
                 System.out.println(Thread.currentThread().getName()+" request:"+request);
                 JSONObject jsonObj = JSONObject.fromObject(request);
                // business login
                //close socket
                if ("0".equals(request)) {
                    break;
                }
                //parse json and handle request
               int command=jsonObj.getInt("method");
               JSONObject json = new JSONObject();
               switch(command)
               {
                   case 3:
                   case 4:
                   case 5:
                   case 6:
                   case 7:
                   case 9:
                   case 10:
                   case 11:
                    json.put("return",true);
                    break;
                   case 1:
                    json.put("sName","Tom");
                    json.put("sClass",1702);
                    json.put("sGender",false);
                    json.put("sID",20179248);
                    break;
                   case 2:
                   case 12:
                    response="1";
                    break;
                   case 8:
                    json.put("cName","Math");
                    json.put("cCapacity",100);
                    json.put("cRemain",20);
                    break;
                    

               }
                
                if(!("1".equals(response)))
                    response=json.toString();
                Thread.sleep(2000);
                //TODO 测试用
                System.out.println(Thread.currentThread().getName()+" response:"+response);
                out.println(response);
                Thread.sleep(200);
            }
        }
        catch(IOException e)
        {
            System.out.println(Thread.currentThread().getName()+" disconnect");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        try {
            socket.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }



}