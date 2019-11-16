package server;

import java.io.*;
import java.net.Socket;
import net.sf.json.*;
import org.apache.commons.pool2.ObjectPool;
import pool.SocketUtil;

//server业务处理线程
//Esther

public class WorkerRunnable implements Runnable {
    protected ObjectPool<Socket> pool=null;
    protected Socket clientSocket = null;
    

    public WorkerRunnable(Socket clientSocket,ObjectPool<Socket> pool) {
        this.clientSocket = clientSocket;
        this.pool=pool;
        
    }

    public void run() {
        try {
            System.out.println(Thread.currentThread().getName()+" running");
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String response = null;
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
            
            SocketUtil readerUtil = new SocketUtil(pool);
            while (clientSocket.isConnected()&&!clientSocket.isClosed()) {
                String request = in.readLine();
                if(request==null)
                {
                	continue;
                }
                 //TODO 测试用
                 System.out.println(Thread.currentThread().getName()+" request:"+request);
                // business login
                //close socket
                if ("0".equals(request)) {
                    break;
                }
                //parse json and handle request
                JSONObject jsonObj = JSONObject.fromObject(request);
                JSONObject requestObj=new JSONObject();
               
                if("Select".equals(jsonObj.getString("classes")))
                {
                    if(0==jsonObj.getInt("method"))
                    {
                        //select
                        requestObj.put("method",3);
                        requestObj.put("sID",String.format("%08d", jsonObj.getLong("sID")));
                        requestObj.put("cID",String.format("%010d", jsonObj.getLong("cID")));
                    }
                    else if(1==jsonObj.getInt("method"))
                    {
                        //drop
                        requestObj.put("method",4);
                        requestObj.put("sID",String.format("%08d", jsonObj.getLong("sID")));
                        requestObj.put("cID",String.format("%010d", jsonObj.getLong("cID")));
                    }
                    else if(2==jsonObj.getInt("method"))
                    {
                        //get select list
                        requestObj.put("method",2);  
                        requestObj.put("sID",String.format("%08d", jsonObj.getLong("sID")));
                    }
                }
                else if("Course".equals(jsonObj.getString("classes")))
                {
                    if(0==jsonObj.getInt("method"))
                    {
                        //add class
                        requestObj.put("method",9);
                        requestObj.put("cID",String.format("%010d", jsonObj.getLong("cID")));
                        requestObj.put("cName",jsonObj.getString("cName"));
                        requestObj.put("cCapacity",jsonObj.getInt("cCapacity"));
                    }
                    else if(1==jsonObj.getInt("method"))
                    {
                        //delete class
                        requestObj.put("method",10);
                        requestObj.put("cID",String.format("%010d", jsonObj.getLong("cID")));
                    }
                    else if(2==jsonObj.getInt("method"))
                    {
                        //get class list
                        requestObj.put("method",12);
                    }

                }
                else if("Student".equals(jsonObj.getString("classes")))
                {
                    if(0==jsonObj.getInt("method"))
                    {
                        //add Student
                        requestObj.put("method",6);
                        requestObj.put("sID",String.format("%08d", jsonObj.getLong("sID")));
                        requestObj.put("sName",jsonObj.getString("sName"));
                        requestObj.put("sClass",jsonObj.getInt("sClass"));
                        requestObj.put("sGender",jsonObj.getBoolean("sGender"));
                    }
                    else if(1==jsonObj.getInt("method"))
                    {
                        //delete student
                        requestObj.put("method",7);
                        requestObj.put("sID",String.format("%08d", jsonObj.getLong("sID")));
                    }
                    else if(2==jsonObj.getInt("method"))
                    {
                        //set class
                        requestObj.put("method",5);
                        requestObj.put("sID",String.format("%08d", jsonObj.getLong("sID")));
                        requestObj.put("op",2);
                        requestObj.put("change",jsonObj.getInt("sClass"));
                    }
                    else if(3==jsonObj.getInt("method"))
                    {
                        //set gender
                        requestObj.put("method",5);
                        requestObj.put("sID",String.format("%08d", jsonObj.getLong("sID")));
                        requestObj.put("op",3);
                        requestObj.put("change",jsonObj.getBoolean("sGender"));
                    }
                    else if(4==jsonObj.getInt("method"))
                    {
                        //set name
                        requestObj.put("method",5);
                        requestObj.put("sID",String.format("%08d", jsonObj.getLong("sID")));
                        requestObj.put("op",1);
                        requestObj.put("change",jsonObj.getString("sName"));
                    }
                    else if(5==jsonObj.getInt("method"))
                    {
                        //get info
                        requestObj.put("method",1);
                        requestObj.put("sID",String.format("%08d", jsonObj.getLong("sID")));
                    }
                }     
                
                response=readerUtil.sendToServer(requestObj.toString());
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
            clientSocket.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}