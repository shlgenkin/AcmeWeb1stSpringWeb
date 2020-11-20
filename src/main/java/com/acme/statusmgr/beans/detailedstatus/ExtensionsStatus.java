package com.acme.statusmgr.beans.detailedstatus;

import com.acme.servermgr.ServerManager;
import com.acme.statusmgr.beans.StatusInterface;

/**
 * Class used to obtain status of specific resource of server.
 * This class and all status classes located in detailedStatus package follow the same decorator pattern.
 * They all accept a StatusInterface object and return its getId() and
 * getContentHeader() method unchanged. However, each class decorates the
 * object's getStatusDesc() method by adding their own status message in
 * addition to / on top of the original returned content. For instance, the ExtensionsStatus
 * class returns the original content provided by the StatusInterface object followed
 * by the extensions status.
 */
public class ExtensionsStatus implements StatusInterface {

    StatusInterface inputSI;

    /**
     * @param SIArgument A StatusInterface object to be decorated
     */
    public ExtensionsStatus (StatusInterface SIArgument){
        inputSI = SIArgument;
    }

    @Override
    public long getId() {
        return inputSI.getId();
    }

    @Override
    public String getContentHeader() {
        return inputSI.getContentHeader();
    }

    /**
     * @return The original content from provided StatusInterface object and specific status returned from ServerManager.
     *
     */
    @Override
    public String getStatusDesc() {
        return String.format(DecoratorTools.detailedTemplate, inputSI.getStatusDesc(), ServerManager.getExtensionsStatus());
    }
}
