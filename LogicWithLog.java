import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class LogicWithLog extends Logic implements RILogic {
    private static final String LOGIC_NAME = "Logic";
    private static final String DATA_NAME  = "Data";
    protected RILogger rmiLoggerNode;

    public LogicWithLog(String sDataName, String loggerUrl) throws RemoteException, NotBoundException, MalformedURLException {
        super(sDataName);
        this.rmiLoggerNode = (RILogger) Naming.lookup(loggerUrl);
    }


    private void logInteraction(String clientId, String command, String message) {
        try {
            rmiLoggerNode.log(clientId, command, message);
        } catch (Exception e) {
            System.err.println("Eroare la conectarea cu Logger: " + e.getMessage());
        }
    }


    /**
     * Lista tuturor studentilor.
     *
     * @return un sir, rezultat al procesarii comenzii
     */
    @Override
    public String getAllStudents()
            throws RemoteException {

        String clientId = "Client_" + Thread.currentThread().getId(); // Identificare client simplă
        logInteraction(clientId, "getAllStudents", "Clientul a cerut lista studenților.\n");


        // Preluarea informatiilor referitoare la toti studentii.
        ArrayList vStudent = this.rmiDataNode.getAllStudentRecords();

        // Construirea listei cu toti studentii si returnarea ei.
        String sReturn = "";
        for (int i = 0; i < vStudent.size(); i++) {
            sReturn += (i == 0 ? "" : "\n") + ((Student) vStudent.get(i)).toString();
        }
        return sReturn;
    }

    /**
     * Lista tuturor cursurilor.
     *
     * @return un sir, rezultat al procesarii comenzii
     */
    @Override
    public String getAllCourses()
            throws RemoteException {
        String clientId = "Client_" + Thread.currentThread().getId(); // Identificare client simplă
        logInteraction(clientId, "getAllCourses", "Clientul a cerut lista cursurilor.\n");

        // Preluarea informatiilor referitoare la toate cursurile.
        ArrayList vCourse = this.rmiDataNode.getAllCourseRecords();

        // Construirea listei cu informatii despre toate cursurile si returnarea ei.
        String sReturn = "";
        for (int i = 0; i < vCourse.size(); i++) {
            sReturn += (i == 0 ? "" : "\n") + ((Course) vCourse.get(i)).toString();
        }
        return sReturn;
    }

    /**
     * Lista studentilor inregistrati la un curs.
     *
     * @param sCID un sir ce reprezinta ID curs
     * @return un sir, rezultat al procesarii comenzii
     */
    @Override
    public String getRegisteredStudents(String sCID)
            throws RemoteException {

        String clientId = "Client_" + Thread.currentThread().getId(); // Identificare client simplă
        logInteraction(clientId, "getRegisteredStudents", "Clientul a cerut lista studentilor inregistrati la curs.");
        // Preluarea listei studentilor inregistrati la cursul precizat prin ID curs.
        Course objCourse = this.rmiDataNode.getCourseRecord(sCID);
        if (objCourse == null) {
            logInteraction(clientId, "getRegisteredStudents", "Eroare: ID curs inexistent.\n");
            return "ID curs inexistent";
        }
        ArrayList vStudent = objCourse.getRegisteredStudents();

        // Construirea listei studentilor si returnarea ei.
        String sReturn = "";
        for (int i = 0; i < vStudent.size(); i++) {
            sReturn += (i == 0 ? "" : "\n") + ((Student) vStudent.get(i)).toString();
        }
        logInteraction(clientId, "getRegisteredStudents", "Succes: Lista sau mesaj returnat.\n");
        return vStudent.size() == 0 ? "niciun student inregistrat la acest curs" : sReturn;
    }

    /**
     * Lista cursurilor la care este inregistrat un student.
     *
     * @param sSID un sir reprezentand ID-ul studentului
     * @return un sir, rezultat al procesarii comenzii
     */
    @Override
    public String getRegisteredCourses(String sSID)
            throws RemoteException {
        String clientId = "Client_" + Thread.currentThread().getId(); // Identificare client simplă
        logInteraction(clientId, "getRegisteredCourses", "Clientul a cerut lista cursurilor la care un stundent este inregistrat.");
        // Obtinerea listei cursurilor la care este inregistrat studentul.
        Student objStudent = this.rmiDataNode.getStudentRecord(sSID);
        if (objStudent == null) {
            logInteraction(clientId, "getRegisteredCourses", "Eroare: ID student inexistent.\n");
            return "ID student inexistent";
        }
        ArrayList vCourse = objStudent.getRegisteredCourses();

        // Construirea listei cu informatii despre cursuri si returnarea ei.
        String sReturn = "";
        for (int i = 0; i < vCourse.size(); i++) {
            sReturn += (i == 0 ? "" : "\n") + ((Course) vCourse.get(i)).toString();
        }
        logInteraction(clientId, "getRegisteredCourses", "Succes: Lista sau mesaj returnat.\n");
        return sReturn;
    }

    /**
     * Lista cursurilor absolvite de un student.
     *
     * @param sSID un sir reprezentand ID-ul studentului
     * @return un sir, rezultat al procesarii comenzii
     */
    @Override
    public String getCompletedCourses(String sSID)
            throws RemoteException {
        String clientId = "Client_" + Thread.currentThread().getId(); // Identificare client simplă
        logInteraction(clientId, "getCompletedCourses", "Clientul a cerut lista cursurilor finalizate de un stundent.");

        // Obtinerea listei cursurilor absolvite de student.
        Student objStudent = this.rmiDataNode.getStudentRecord(sSID);
        if (objStudent == null) {
            logInteraction(clientId, "getCompletedCourses", "Eroare: ID student inexistent.\n");
            return "ID student inexistent";
        }
        ArrayList vCourseID = objStudent.getCompletedCourses();

        // Construirea listei cu informatii despre cursuri si returnarea ei.
        String sReturn = "";
        for (int i = 0; i < vCourseID.size(); i++) {
            String sCID = (String) vCourseID.get(i);
            String sName = this.rmiDataNode.getCourseName(sCID);
            sReturn += (i == 0 ? "" : "\n") + sCID + " " + (sName == null ? "Unknown" : sName);
        }
        logInteraction(clientId, "getCompletedCourses", "Succes: Lista sau mesaj returnat.\n");
        return sReturn;
    }

    /**
     * Inregistrare student la un curs.
     * Conflictele se verifica inainte de realizarea inregistrarii.
     *
     * @param sSID un sir reprezentand ID student
     * @param sCID un sir reprezentand ID curs
     * @return un sir, rezultat al procesarii comenzii
     */
    @Override
    public String makeARegistration(String sSID, String sCID)
            throws RemoteException {
        // Preluare informatii student si curs.
        String clientId = "Client_" + Thread.currentThread().getId(); // Identificare client simplă
        logInteraction(clientId, "makeARegistration", "Clientul a solicitat inregistrarea unui student la curs.");
        Student objStudent = this.rmiDataNode.getStudentRecord(sSID);
        Course objCourse = this.rmiDataNode.getCourseRecord(sCID);
        if (objStudent == null) {
            logInteraction(clientId, "makeARegistration", "Eroare: ID student inexistent.\n");
            return "ID student inexistent";
        }
        if (objCourse == null) {
            logInteraction(clientId, "makeARegistration", "Eroare: ID curs inexistent.\n");
            return "ID curs inexistent";
        }

        // Verificare daca un curs date este in conflict cu oricare dintre cursurile
        // la care studentul este inregistrat.
        ArrayList vCourse = objStudent.getRegisteredCourses();
        for (int i = 0; i < vCourse.size(); i++) {
            if (((Course) vCourse.get(i)).conflicts(objCourse)) {
                logInteraction(clientId, "makeARegistration", "Eroare: Conflicte de inregistrare la curs.\n");
                return "Conflicte de inregistrare la curs";
            }
        }

        // Cerere validata. Inregistrare student la curs.
        this.rmiDataNode.makeARegistration(sSID, sCID);
        logInteraction(clientId, "makeARegistration", "Succes.\n");
        return "Succes!";
    }

    /**
     * Creare nod logic si lansarea lui.
     */
    public static void main(String args[]) {
        // Verificarea numarului de parametri.
        if (args.length < 1) {
            System.out.println("Numar incorect de parametrii");
            System.err.println("Utilizare: java Logic <Adresa_IP_Data> <Adresa_IP_Logger - optional>");
            System.exit(1);
        }

        try {

            String dataIp = args[0];
            String loggerIP = args.length == 2 ? args[1] : "null";
            String dataUrl = "//" + dataIp + "/Data";
            String loggerUrl = "//" + loggerIP + "/Logger";

            InetAddress localHost = InetAddress.getLocalHost();
            String ipAddress = NetworkUtils.getNonLoopbackAddress();

            // Afișează informații despre mașina locală
            System.out.println("Nodul Logic rulează la:");
            System.out.println("Adresa IP: " + ipAddress);
            System.out.println("Nodul Logic va comunica cu Data la adresa: " + dataUrl);

            if (!loggerIP.equals("null")) {
                System.out.println("Nodul Logic va comunica cu Logger la adresa: " + loggerUrl);
            }


            // Crearea unui nod logic si publicarea sa prin RMI.
            Logic objLogic = new LogicWithLog(dataUrl, loggerUrl);
            String logicUrl = "//" + ipAddress + "/" + LOGIC_NAME;
            Naming.rebind(logicUrl, objLogic);

            System.out.println("Nodul logic este gata de servire.");

            System.out.println("Adresa la care clientul trebuie să se conecteze: " + logicUrl);

            // Asteptare intrerupere de la utilizator.
            System.out.println("Apasati Enter pentru terminare.");

            System.in.read();

            // Eliberare resurse si terminare.
            Naming.unbind("//" + ipAddress + "/" + LOGIC_NAME);
            System.out.println("Nodul logic se dezactiveaza. Apasati Ctrl-C daca dureaza prea mult.");
        } catch (java.rmi.ConnectException e) {
            // Afisare mesaj de eroare si exit.
            System.err.println("Java RMI error: verificati daca rmiregistry este pornit.");
            System.exit(1);
        } catch (java.rmi.NotBoundException e) {
            // Afisare mesaj de eroare si exit.
            System.err.println("Java RMI error: verificati daca nodul datelor este pornit.");
            System.exit(1);
        } catch (Exception e) {
            // Afisare informatii pentru depanare.
            System.out.println("Unexpected exception at " + LOGIC_NAME);
            e.printStackTrace();
            System.exit(1);
        }
    }

}
