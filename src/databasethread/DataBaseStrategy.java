package databasethread;

import databaseoperations.DBOperationsInterface;

/**
 * 数据库接口
 * @author zhaowenbo
 *
 */

public interface DataBaseStrategy {	//策略接口, 便于拓展功能
	public void service(java.net.Socket socket, DBOperationsInterface dbo); 
}
