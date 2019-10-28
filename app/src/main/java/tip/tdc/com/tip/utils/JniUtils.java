package tip.tdc.com.tip.utils;

/**
 * Created by thanh on 07/04/2016.
 */
public class JniUtils {
    /**
     * Check device can use C lib
     */

    public static boolean isDeviceCompetible = false;
    public static boolean loadAndCheckLibCompatiable(){
        if(!isDeviceCompetible) {
            try {
                System.loadLibrary("TinTin");
                isDeviceCompetible = true;
            } catch (UnsatisfiedLinkError e) {
                LoggerFactory.logStackTrace(e);
                isDeviceCompetible = false;
            }
        }
        return isDeviceCompetible;
    }

}
