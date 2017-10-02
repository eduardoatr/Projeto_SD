import java.util.List;
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

public class OperationClient1{
    public static void main(String [] args){
    try{
        String arg1 = args[0]; // "localhost"
        System.out.println(arg1);
        int arg2 = Integer.parseInt(args[1]); // 9090
 
        TTransport transport = new TSocket(arg1,arg2);
        transport.open();

        TProtocol protocol = new  TBinaryProtocol(transport);
        Operation.Client client = new Operation.Client(protocol);
        
        //=========================================================
        // Teste: CRUD vértice
        //=========================================================
        
        List<Vertex> lv;  
        Vertex v;
        
        // Criar vértice
        client.addVrt(1, 1, 1.1, "vrt1");
        client.addVrt(2, 2, 2.2, "vrt2");
        client.addVrt(3, 3, 3.3, "vrt3");
        client.addVrt(4, 4, 4.4, "vrt4");
        
        // Listar vértices
        lv = client.listAllVrts();
        for(int i = 0; i < lv.size(); i++)
            System.out.println(lv.get(i).toString());
        
        // Deletar vértice
        client.delVrt(4);
          
        // Verificar os resultados
        lv = client.listAllVrts();
        for(int i = 0; i < lv.size(); i++)
            System.out.println(lv.get(i).toString());
        
        // Atualizar vértice
        client.updtVrt(3, 5, 5.5, "vrt5");
        
        // Ler vértice
        v = client.readVrt(3);    
        System.out.println(v.toString());
              
        //=========================================================
        // Teste: CRUD arestas
        //=========================================================
        
        List<Edge> le;
        Edge e;
        
        // Criar arestas
        client.addEdg(1, 1, 2, 1.1, "edg1", true);
        client.addEdg(2, 2, 3, 2.2, "edg2", true);
        client.addEdg(3, 1, 3, 3.3, "edg3", true);
        client.addEdg(4, 3, 1, 4.4, "edg4", true);
        
        // Listar arestas
        le = client.listAllEdgs();
        for(int i = 0; i < le.size(); i++)
            System.out.println(le.get(i).toString());
        
        // Deletar arestas
        client.delEdg(4);
        
        // Verificar os resultados
        le = client.listAllEdgs();
        for(int i = 0; i < le.size(); i++)
            System.out.println(le.get(i).toString());
        
        // Atualizar aresta
        client.updtEdg(3, 1, 3, 5.5, "edg5", true);
        
        // Ler aresta        
        e = client.readEdg(3);    
        System.out.println(e.toString());
        
        //=========================================================
        // Teste: Outros serviços
        //=========================================================
        
        // Listar vizinhos
        lv = client.listVrtNgbs(1);
        for(int i = 0; i < lv.size(); i++)
            System.out.println(lv.get(i).toString());
        
        // Listar arestas adjacentes
        le = client.listVrtEdgs(1);
        for(int i = 0; i < le.size(); i++)
            System.out.println(le.get(i).toString());
              
        transport.close();
    }catch(TException x){
        x.printStackTrace();
    }
  }
}
