/**
 * @(#)Course.java
 */


import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;


/**
 * Clasa reprezinta o inregistrare corespunzatoare unui curs. Ea contine informatii generale 
 * despre curs si lista studentilor inregistrati la acest curs. 
 * Clasa este construita dintr-un sir de caractere orientat pe campuri separate de spatii.
 * Clasa implementeaza <code>Serializable</code> astfel incat instante ale ei sa poata fi
 * transferate intre masini prin RMI.
 */
public class Course implements Serializable {

    /**
     * Sir ce reprezinta ID curs.
     */
    protected String sCID;

    /**
     * Sir ce reprezinta ziua din saptamana cand se tine cursul.
     */
    protected String sDays;

    /**
     * Sir ce reprezinta ora de incepere a cursului.
     */
    protected int iStart;

    /**
     * Sir ce reprezinta ora de terminare a cursului.
     */
    protected int iStop;

    /**
     * Sir ce reprezinta numele profesorului.
     */
    protected String sInstructor;

    /**
     * Sir ce reprezinta numele cursului.
     */
    protected String sName;

    /**
     * Lista ce contine studentii inregistrati la acest curs. Elementele din lista sunt
     * obiecte de tip <code>Student</code> ce reprezinta studentii inregistrati.
     */
    protected ArrayList vRegistered;

    /**
     * Construire obiect de tip curs prin parsarea sirului de intrare. Sirul <code>sInput</code> este
     * orientat pe campuri separate de spatii. Campurile sunt ID curs, ziua cursului,
     * ora de incepere, ora de terminare, profesor si nume curs. 
     * Desi nu este necesar, evitati totusi includerea de caractere "newline" 
     * in sirul <code>sInput</code>.
     *
     * @param sInput sirul ce trebuie parsat si care reprezinta o inregistrare 
     * corespunzatoare unui curs.
     */
    public Course(String sInput)
    {
        // Pregatire pentru tokenize-area sirului de intrare.
        StringTokenizer objTokenizer = new StringTokenizer(sInput);

        // Extragere ID curs, timp si profesor.
        this.sCID        = objTokenizer.nextToken();
        this.sDays       = objTokenizer.nextToken();
        this.iStart      = Integer.parseInt(objTokenizer.nextToken());
        this.iStop       = Integer.parseInt(objTokenizer.nextToken());
        this.sInstructor = objTokenizer.nextToken();

        // Extragere nume curs.
        this.sName = objTokenizer.nextToken();
        while (objTokenizer.hasMoreTokens()) {
            this.sName = this.sName + " " + objTokenizer.nextToken();
        }

        // Pregatire pentru memorarea studentilor ce se vor inregistra la acest curs.
        this.vRegistered = new ArrayList();
    }

    /**
      * Verificare daca sirul <code>sCID</code> dat este egal cu ID-ul acestui curs.
      *
      * @param  sCID sir reprezentand un ID curs
      * @return <code>true</code> daca <code>sCID</code> este egal cu ID-ul acestui curs.
      */
    public boolean match(String sCID) {
        return this.sCID.equals(sCID);
    }

    /**
      * Verificare daca exista conflict de timp intre inregistrarea corespunzatoare cursului 
      * <code>objCourse</code> si inregistrarea corespunzatoare acestui curs.
      *
      * @param  objCourse un obiect de tip Course de verificat ]n raport cu acest curs.
      * @return <code>true</code> daca <code>objCourse</code> e in conflict cu acest curs.
      */
    public boolean conflicts(Course objCourse) {
        // Conflict de tip "doua cursuri cu acelasi ID".
        if (this.sCID.equals(objCourse.sCID)) {
            return true;
        }

        // Conflict de tip "suprapunere de ore intre doua cursuri".
        for (int i=0; i<this.sDays.length(); i++) {
            for (int j=0; j<objCourse.sDays.length(); j++) {
                if (this.sDays.regionMatches(i, objCourse.sDays, j, 1)) {
                    return (this.iStart <= objCourse.iStart && objCourse.iStart < this.iStop)
                        || (objCourse.iStart <= this.iStart && this.iStart < objCourse.iStop) 
                        ? true : false;
                }
            }
        }
        return false;
    }

    /**
      * Returneaza numele acestui curs.
      */
    public String getName() {
        return this.sName;
    }

    /**
      * Returneaza o lista cu studentii inregistrati la acest curs.
      *
      * @return studentii inregistrati la acest curs ca <code>ArrayList</code> de
      * obiecte de tip <code>Students</code>. 
      */
    public ArrayList getRegisteredStudents() {
        return this.vRegistered;
    }

    /**
     * Inregistrarea unui student la acest curs.
     *
     * @param referinta la obiectul ce reprezinta studentul ce trebuie inregistrat.
     * vezi:   Student#registerCourse(Course)
     */
    public void registerStudent(Student objStudent) {
        this.vRegistered.add(objStudent);
    }

    /**
     * Returneaza un sir ce reprezinta integistrarea corespunzatoare acestui curs. 
     * Sirul rezultat va fi in acelasi format ca si argumentul costructorului acestei clase.
     *
     * @return un sir ce reprezinta inregistrarea corespunzatoare cursului
     * vezi:    #Course(String)
     * vezi:    Student#toString()
     */
    public String toString() {
        // Crearea sirului de returnat ce contine ID curs, zi curs
        // ora incepere, ora terminare, profesor si numele cursului.
        return this.sCID + " " + this.sDays + " " + this.iStart
               + " " + this.iStop + " " + this.sInstructor + " " + this.sName;
    }
}
