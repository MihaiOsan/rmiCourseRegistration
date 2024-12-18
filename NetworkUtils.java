import java.net.*;
import java.util.Enumeration;

/**
 * Utilitar pentru operațiuni legate de rețea.
 */
public class NetworkUtils {

    /**
     * Returnează adresa IP non-loopback a mașinii.
     *
     * @return Adresa IP non-loopback sau un mesaj de eroare dacă nu a fost găsită.
     */
    public static String getNonLoopbackAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // Exclude interfețele inactive sau de tip loopback
                if (!iface.isUp() || iface.isLoopback()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "Adresa IP nu a fost găsită!";
    }

    public static void main (String args[]){
        System.out.println(NetworkUtils.getNonLoopbackAddress());
    }
}
