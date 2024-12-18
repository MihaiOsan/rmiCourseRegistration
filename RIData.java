import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface RIData extends Remote {
    public ArrayList getAllStudentRecords()
        throws RemoteException;

    public ArrayList getAllCourseRecords()
        throws RemoteException;

    public Student getStudentRecord(String sSID)
        throws RemoteException;

    public String getStudentName(String sSID)
        throws RemoteException;

    public Course getCourseRecord(String sCID)
        throws RemoteException;

    public String getCourseName(String sCID)
        throws RemoteException;

    public void makeARegistration(String sSID, String sCID)
        throws RemoteException;
}
