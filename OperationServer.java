import thrift.Operation;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;

/**
 *
 * @author Eduardo Vieira e Sousa
 */

public class OperationServer{
  
    public static OperationHandler handler;
    public static Operation.Processor processor;

    public static void main(String [] args) {
        try{
            handler = new OperationHandler();
            processor = new Operation.Processor(handler);           
            int arg1 = Integer.parseInt(args[0]); // 9090
            
            Runnable serverThread = new Runnable(){
		public void run() {
			poolServer(processor, arg1);
		}
            };  
            
            new Thread(serverThread).start();
                             
        }catch(Exception x){
             x.printStackTrace();
        }
    }
    
    public static void poolServer(Operation.Processor processor, int arg1) {
        try{
            TServerTransport serverTransport = new TServerSocket(arg1);         
            TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
            
            System.out.println("Starting the multithread server...");
            server.serve();
            
        }catch(Exception e) {
          e.printStackTrace();
        }
    }    
}
