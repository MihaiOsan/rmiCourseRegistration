/**
 * @(#)Logic.java
 */


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;


/**
 * Clasa reprezinta un nod logic responsabil sa ofere metode de acces
 * de nivel inalt la date, utilizand mecanismul RMI. 
 * Depinde de functionalitatile nodului datelor care este, de asemenea, 
 * disponibil prin mecanism RMI. Actioneaza astfel ca o punte intre 
 * nodurile clienti si nodul datelor.
 */
public class Logic extends UnicastRemoteObject implements RILogic {

    private static final String LOGIC_NAME = "Logic";
    private static final String DATA_NAME  = "Data";

    /**
     * Referinta "remote" la nodul datelor.
     */
    protected RIData rmiDataNode;

    /**
     * Construieste un nod logic. La momentul crearii se obtine
     * o referinta la nodul datelor.
     *
     * @param sDataName numele nodului datelor
     */
    public Logic(String sDataName)
           throws RemoteException, NotBoundException, MalformedURLException {
        // Obtinerea referintei "remote" la nodul datelor.
        this.rmiDataNode = (RIData) Naming.lookup(sDataName);
    }

    /**
     * Lista tuturor studentilor.
     *
     * @return un sir, rezultat al procesarii comenzii
     */
    public String getAllStudents()
                  throws RemoteException {
        // Preluarea informatiilor referitoare la toti studentii.
        ArrayList vStudent = this.rmiDataNode.getAllStudentRecords();

        // Construirea listei cu toti studentii si returnarea ei.
        String sReturn = "";
        for (int i=0; i<vStudent.size(); i++) {
            sReturn += (i == 0 ? "" : "\n") + ((Student) vStudent.get(i)).toString();
        }
        return sReturn;
    }

    /**
     * Lista tuturor cursurilor.
     *
     * @return un sir, rezultat al procesarii comenzii
     */
    public String getAllCourses()
                  throws RemoteException {
        // Preluarea informatiilor referitoare la toate cursurile.
        ArrayList vCourse = this.rmiDataNode.getAllCourseRecords();

        // Construirea listei cu informatii despre toate cursurile si returnarea ei.
        String sReturn = "";
        for (int i=0; i<vCourse.size(); i++) {
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
    public String getRegisteredStudents(String sCID)
                  throws RemoteException {
        // Preluarea listei studentilor inregistrati la cursul precizat prin ID curs.
        Course objCourse = this.rmiDataNode.getCourseRecord(sCID);
        if (objCourse == null) {
            return "ID curs inexistent";
        }
        ArrayList vStudent = objCourse.getRegisteredStudents();

        // Construirea listei studentilor si returnarea ei.
        String sReturn = "";
        for (int i=0; i<vStudent.size(); i++) {
            sReturn += (i == 0 ? "" : "\n") + ((Student) vStudent.get(i)).toString();
        }
        return sReturn;
    }

    /**
     * Lista cursurilor la care este inregistrat un student.
     *
     * @param sSID un sir reprezentand ID-ul studentului
     * @return un sir, rezultat al procesarii comenzii
     */
    public String getRegisteredCourses(String sSID)
                  throws RemoteException {
        // Obtinerea listei cursurilor la care este inregistrat studentul.
        Student objStudent = this.rmiDataNode.getStudentRecord(sSID);
        if (objStudent == null) {
            return "ID student inexistent";
        }
        ArrayList vCourse = objStudent.getRegisteredCourses();

        // Construirea listei cu informatii despre cursuri si returnarea ei.
        String sReturn = "";
        for (int i=0; i<vCourse.size(); i++) {
            sReturn += (i == 0 ? "" : "\n") + ((Course) vCourse.get(i)).toString();
        }
        return sReturn;
    }

    /**
     * Lista cursurilor absolvite de un student.
     *
     * @param sSID un sir reprezentand ID-ul studentului
     * @return un sir, rezultat al procesarii comenzii
     */
    public String getCompletedCourses(String sSID)
                  throws RemoteException {
        // Obtinerea listei cursurilor absolvite de student.
        Student objStudent = this.rmiDataNode.getStudentRecord(sSID);
        if (objStudent == null) {
            return "ID student inexistent";
        }
        ArrayList vCourseID = objStudent.getCompletedCourses();

        // Construirea listei cu informatii despre cursuri si returnarea ei.
        String sReturn = "";
        for (int i=0; i<vCourseID.size(); i++) {
            String sCID = (String) vCourseID.get(i);
            String sName = this.rmiDataNode.getCourseName(sCID);
            sReturn += (i == 0 ? "" : "\n") + sCID + " " + (sName == null ? "Unknown" : sName);
        }
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
    public String makeARegistration(String sSID, String sCID)
                  throws RemoteException {
        // Preluare informatii student si curs.
        Student objStudent = this.rmiDataNode.getStudentRecord(sSID);
        Course objCourse = this.rmiDataNode.getCourseRecord(sCID);
        if (objStudent == null) {
            return "ID student inexistent";
        }
        if (objCourse == null) {
            return "ID curs inexistent";
        }

      // Verificare daca un curs date este in conflict cu oricare dintre cursurile
	  // la care studentul este inregistrat.
        ArrayList vCourse = objStudent.getRegisteredCourses();
        for (int i=0; i<vCourse.size(); i++) {
            if (((Course) vCourse.get(i)).conflicts(objCourse)) {
                return "Conflicte de inregistrare la curs";
            }
        }

        // Cerere validata. Inregistrare student la curs.
        this.rmiDataNode.makeARegistration(sSID, sCID);
        return "Succes!";
    }

    /**
     * Creare nod logic si lansarea lui. 
     */
    public static void main(String args[]) {
        // Verificarea numarului de parametri.
        if (args.length != 0) {
            System.out.println("Numar incorect de parametrii");
            System.out.println("Utilizare: java Logic");
            System.exit(1);
        }

        try {
            // Crearea unui nod logic si publicarea sa prin RMI.
            Logic objLogic = new Logic(DATA_NAME);
            Naming.rebind(LOGIC_NAME, objLogic);
            System.out.println("Nodul logic este gata de servire.");

            // Asteptare intrerupere de la utilizator.
            System.out.println("Apasati Enter pentru terminare.");
            System.in.read();

            // Eliberare resurse si terminare.
            Naming.unbind(LOGIC_NAME);
            System.out.println("Nodul logic se dezactiveaza. Apasati Ctrl-C daca dureaza prea mult.");
        }
        catch (java.rmi.ConnectException e) {
            // Afisare mesaj de eroare si exit.
            System.err.println("Java RMI error: verificati daca rmiregistry este pornit.");
            System.exit(1);
	}
        catch (java.rmi.NotBoundException e) {
            // Afisare mesaj de eroare si exit.
            System.err.println("Java RMI error: verificati daca nodul datelor este pornit.");
            System.exit(1);
        }
        catch (Exception e) {
            // Afisare informatii pentru depanare.
            System.out.println("Unexpected exception at " + LOGIC_NAME);
            e.printStackTrace();
            System.exit(1);
        }
    }
}
