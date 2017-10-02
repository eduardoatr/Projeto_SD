import thrift.*;
import org.apache.thrift.TException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Eduardo Vieira e Sousa
 */

public class OperationHandler implements Operation.Iface{
    
    private Graph g = new Graph();
    ConcurrentSkipListSet<Integer> emUsoVrt = new ConcurrentSkipListSet<Integer>();
    ConcurrentSkipListSet<Integer> emUsoEdg = new ConcurrentSkipListSet<Integer>();
    
    // OK!
    private Boolean checVrt(int nome){ // Verifica já existe um vértice com o nome;
        while(!emUsoVrt.add(nome));          
        // Região crítica
        boolean r = false;
        for(int i = g.getVertSize(); i>0; i--)
            if(g.getVert().get(i-1).getNome() == nome)
                r = true;
        // Região crítica
        emUsoVrt.remove(nome);
           
        return r;
    }
    
    // OK!
    private int getIndexVrt(int nome){ // Retorna o índice do vértice;
        int r = -1;
        for(int i = g.getVertSize(); i>0; i--)
            if(g.getVert().get(i-1).getNome() == nome)
                r = i-1;
      
        return r;
    }
    
    // OK!
    private Boolean checEdg(int nome){ // Verifica já existe um aresta com o nome;
        while(!emUsoEdg.add(nome));
        // Região crítica
        boolean r = false;
        for(int i = g.getArestSize(); i>0; i--)
            if((g.getArest().get(i-1).getNome() == nome))
                r = true;
        // Região crítica
        emUsoEdg.remove(nome);
        
        return r;
    }
    
    // OK!
    private int getIndexEdg(int nome){ // Retorna o índice da aresta;
        int r = -1;
        for(int i = g.getArestSize(); i>0; i--)
            if((g.getArest().get(i-1).getNome() == nome))
                r = i-1;
        
        return r;
    }
    
    @Override // OK!
    public void addVrt(int nome, int cor, double peso, String desc) throws InvalidOperation, TException{               
        if(checVrt(nome)){            
            InvalidOperation io = new InvalidOperation();
            LocalDateTime now = LocalDateTime.now();
            io.hora =  Integer.toString(now.getHour()) +":"+ Integer.toString(now.getMinute());
            io.msg = "O grafo já possui um vértice com o nome: "+nome+".";
            throw io;
        }else{          
            while(!emUsoVrt.add(nome));          
            // Região crítica
            System.out.println("addVrt("+nome+ ", "+cor+", "+peso+", "+desc+");(TreadID:"+Thread.currentThread().getId()+")");
            Vertex novo = new Vertex(nome,cor,peso,desc,null);
            g.addToVert(novo);   
            // Região crítica
            emUsoVrt.remove(nome);     
        }
    }

    @Override // ARRUMAR DELEÇÂO DE ARESTAS (testar)
    public void delVrt(int nome) throws InvalidOperation, TException{                              
        if(checVrt(nome)){    
            while(!emUsoVrt.add(nome));
            // Região crítica
            System.out.println("delVrt("+nome+");");
            for(int i = g.getVert().get(getIndexVrt(nome)).getAdjaSize(); i > 0 ; i--)
                this.delEdg(g.getVert().get(getIndexVrt(nome)).getAdja().get(i-1).getNome());
            g.getVert().remove(getIndexVrt(nome));    
            // Região crítica
            emUsoVrt.remove(nome);
        }else{           
            InvalidOperation io = new InvalidOperation();
            LocalDateTime now = LocalDateTime.now();
            io.hora =  Integer.toString(now.getHour()) +":"+ Integer.toString(now.getMinute());
            io.msg = "O grafo não possui um vértice com o nome: "+nome+".";
            throw io;
        }                   
    }

    @Override // OK!
    public void updtVrt(int nome, int cor, double peso, String desc) throws InvalidOperation, TException{           
        if(checVrt(nome)){
            while(!emUsoVrt.add(nome));                
            // Região crítica
            System.out.println("updtVrt("+nome+ ", "+cor+", "+peso+", "+desc+");(TreadID:"+Thread.currentThread().getId()+")");
            g.getVert().get(getIndexVrt(nome)).setCor(cor);
            g.getVert().get(getIndexVrt(nome)).setPeso(peso);
            g.getVert().get(getIndexVrt(nome)).setDesc(desc);
            // Região crítica
            emUsoVrt.remove(nome);
        }else{
            InvalidOperation io = new InvalidOperation();
            LocalDateTime now = LocalDateTime.now();
            io.hora =  Integer.toString(now.getHour()) +":"+ Integer.toString(now.getMinute());
            io.msg = "O grafo não possui um vértice com o nome: "+nome+".";
            throw io;
        }                        
    }

    @Override // OK!
    public Vertex readVrt(int nome) throws InvalidOperation, TException{     
        if(checVrt(nome)){       
            while(!emUsoVrt.add(nome));             
            // Região crítica
            System.out.println("readVrt("+nome+");(TreadID:"+Thread.currentThread().getId()+")");
            Vertex novo = g.getVert().get(getIndexVrt(nome));
            // Região crítica
            emUsoVrt.remove(nome);
            
            return novo;   
        }else{
            InvalidOperation io = new InvalidOperation();
            LocalDateTime now = LocalDateTime.now();
            io.hora =  Integer.toString(now.getHour()) +":"+ Integer.toString(now.getMinute());
            io.msg = "O grafo não possui um vértice com o nome: "+nome+".";
            throw io;
        }     
    }

    @Override // OK!
    public void addEdg(int nome, int v1, int v2, double peso, String desc, boolean dir) throws InvalidOperation, TException{
        if(checEdg(nome)){             
            InvalidOperation io = new InvalidOperation();
            LocalDateTime now = LocalDateTime.now();
            io.hora = Integer.toString(now.getHour()) +":"+ Integer.toString(now.getMinute());
            io.msg = "O grafo já possui uma aresta com o nome: "+nome+".";
            throw io;
        }else{
            if(!(checVrt(v1) && checVrt(v2))){             
            InvalidOperation io = new InvalidOperation();
            LocalDateTime now = LocalDateTime.now();
            io.hora = Integer.toString(now.getHour()) +":"+ Integer.toString(now.getMinute());
            io.msg = "O grafo não possui vértice com com o nome: "+nome+".";
            throw io;
            }else{      
                while(!emUsoEdg.add(nome));
                while(!emUsoVrt.add(v1));
                while(!emUsoVrt.add(v2));
                // Região crítica
                System.out.println("addEdg("+nome+", "+v1+ ", "+v2+", "+peso+", "+desc+", "+dir+");(TreadID:"+Thread.currentThread().getId()+")");
                Edge novo = new Edge(nome, v1, v2, peso, desc, dir);
                g.addToArest(novo);
                g.getVert().get(getIndexVrt(v1)).addToAdja(novo);
                g.getVert().get(getIndexVrt(v2)).addToAdja(novo);
                // Região crítica
                emUsoEdg.remove(nome);
                emUsoVrt.remove(v1);
                emUsoVrt.remove(v2);
            }
        }
    }

    @Override // ARRUMAR LISTA DE ADJACENCIA DOS VERTICES
    public void delEdg(int nome) throws InvalidOperation, TException{
        if(checEdg(nome)){
            
            int v1 = g.getArest().get(getIndexEdg(nome)).getV1();
            int v2 = g.getArest().get(getIndexEdg(nome)).getV2();
        
            while(!emUsoEdg.add(nome));  
            while(!emUsoVrt.add(v1));
            while(!emUsoVrt.add(v2));
                    
            // Região crítica
            System.out.println("delEdg("+nome+");");                  
            g.getVert().get(getIndexVrt(v1)).getAdja().remove(g.getArest().get(getIndexEdg(nome)));
            g.getVert().get(getIndexVrt(v2)).getAdja().remove(g.getArest().get(getIndexEdg(nome)));  
            g.getArest().remove(getIndexEdg(nome));           
            // Região crítica
            emUsoEdg.remove(nome);
            emUsoVrt.remove(v1);
            emUsoVrt.remove(v2);
        }else{
            InvalidOperation io = new InvalidOperation();
            LocalDateTime now = LocalDateTime.now();
            io.hora =  Integer.toString(now.getHour()) +":"+ Integer.toString(now.getMinute());
            io.msg = "O grafo não possui uma aresta com o nome: "+nome+".";
            throw io;
        }
    }

    @Override // OK!
    public void updtEdg(int nome, int v1, int v2, double peso, String desc, boolean dir) throws InvalidOperation, TException{
        if(checEdg(nome)){            
            while(!emUsoEdg.add(nome));
            while(!emUsoVrt.add(v1));
            while(!emUsoVrt.add(v2));
            // Região crítica
            System.out.println("updtEdg("+nome+", "+v1+ ", "+v2+", "+peso+", "+desc+", "+dir+");(TreadID:"+Thread.currentThread().getId()+")");
            g.getArest().get(getIndexEdg(nome)).setPeso(peso);
            g.getArest().get(getIndexEdg(nome)).setDesc(desc);
            g.getArest().get(getIndexEdg(nome)).setFlag(dir); 
            // Região crítica
            emUsoEdg.remove(nome);
            emUsoVrt.remove(v1);
            emUsoVrt.remove(v2);
        }else{
            InvalidOperation io = new InvalidOperation();
            LocalDateTime now = LocalDateTime.now();
            io.hora =  Integer.toString(now.getHour()) +":"+ Integer.toString(now.getMinute());
            io.msg = "O grafo não possui uma aresta com o nome: "+nome+".";
            throw io;
        }
    }

    @Override // OK!
    public Edge readEdg(int nome) throws InvalidOperation, TException{
        if(checEdg(nome)){ 
            
            int v1 = g.getArest().get(getIndexEdg(nome)).getV1();
            int v2 = g.getArest().get(getIndexEdg(nome)).getV2();
            
            while(!emUsoEdg.add(nome));
            while(!emUsoVrt.add(v1));
            while(!emUsoVrt.add(v2));
            // Região crítica
            System.out.println("readEdg("+nome+");");
            Edge novo = g.getArest().get(getIndexEdg(nome)); 
            // Região crítica
            emUsoEdg.remove(nome);
            emUsoVrt.remove(v1);
            emUsoVrt.remove(v2);
            
            return novo;       
        }else{
            InvalidOperation io = new InvalidOperation();
            LocalDateTime now = LocalDateTime.now();
            io.hora =  Integer.toString(now.getHour()) +":"+ Integer.toString(now.getMinute());
            io.msg = "O grafo não possui uma aresta com o nome: "+nome+".";
            throw io;
        }
    }

    @Override // OK!
    public List<Vertex> listAllVrts() throws TException{
        while(!emUsoVrt.isEmpty());
        // Região crítica
        System.out.println("listAllVrts();(TreadID:"+Thread.currentThread().getId()+")");
        List<Vertex> novo = g.getVert();
        // Região crítica
        emUsoVrt.clear();  
        
        return novo;      
    }

    @Override // OK!
    public List<Edge> listAllEdgs() throws TException{
        while(!emUsoVrt.isEmpty());
        while(!emUsoEdg.isEmpty());              
        // Região crítica
        System.out.println("listAllEdgs();(TreadID:"+Thread.currentThread().getId()+")");
        List<Edge> novo = g.getArest();
        // Região crítica
        emUsoVrt.clear();
        emUsoEdg.clear();
 
        return novo;      
    }

    @Override // OK!
    public List<Edge> listVrtEdgs(int v) throws InvalidOperation, TException{
        while(!emUsoVrt.isEmpty());
        while(!emUsoEdg.isEmpty());
        // Região crítica
        System.out.println("listVrtEdgs("+v+");(TreadID:"+Thread.currentThread().getId()+")");
        List<Edge> novo = g.getVert().get(getIndexVrt(v)).getAdja();
        // Região crítica
        emUsoVrt.clear();
        emUsoEdg.clear();
         
        return novo; 
    }

    @Override // OK!
    public List<Vertex> listVrtNgbs(int v) throws InvalidOperation, TException{   
        while(!emUsoVrt.isEmpty());
        while(!emUsoEdg.isEmpty());
        // Região crítica
        System.out.println("listVrtNgbs("+v+");(TreadID:"+Thread.currentThread().getId()+")");
        List<Vertex> novo = new ArrayList<>();      
        for(int i = 0; i < g.getVert().get(getIndexVrt(v)).getAdja().size(); i++){
            if((g.getVert().get(getIndexVrt(v)).getAdja().get(i).getV1()) != v){
                novo.add(g.getVert().get(getIndexVrt((g.getVert().get(getIndexVrt(v)).getAdja().get(i).getV1()))));
            }else{
                novo.add(g.getVert().get(getIndexVrt((g.getVert().get(getIndexVrt(v)).getAdja().get(i).getV2()))));
            }
        }
        // Região crítica
        emUsoVrt.clear();
        emUsoEdg.clear();
          
        return  novo;
    }         

    @Override
    public void testServWait() throws TException{
        synchronized(this){ 
            try{
                this.wait();
            }catch(InterruptedException ex){
                Logger.getLogger(OperationHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void testServAddV(int v) throws TException{
        this.emUsoVrt.add(v);
    }

    @Override
    public void testServRmvV(int v) throws TException{
        this.emUsoVrt.remove(v);
    }

    @Override
    public void testServClearV() throws TException{
        this.emUsoVrt.clear();
    }

    @Override
    public void testServAddE(int v) throws TException{
        this.emUsoEdg.add(v);
    }

    @Override
    public void testServRmvE(int v) throws TException{
        this.emUsoEdg.remove(v);
    }

    @Override
    public void testServClearE() throws TException{
        this.emUsoEdg.clear();
    }
}

