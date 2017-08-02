package dydeve.monitor.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * Created by yuduy on 2017/8/2.
 */
public class IPUtils {

    private static final Logger log = LoggerFactory.getLogger(IPUtils.class);

    private IPUtils() {
        throw new UnsupportedOperationException();
    }

    private static final String LOCAL_IP = initLocalIpAddress();

    private static final String XFF = "X-Forwarded-For";

    /**
     * X-Forwarded-For
     * Proxy-Client-IP
     * WL-Proxy-Client-IP
     * HTTP_CLIENT_IP
     * HTTP_X_FORWARDED_FOR
     */

    private static final String[] LOOP_HEAD_NAMES = new String[] {XFF};

    public static boolean isIpInvalid(String ip) {
        return ip == null || ip.length() == 0 || "unknown".equals(ip);
    }

    public static String getRemoteIp(HttpServletRequest httpRequest) {
        String ip = null;
        for (String headName : LOOP_HEAD_NAMES) {
            ip = httpRequest.getHeader(headName);
            if (!isIpInvalid(ip)) {
                break;
            }
            ip = null;
        }

        if (ip != null) {
return null;
        }
        return null;

    }

    /*public static boolean isLocalIp(String ip) {
        try {
            InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }*/

    //From HBase Addressing.Java getIpAddress
    private static InetAddress getLocalInetAddress() throws SocketException {
        // Before we connect somewhere, we cannot be sure about what we'd be bound to; however,
        // we only connect when the message where client ID is, is long constructed. Thus,
        // just use whichever IP address we can find.
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface current = interfaces.nextElement();
            if (!current.isUp() || current.isLoopback() || current.isVirtual()) {
                continue;
            }
            Enumeration<InetAddress> addresses = current.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                if (!addr.isLoopbackAddress()) {
                    return addr;
                }
            }
        }

        throw new SocketException("Can't get our ip address, interfaces are: " + interfaces);
    }

    private static String initLocalIpAddress() {
        try {
            return getLocalInetAddress().getHostAddress();
        } catch (SocketException e) {
            log.error("can't get localhost ip address.", e);
            try {
                return InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e1) {
                log.error("unknown host.", e);
            }
            return "127.0.0.1";
        }
    }

    public static void main(String[] args) throws UnknownHostException {
        System.out.println(InetAddress.getLocalHost().getHostAddress());
    }

}
