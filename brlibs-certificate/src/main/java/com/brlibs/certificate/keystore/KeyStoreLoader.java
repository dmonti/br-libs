package com.brlibs.certificate.keystore;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

import com.brlibs.system.OperatingSystem;

public class KeyStoreLoader {
    public static KeyStore load () {
        KeyStore keyStore = null;
        if ( OperatingSystem.isWindows() ) {
            keyStore = load( KeyStoreType.SUNMSCAPI );
        } else if ( OperatingSystem.isMac() ) {
            keyStore = load( KeyStoreType.KEYCHAINSTORE );
        }
        return keyStore;
    }

    public static KeyStore load ( KeyStoreType type ) {
        try {
            KeyStore keyStore = getInstance( type );
            keyStore.load( null , null );
            return keyStore;
        } catch ( GeneralSecurityException | IOException e ) {
            throw new RuntimeException( "Erro carregando do keystore de tipo " + type.toString() , e );
        }
    }

    public static KeyStore getInstance ( KeyStoreType type ) throws GeneralSecurityException {
        KeyStore keyStore = null;
        if ( KeyStoreType.PKCS12.equals( type ) )
            keyStore = KeyStore.getInstance( type.toString() , "BC" );
        else if ( KeyStoreType.SUNMSCAPI.equals( type ) )
            keyStore = KeyStore.getInstance( type.toString() , "SunMSCAPI" );
        else if ( KeyStoreType.KEYCHAINSTORE.equals( type ) )
            keyStore = KeyStore.getInstance( type.toString() , "Apple" );
        else if ( keyStore == null ) {
            keyStore = KeyStore.getInstance( type.toString() );
        }
        return keyStore;
    }
}