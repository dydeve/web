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

    public static final String LOCAL_IP = initLocalIpAddress();

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

    public static String getIpAddress(HttpServletRequest httpRequest) {
        String ip;
        for (String headName : LOOP_HEAD_NAMES) {
            ip = httpRequest.getHeader(headName);
            if (!isIpInvalid(ip)) {
                ip = getFirstValidIp(ip);
                if (ip != null) {
                    return ip;
                }
            }
        }

        ip = httpRequest.getRemoteAddr();

        if (log.isDebugEnabled()) {
            //can't get ip from head, show all headers
            log.debug("remote upstream ip is : {}, headers are : {}", ip, getHeaders(httpRequest));
        }


        return ip;

    }

    public static String getHeaders(HttpServletRequest httpRequest) {
        Enumeration<String> headerNaames = httpRequest.getHeaderNames();
        String currentHeaderName;
        StringBuilder buf = new StringBuilder();
        while (headerNaames.hasMoreElements()) {
            currentHeaderName = headerNaames.nextElement();
            buf.append(currentHeaderName).append(":");
            buf.append(httpRequest.getHeader(currentHeaderName)).append(",").append("\n");
        }
        buf.delete(buf.length() - 2, buf.length());
        return buf.toString();
    }

    public static String getFirstValidIp(String ipString) {
        String[] ips = ipString.split(",");
        for (String ip : ips) {
            if (!isIpInvalid(ip)) {
                return ip;
            }
        }
        return null;
    }

    public static boolean isLocalIp(String ip) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ip);
            return inetAddress.isSiteLocalAddress() || inetAddress.isLoopbackAddress();
        } catch (UnknownHostException e) {
            log.error("parse ip {} fail. e.message {}",  ip, e.getMessage());
            return false;
        }
    }

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
