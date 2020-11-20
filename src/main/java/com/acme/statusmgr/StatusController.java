package com.acme.statusmgr;

import java.util.InputMismatchException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.acme.statusmgr.beans.*;
import com.acme.statusmgr.beans.detailedstatus.ExtensionsStatus;
import com.acme.statusmgr.beans.detailedstatus.MemoryStatus;
import com.acme.statusmgr.beans.detailedstatus.OperationalStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller for all web/REST requests about the status of servers
 *
 * For initial school project - just handles info about this server
 * Syntax for URLS:
 *    All start with /server
 *
 *    /status will return status of server
 *    optional 'name' param specifies a requestor name to appear in response (default Anonymous)
 *    optional 'details' param places all comma-delimited arguments into a list outputted to the console
 *
 *    /status/detailed returns essentially same result as /status with extra details as specified
 *    optional 'name' param specifies a requestor name to appear in response (default Anonymous)
 *    required 'details' param returns detailed status of specific resource specified e.g. extensions
 *    Note: some resource status requests may throw an error if unsupported
 *
 * Examples:
 *    http://localhost:8080/server/status
 *
 *    http://localhost:8080/server/status?name=Noach&details=message1,message2
 *
 *    http://localhost:8080/server/status/detailed?details=memory,operations
 *
 *    http://localhost:8080/server/status/detailed?details=operations,memory&name=Noach
 *
 */
@RestController
@RequestMapping("/server")
public class StatusController {

    protected static final String template = "Server Status requested by %s";
    protected final AtomicLong counter = new AtomicLong();
    protected static String nullMessage = "Required List parameter 'details' is not present in request";

    @RequestMapping("/status")
    public ServerStatus serverStatusHandler(@RequestParam(value="name", defaultValue="Anonymous", required = false) String name, @RequestParam(value="details", defaultValue = "", required = false) List<String> detailsList) {
        System.out.println("*** DEBUG INFO ***\n" + detailsList.toString());
        return new ServerStatus(counter.incrementAndGet(),
                            String.format(template, name));
    }
    @RequestMapping("/status/detailed")
    public StatusInterface detailedStatusHandler(
            @RequestParam(value="name", defaultValue="Anonymous", required = false) String name,
            @RequestParam(value="details", required = false, defaultValue = "") List<String> detailedList) {
        StatusInterface SIReturn = new ServerStatus(counter.incrementAndGet(), String.format(template, name));
        if (detailedList.isEmpty())
            throw new ForbiddenException(nullMessage);
        for (String detailedListItem : detailedList) {
            switch (detailedListItem.toLowerCase()) {
                case "operations":
                    SIReturn = new OperationalStatus(SIReturn);
                    System.out.println(SIReturn.getStatusDesc());
                    break;
                case "extensions":
                    SIReturn = new ExtensionsStatus(SIReturn);
                    break;
                case "memory":
                    SIReturn = new MemoryStatus(SIReturn);
                    break;
                default:
                    throw new ForbiddenException(String.format("Invalid details option: %s", detailedListItem));

        }
    }
        return SIReturn;
    }

    @RequestMapping("/exception")
    @ResponseBody
    public ResponseEntity sendViaException() {
        throw new ForbiddenException("");
    }
}

