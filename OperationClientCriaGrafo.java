import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import thrift.*;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

/**
 *
 * @author Eduardo Vieira e Sousa
 */

public class OperationClientCriaGrafo{
    public static void main(String [] args){
        try{
            String arg1 = args[0]; // "localhost"
            int arg2 = Integer.parseInt(args[1]); // 9090

            TTransport transport = new TSocket(arg1,arg2);
            TProtocol protocol = new  TBinaryProtocol(transport);
            Operation.Client client = new Operation.Client(protocol);
            transport.open();

            //=========================================================
            // Geração aleatória do grafo
            //=========================================================

            Random r = new Random();

            // Add 50 vértices aleatórios
            for(int i = 0; i <50; i++){
                try{
                    client.addVrt(r.nextInt(150), r.nextInt(50), r.nextDouble(),"Vert"+i);
                }catch(InvalidOperation io){
                    System.out.println("("+io.getHora()+")"+io.getMsg());
                }
            }
            
            // Add 100 arestas aleatórias
            for(int i = 0; i <100; i++){
                try{
                    client.addEdg(r.nextInt(300), r.nextInt(100), r.nextInt(100), r.nextDouble(),"Vert"+i, r.nextBoolean());
                }catch(InvalidOperation io){
                    System.out.println("("+io.getHora()+")"+io.getMsg());
                }
            }

            transport.close(); 
        }catch(TException x){
            x.printStackTrace();
        }
    }
}
