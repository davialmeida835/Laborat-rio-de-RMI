import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.Naming;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.ArrayList;

public class Servidor extends UnicastRemoteObject implements ServidorChatRemoto {

    private final Map<String, ClienteChatRemoto> clientes = new ConcurrentHashMap<>();

    protected Servidor() throws RemoteException {
        super();
    }

    @Override
    public synchronized void cadastrarCliente(String usuario, String ip, int porta, ClienteChatRemoto cliente) throws RemoteException {
        if (clientes.containsKey(usuario)) {
            System.out.println("Cliente já cadastrado: " + usuario);
        } else {
            clientes.put(usuario, cliente);
            System.out.println("Cliente cadastrado: " + usuario);
        }
    }

    @Override
    public synchronized void removerCliente(String usuario) throws RemoteException {
        clientes.remove(usuario);
        System.out.println("Cliente removido: " + usuario);
    }

    @Override
    public synchronized List<String> listarClientes() throws RemoteException {
        return new ArrayList<>(clientes.keySet());
    }

    @Override
    public ClienteChatRemoto obterCliente(String usuario) throws RemoteException {
        return clientes.get(usuario);
    }

    @Override
    public void enviarMensagemSala(String usuario, String mensagem) throws RemoteException {
        System.out.println("Mensagem da sala por " + usuario + ": " + mensagem);
        for (Map.Entry<String, ClienteChatRemoto> entry : clientes.entrySet()) {
            try {
                entry.getValue().receberMensagemSala(usuario, mensagem);
            } catch (RemoteException e) {
                System.err.println("Erro enviando mensagem para " + entry.getKey());
            }
        }
    }

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            Servidor servidor = new Servidor();
            Naming.rebind("Servidor", servidor);
            System.out.println("Servidor de chat pronto!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
