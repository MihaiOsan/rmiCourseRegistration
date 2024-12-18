/**
 * @(#)Data.java
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;


/**
 * Un nod de date ofera acces la date despre studenti si cursuri, 
 * inclusiv citirea informatiilor din inregistrarile corespunzatoare din fisiere 
 * si scrierea acestora. Aceasta versiune nu suporta insa scrierea in fisiere 
 * a informatiilor corespunzatoare inregistrarii studentilor la cursuri; 
 * la oprirea aplicatiei aceste informatii se pierd.
 */
public class Data extends UnicastRemoteObject implements RIData {

    private static final String DATA_NAME = "Data";

    /**
     * O list de obiecte de tip <code>Student</code> continand inregistrari 
     * cu informatii corespunzatoare unui student.
     */
    protected ArrayList vStudent;

    /**
     * O lista de obiecte de tip <code>Course</code> continand inregsitrari 
     * cu informatii corespunzatoare unui curs.
     */
    protected ArrayList vCourse;

    /**
     * Construire nod de date care ofera acces la date despre cursuri si studenti. 
     * Datele initiale sunt completate cu informatii din fisierele cu cursuri 
     * si studenti. La momentul crearii, nodul datelor nu contine nici o informatie 
     * referitoare la inregistrari studenti la cursuri.
     *
     * @param sStudentFileName numele fisierului cu studenti
     * @param sCourseFileName numele fisierului cu cursuri
     */
    public Data(String sStudentFileName, String sCourseFileName)
           throws RemoteException, FileNotFoundException, IOException {
        // Deschiderea fisierelor cu studenti si cursuri date.
        BufferedReader objStudentFile = new BufferedReader(new FileReader(sStudentFileName));
        BufferedReader objCourseFile  = new BufferedReader(new FileReader(sCourseFileName));

        // Initializarea listelor de studenti si de cursuri.
        this.vStudent = new ArrayList();
        this.vCourse  = new ArrayList();

        // Popularea listelor de studenti si de cursuri.
        while (objStudentFile.ready()) {
            this.vStudent.add(new Student(objStudentFile.readLine()));
        }
        while (objCourseFile.ready()) {
           this.vCourse.add(new Course(objCourseFile.readLine()));
        }

        // Inchiderea fisierelor cu studenti si cursuri.
        objStudentFile.close();
        objCourseFile.close();
    }

    /**
     * Returnarea listei de studenti.
     *
     * @return o <code>ArrayList</code> de obiecte <code>Student</code> 
     * continand informatii despre studenti.
     */
    public ArrayList getAllStudentRecords() {
        // Returnarea listei de studenti.
        return this.vStudent;
    }

    /**
     * Returnarea listei tuturor cursurilor.
     *
     * @return o <code>ArrayList</code> de obiecte <code>Course</code> 
     * continand informatii despre cursuri.
     */
    public ArrayList getAllCourseRecords() {
        // Returnarea listei de cursuri.
        return this.vCourse;
    }

    /**
     * Returnarea informatiilor despre studentul cu ID-ul precizat.
     *
     * @param  sSID ID student precizat
     * @return obiectul <code>Student</code> cu ID-ul egal cu <code>sSID</code>.
     * sau <code>null</code> daca nu exista student cu acest ID
     * vezi:    #getStudentName(String)
     */
    public Student getStudentRecord(String sSID) {
        // Cautarea si returnarea informatiilor despre student, daca acesta exista.
        for (int i=0; i<this.vStudent.size(); i++) {
            Student objStudent = (Student) this.vStudent.get(i);
            if (objStudent.match(sSID)) {
                return objStudent;
            }
        }

        // Returnare null daca nu este gasit un student corespunzator.
        return null;
    }

    /**
     * Returnarea numelui unui student cu un ID egal cu un ID dat.
     *
     * @param  sSID ID student precizat
     * @return un <code>String</code> reprezentand numele studentului. 
     * sau <code>null</code> daca nu e gasit.
     * vezi:    #getStudentRecord(String)
     */
    public String getStudentName(String sSID) {
        // Cautare si returnarea numelui studentului daca acesta exista.
        for (int i=0; i<this.vStudent.size(); i++) {
            Student objStudent = (Student) this.vStudent.get(i);
            if (objStudent.match(sSID)) {
                return objStudent.getName();
            }
        }

        // Returnare null daca nu e gasit.
        return null;
    }

    /**
     * Returnarea informatiilor referitoare la un curs cu ID-ul egal cu un ID dat.
     *
     * @param  sCID un ID curs de cautat
     * @return un obiect <code>Course</code> cu ID-ul egal cu <code>sCID</code>
     * sau <code>null</code> daca nu exista.
     * vezi:    #getCourseName(String)
     */
    public Course getCourseRecord(String sCID) {
        // Cautare si returnare curs corespunzator, daca e gasit.
        for (int i=0; i<this.vCourse.size(); i++) {
            Course objCourse = (Course) this.vCourse.get(i);
            if (objCourse.match(sCID)) {
                return objCourse;
            }
        }

        // Returnare null daca nu e gasit.
        return null;
    }

    /**
     * Returnare nume curs cu ID-ul egal cu un ID dat.
     *
     * @param  sCID un ID curs de cautat
     * @return un <code>String</code> reprezentand numele cursului sau 
     * <code>null</code> daca nu e gasit.
     * vezi:    #getCourseRecord(String,String)
     */
    public String getCourseName(String sCID) {
        // Cautare si returnare curs corespunzator daca e gasit.
        for (int i=0; i<this.vCourse.size(); i++) {
            Course objCourse = (Course) this.vCourse.get(i);
            if (objCourse.match(sCID)) {
                return objCourse.getName();
            }
        }

        // Returnare null daca nu exista.
        return null;
    }

    /**
     * Realizare inregistrare student la un curs. 
     * Nu se face nici o verificare de conflicte inainte de actualizarea bazei de date. 
     * Nu se intampla nimic daca nu exista un curs si/sau un student corespunzator.
     *
     * @param  sSID ID student de inregistrat
     * @param  sCID ID curs la care se face inregistrarea
     */
    public void makeARegistration(String sSID, String sCID) {
        // Gasire student si curs.
        Student objStudent = this.getStudentRecord(sSID);
        Course  objCourse  = this.getCourseRecord(sCID);

        // Realizare inregistrare student la curs.
        if (objStudent != null && objCourse != null) {
            objStudent.registerCourse(objCourse);
            objCourse.registerStudent(objStudent);
        }
    }

    /**
     * Creare si lanstare nod de date. Sunt asteptati doi parametri: 
     * numele fisierului cu informatii despre studenti si
     * numele fisierului cu informatii despre cursuri.
     *
     * @param args array cu parametrii de intrare
     */
    public static void main(String args[]) {
        // Verificarea numarului de parametri.
        if (args.length != 2) {
            System.err.println("Numar incorect de parametrii");
            System.err.println("Utilizare: java Data <FisierStudenti> <FisierCursuri>");
            System.exit(1);
        }

        // Verificarea existentei fisierelor de intrare.
        if (!new File(args[0]).exists()) {
            System.err.println("Nu exista fisierul " + args[0]);
            System.exit(1);
        }
        if (new File(args[1]).exists() == false) {
            System.err.println("Nu exista fisierul " + args[1]);
            System.exit(1);
        }

        try {
            // Creare nod de date si publicare pentru comunicare prin RMI.
            Data objData = new Data(args[0], args[1]);
            Naming.bind(DATA_NAME, objData);
            System.out.println("Nodul datelor este gata pentru servire.");

            // Asteptare intrerupere de la utilizator.
            System.out.println("Apasati Enter pentru terminare.");
            System.in.read();

            // Eliberare resurse si terminare.
            Naming.unbind(DATA_NAME);
            System.out.println("Nodul datelor se dezactiveaza. Apasati Ctrl-C daca dureaza prea mult.");
        }
        catch (java.rmi.ConnectException e) {
            // Afisare mesaj de eroare si exit.
            System.err.println("Java RMI error: verificat daca rmiregistry este pornit.");
            System.exit(1);
        }
        catch (Exception e) {
            // Afisare informatii pentru depanare.
            System.err.println("Unexpected exception at " + DATA_NAME);
            e.printStackTrace();
            System.exit(1);
        }
    }
}
