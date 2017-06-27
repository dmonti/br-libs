package com.brlibs.certificate.keystore;

public enum KeyStoreType {
    BKS ( "BKS" ) , //
    JCEKS ( "JCEKS" ) , //
    JKS ( "JKS" ) , //
    KEYCHAINSTORE ( "KeychainStore" ) , //
    PKCS11 ( "PKCS11" ) , //
    PKCS12 ( "PKCS12" ) , //
    SUNMSCAPI ( "Windows-MY" ) , //
    UBER ( "UBER" );

    private final String name;

    private KeyStoreType ( String keyStoreType ) {
        this.name = keyStoreType;
    }

    public String toString () {
        return this.name;
    }
}