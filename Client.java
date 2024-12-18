 // @(#)Client.java
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


/**
 * Clasa reprezinta un nod client, de pe treapta client, care este responsabil 
 * cu preluarea intrarilor de la utilizator, executarea metodelor corespunzatoare 
 * din nodul logic si afisarea mesajelor returnate de nodul logic. 
 * Mesajele returnate sunt obiecte de tip String.
 */
public class Client {

    private static final String CLIENT_NAME = "Client";
    private static final String LOGIC_NAME  = "Logic";

    /**
	     * Referinta "remote" la nodul logic.
     */
    protected RILogic rmiLogicNode;

    /**
     * Construieste un client. In momentul crearii clientului se obtine o
     * referinta "remote" la nodul logic. 
     * @param sLogicName numele nodului logic
     */
    public Client(String sLogicName)
           throws NotBoundException, MalformedURLException, RemoteException {
        // Obtinerea referintei remote la nodul logic.
        this.rmiLogicNode = (RILogic) Naming.lookup(sLogicName);
    }

    /**
     * Clientul preia continuu intrari de la utilizator si executa
     * metodele RMI asociate din nodul logic.
     */
    public void execute()
                throws RemoteException, IOException {
        // Crearea unui "buffered reader" utilizand fluxul de intrare al sistemului.
        BufferedReader objReader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            // Afisarea comenzilor disponibile si preluarea selectiei facuta de utilizator.
            System.out.println("\nAssignment 2 - Sistem inregistrare studenti\n");
            System.out.println("1) Lista studenti");
            System.out.println("2) Lista cursuri");
            System.out.println("3) Lista studenti inregistrati la un curs");
            System.out.println("4) Lista cursurilor la care e inregistrat un student");
            System.out.println("5) Lista cursurilor absolvite de un student");
            System.out.println("6) Inregistrare student la un curs");
            System.out.println("x) Exit");
            System.out.println("\nAlegeti si apasati return >> ");
            String sChoice = objReader.readLine().trim();

            // Executia comenzii 1: Lista studenti.
            if (sChoice.equals("1")) {
            // Executa metoda RMI pentru a obtine informatii despre student 
		    // si afiseaza informatiile obtinute.
                System.out.println("\n" + this.rmiLogicNode.getAllStudents());
                continue;
            }

            // Executia comenzii 2: Lista cursuri.
            if (sChoice.equals("2")) {
            // Executa metoda RMI pentru a obtine informatii despre cursuri 
		    // si afiseaza informatiile obtinute.
                System.out.println("\n" + this.rmiLogicNode.getAllCourses());
                continue;
            }

            // Executia comenzii 3: Lista studenti inregistrati la un curs.
            if (sChoice.equals("3")) {
            // Preluare ID curs de la utilizator.
                System.out.println("\nIndicati ID curs si apasati return >> ");
                String sCID = objReader.readLine().trim();

            // Executa metoda RMI pentru a obtine informatii despre studentii inregistrati 
		    // la cursul indicat si afiseaza informatiile obtinute.
                System.out.println("\n" + this.rmiLogicNode.getRegisteredStudents(sCID));
                continue;
            }

            // Executia comenzii 4: Lista cursurilor la care e inregistrat un student.
            if (sChoice.equals("4")) {
                // Preluare ID student de la utilizator.
                System.out.println("\nIndicati ID student si apasati return >> ");
                String sSID = objReader.readLine().trim();

            // Executa metoda RMI pentru a obtine informatii despre cursurile la care 
		    // e inregistrat studentul indicat si afiseaza informatiile obtinute.
                System.out.println("\n" + this.rmiLogicNode.getRegisteredCourses(sSID));
                continue;
            }

            // Executia comenzii 5: Lista cursurilor absolvite de un student.
            if (sChoice.equals("5")) {
                // Preluare ID student de la utilizator.
                System.out.println("\nIndicati ID student si apasati return >> ");
                String sSID = objReader.readLine().trim();

            // Executa metoda RMI pentru a obtine informatii despre cursurile absolvite 
		    // de studentul indicat si afiseaza informatiile obtinute.
                System.out.println("\n" + this.rmiLogicNode.getCompletedCourses(sSID));
                continue;
            }

            // Executia comenzii 6: Inregistrare student la un curs.
            if (sChoice.equals("6")) {
                // Preluare ID student si ID curs de la utilizator.
                System.out.println("\nIndicati ID student si apasati return >> ");
                String sSID = objReader.readLine().trim();
                System.out.println("\nIndicati ID curs si apasati return >> ");
                String sCID = objReader.readLine().trim();

            // Executa metoda RMI pentru a face o noua inregistrare  
		    // si afiseaza informatiile returnate.
                System.out.println("\n" + this.rmiLogicNode.makeARegistration(sSID, sCID));
                continue;
            }

            // Terminarea acestui client.
            if (sChoice.equalsIgnoreCase("X")) {
                break;
            }
        }

        // Eliberarea resurselor.
        objReader.close();
    }

    /**
     * Creaza un client si il lanseaza. 
     *
     * @param args array cu parametrii de intrare
     */
    public static void main(String args[]) {
        // Verificare numar de parametri.
        if (args.length != 0) {
            System.out.println("Numar de parametrii incorect");
            System.out.println("Utilizare: java Client");
            System.exit(1);
        }

        try {
            // Crearea si executarea unui client.
            Client objClient = new Client(LOGIC_NAME);
            objClient.execute();
        }
        catch (java.rmi.ConnectException e) {
            // Afisare mesaj de eroare si exit.
            System.err.println("Java RMI error: Verificati daca rmiregistry este pornit.");
            System.exit(1);
	}
        catch (java.rmi.NotBoundException e) {
            // Afisare mesaj de eroare si exit.
            System.err.println("Java RMI error: verificat daca nodul logic este pornit.");
            System.exit(1);
        }

        catch (Exception e) {
            // Afisare informatii pentru depanare.
            System.out.println("Unexpected exception at " + CLIENT_NAME);
            e.printStackTrace();
            System.exit(1);
        }
    }
}
