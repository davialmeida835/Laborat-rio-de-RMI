import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClienteChatRemoto extends Remote {
    void receberMensagem(String remetente, String mensagem) throws RemoteException;
    void receberMensagemSala(String remetente, String mensagem) throws RemoteException;
    void receberArquivo(String remetente, String nomeArquivo, byte[] dados) throws RemoteException;
}
