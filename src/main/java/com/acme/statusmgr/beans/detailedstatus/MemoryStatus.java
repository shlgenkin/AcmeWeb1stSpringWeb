package com.acme.statusmgr.beans.detailedstatus;

import com.acme.servermgr.ServerManager;
import com.acme.statusmgr.beans.StatusInterface;

public class MemoryStatus implements StatusInterface {

    StatusInterface inputSI;

    public MemoryStatus (StatusInterface SIArgument){
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
        return String.format(DecoratorTools.detailedTemplate, inputSI.getStatusDesc(), ServerManager.getMemoryStatus());
    }
}
