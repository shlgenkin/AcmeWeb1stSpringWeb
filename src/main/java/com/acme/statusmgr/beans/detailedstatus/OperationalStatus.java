package com.acme.statusmgr.beans.detailedstatus;

import com.acme.servermgr.ServerManager;
import com.acme.statusmgr.beans.StatusInterface;

public class OperationalStatus implements StatusInterface {

    StatusInterface inputSI;

    public OperationalStatus (StatusInterface SIArgument){
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

    @Override
    public String getStatusDesc() {
        return String.format(DecoratorTools.detailedTemplate, inputSI.getStatusDesc(), ServerManager.getOperationalStatus());
    }

}
