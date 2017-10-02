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

public class OperationClient2{
    public static void main(String [] args){
        try{
            String arg1 = args[0]; // "localhost"
            int arg2 = Integer.parseInt(args[1]); // 9090


            TTransport transport = new TSocket(arg1,arg2);
            TProtocol protocol = new  TBinaryProtocol(transport);
            Operation.Client client = new Operation.Client(protocol);
            transport.open();

            //=========================================================
            // Teste: Teste de concorrência
            // ( Reiniciar o servidor e usar os serviços de teste )
            // ( Iniciar os dois clientes simultaneamente )
            //=========================================================
            
            //client.testServWait();
            //client.testServAddV(12);
            //client.testServRmvV(1);
            //client.testServClearV();
            
            client.addVrt(156, 1, 1.1, "edg1");
             
            transport.close(); 
        }catch(TException x){
            x.printStackTrace();
        }
    }
}
