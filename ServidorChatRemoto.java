import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServidorChatRemoto extends Remote {
    void cadastrarCliente(String usuario, String ip, int porta, ClienteChatRemoto cliente) throws RemoteException;
    void removerCliente(String usuario) throws RemoteException;
    List<String> listarClientes() throws RemoteException;
    void enviarMensagemSala(String usuario, String mensagem) throws RemoteException;
    ClienteChatRemoto obterCliente(String usuario) throws RemoteException;
}
