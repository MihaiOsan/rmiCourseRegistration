import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RILogic extends Remote {
    public String getAllStudents() 
        throws RemoteException;

    public String getAllCourses() 
        throws RemoteException;

    public String getRegisteredStudents(String sCID) 
        throws RemoteException;

    public String getRegisteredCourses(String sSID) 
        throws RemoteException;

    public String getCompletedCourses(String sSID) 
        throws RemoteException;

    public String makeARegistration(String sSID, String sCID) 
        throws RemoteException;
}
