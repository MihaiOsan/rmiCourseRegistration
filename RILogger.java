import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RILogger extends Remote {
    /**
     * Metoda pentru jurnalizare. LogicNode apelează această metodă pentru a înregistra un mesaj.
     *
     * @param clientId Identificatorul clientului (de exemplu, "Client_1")
     * @param command Comanda executată (de exemplu, "getAllStudents")
     * @param message Mesajul suplimentar asociat cu logarea
     * @throws RemoteException dacă există probleme de comunicare RMI
     */
    void log(String clientId, String command, String message) throws RemoteException;
}
