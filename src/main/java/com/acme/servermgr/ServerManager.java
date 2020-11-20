package com.acme.servermgr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Manage all servers (service providers) being tracked by the Acme server tracking system
 * For now just some simple static methods for use in school project.
 * Treat this as a 'black box' that gives back indicators like up, running etc for
 * various 'services' that are being managed.
 */
public class ServerManager {

    /**
     * Get the status of this server
     * @return a descriptive string about the servers status
     */

    static List<String> ExtensionsList= new ArrayList<String>(
            Arrays.asList("Hypervisor", "Kubernetes", "RAID-6"));

    static public String getCurrentServerStatus() {
        return "up";  // The server is up
    }

    static public String getOperationalStatus() {
        return "is operating normally";
    }

    static public String getExtensionsStatus() {
        return "is using these extensions - " + ExtensionsList;
    }

    static public String getMemoryStatus(){
        return "its memory is running low";
    }

    /**
     * Find out if this server is operating normally
     * @return Boolean indicating if server is operating normally
     */
    static public Boolean isOperatingNormally()
    {
        return true;
    }
}
