package com.acme.statusmgr.beans;

/**
 *  Interface used by all classes that obtain a status (status classes).
 *  Created for implementing decorator pattern.
 */
public interface StatusInterface {
    long getId();
    String getContentHeader();
    String getStatusDesc();
}
