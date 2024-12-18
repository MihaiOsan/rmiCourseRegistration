/**
 * @(#)Student.java
 */


import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;


/**
 * Clasa reprezinta o inregistrare cu informatii despre un student. 
 * Clasa contine informatii personale ale studentului si 
 * ID-urile cursurilor pe care studentul le-a absolvit.
 * Clasa este construita dintr-un sir orientat pe campuri separate cu spatii.
 * Clasa implementeaza interfata <code>Serializable</code> pentru ca instantele
 * sale sa poata fi transferate prin RMI intre masini.
 */
public class Student implements Serializable {

    /**
     * Sir reprezentand ID-ul studentului.
     */
    protected String sSID;

    /**
     * Sir reprezentand numele studentului.
     */
    protected String sName;

    /**
     * Sir reprezentand specializarea studentului.
     */
    protected String sSpecializare;

    /**
     * Lista cursurilor absolvite de student. Elementele listei sunt
     * obiecte de tip <code>String</code>, reprezentand ID-urile cursurilor absolvite.
     */
    protected ArrayList vCompleted;

    /**
     * Lista cursurilor la care este inregistrat studentul. Elementele listei sunt
     * obiecte de tip <code>Course</code>, reprezentand cursurile la care s-a inregistrat studentul.
     */
    protected ArrayList vRegistered;

    /**
     * Construieste un obiect de tip Student prin parsarea sirului dat. 
     * Argumentul <code>sInput</code> este un sir orieantat pe campuri separate prin spatii. 
     * Primele trei campuri necesare sunt ID student, nume student si specializare. 
     * Urmeaza ID-urile cursurilor absolvite de student (daca exista).
     * Desi nu este necesar, evitati includerea de caractere "newline" 
     * in sirul <code>sInput</code>.
     *
     * @param sInput sirul ce trebuie parsat si care reprezinta o inregistrare 
     * cu informatii despre student.
     */
    public Student(String sInput)
    {
        // Pregatire pentru tokeniz-area sirului de intrare.
        StringTokenizer objTokenizer = new StringTokenizer(sInput);

        // Preluare ID student, nume si specializare.
        this.sSID     = objTokenizer.nextToken();
        this.sName    = objTokenizer.nextToken();
        this.sName    = this.sName + " " + objTokenizer.nextToken();
        this.sSpecializare = objTokenizer.nextToken();

        // Preluarea cursurilor absolvite de student.
        this.vCompleted = new ArrayList();
        while (objTokenizer.hasMoreTokens()) {
            this.vCompleted.add(objTokenizer.nextToken());
        }

        // Pregatire pentru memorarea cursurilor la care se va inregistra studentul.
        this.vRegistered = new ArrayList();
    }

    /**
      * Verificare daca ID-ul <code>sSID</code> dat este egal cu ID-ul studentului 
      * reprezentat de acest obiect.
      *
      * @param  sSID un sir reprezentand un ID student
      * @return <code>true</code> daca <code>sSID</code> este egal cu ID-ul student al acestui obiect.
      */
    public boolean match(String sSID) {
        return this.sSID.equals(sSID);
    }

    /**
     * Returnarea numelui acestui student.
     *
     * @return numele si prenumele studentului reprezentat de acest obiect
     */
    public String getName() {
        return this.sName;
    }

    /**
     * Returnarea listei cursurilor la care este inregistrat acest student.
     *
     * @return cursurile la care este inregistrat studentul, ca 
     * <code>ArrayList</code> de <code>String</code>s 
     * vezi:    #getCompletedCourses()
     */
    public ArrayList getRegisteredCourses() {
        return this.vRegistered;
    }

    /**
     * Returnarea listei cursurilor absolvite de acest student.
     *
     * @return cursurile absolvite de acest student, ca <code>ArrayList</code> de 
     * <code>Course</code>s 
     * vezi:    #getRegisteredCourses()
     */
    public ArrayList getCompletedCourses() {
        return this.vCompleted;
    }

    /**
     * Inregistrare pentru un curs.
     *
     * @param objCourse referinta la un obiect curs la care se va face inregistrarea.
     * vezi:   Course#registerStudent(Student)
     */
    public void registerCourse(Course objCourse) {
        this.vRegistered.add(objCourse);
    }

    /**
     * Returnarea reprezentarii sub forma de sir a informatiilor despre acest student. 
     * Sirul rezultat va fi in acelasi format ca si argumentul constructorului acestei clase.
     *
     * @return o reprezentare sub forma de sir a acestui student
     * vezi:    #Student(String)
     * vezi:    Course#toString()
     */
    public String toString() {
        // Crearea sirului rezultat utilizand ID student, nume si specializare.
        String sReturn = this.sSID + " " + this.sName + " " + this.sSpecializare;

        // Adaugarea ID-urilor cursurilor absolvite.
        for (int i=0; i<this.vCompleted.size(); i++) {
            sReturn = sReturn + " " + this.vCompleted.get(i).toString();
        }

        return sReturn;
    }
}
