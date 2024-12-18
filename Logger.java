import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Logger reprezinta un nod responsabil cu jurnalizarea interacțiunilor.
 * Primește cererile de la LogicNode și le scrie într-un fișier specificat.
 */
public class Logger extends UnicastRemoteObject implements RILogger{

    private static final String LOGGER_NAME = "Logger";
    private String logFile;

    /**
     * Construieste un nod Logger, care primește mesajele de la LogicNode
     * și le scrie într-un fișier de log.
     *
     * @param logFilePath calea către fișierul de log
     */
    public Logger(String logFilePath) throws RemoteException, NotBoundException, MalformedURLException{
        this.logFile = logFilePath;
    }

    /**
     * Metoda care scrie mesajul în fișierul de log.
     *
     * @param clientId Identificatorul clientului
     * @param command Comanda primită
     * @param message Mesajul pentru logare
     */
    public void log(String clientId, String command, String message) {
        synchronized (Logger.class) {
            try (FileWriter fw = new FileWriter(logFile, true);
                 PrintWriter pw = new PrintWriter(fw)) {
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                pw.println("[" + timestamp + "] Client: " + clientId + ", Command: " + command + ", Message: " + message);
            } catch (IOException e) {
                System.err.println("Eroare la scrierea în fișierul de log: " + e.getMessage());
            }
        }
    }

    /**
     * Main method pentru rularea Logger.
     *
     * @param args Parametrii de intrare
     */
    public static void main(String[] args) {


        String logFilePath = args.length==1 ? args[0] : "log.txt";

        try {
            // Creare nod de date si publicare pentru comunicare prin RMI.
            String ipAddress = NetworkUtils.getNonLoopbackAddress();
            System.out.println("Nodul Logger rulează pe:");
            System.out.println("Adresa IP: " + ipAddress);
            // Creează nodul Logger și publică-l prin RMI
            Logger logger = new Logger(logFilePath);
            Naming.rebind("//"+ ipAddress+"/" + LOGGER_NAME, logger);

            System.out.println("Logger este gata. Scrie logurile în: " + logFilePath);
            System.out.println("Apasati Enter pentru terminare.");

            // Așteaptă pentru terminare
            System.in.read();

            // Eliberare resurse
            Naming.unbind("//"+ ipAddress+"/" + LOGGER_NAME);
            System.out.println("Logger s-a dezactivat.");
        } catch (MalformedURLException e) {
            System.err.println("URL incorect: " + e.getMessage());
        } catch (RemoteException e) {
            System.err.println("Eroare RMI: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Eroare IO: " + e.getMessage());
        } catch (NotBoundException e) {
            System.err.println("Logger nu este conectat corect: " + e.getMessage());
        }
    }
}
