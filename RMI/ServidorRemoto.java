import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

public interface ServidorRemoto extends Remote {

    void escreveMsg(String msg) throws RemoteException;

    Date dataDeHoje() throws RemoteException;
}
