package com.brlibs.certificate;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.brlibs.certificate.keystore.KeyStoreLoader;

public class X509CertificateLoader {
    public static List < X509Certificate > loadList ( InputStream crtStream ) throws GeneralSecurityException , IOException {
        List < X509Certificate > certificates = new ArrayList < X509Certificate >();

        CertificateFactory factory = CertificateFactory.getInstance( "X.509" );
        BufferedInputStream bufferStream = new BufferedInputStream( crtStream );
        while ( bufferStream.available() > 0 ) {
            X509Certificate certificate = ( X509Certificate ) factory.generateCertificate( bufferStream );
            certificates.add( certificate );
        }

        return certificates;
    }

    public static X509Certificate getCertificateByAlias ( String keyAlias ) throws KeyStoreException {
        X509Certificate certificate = null;
        KeyStore keyStore = KeyStoreLoader.load();
        Enumeration < String > aliases = keyStore.aliases();
        while ( aliases.hasMoreElements() ) {
            String alias = ( String ) aliases.nextElement();
            if ( alias.equals( keyAlias ) && isValidX509KeyEntry( keyStore , alias ) ) {
                certificate = ( X509Certificate ) keyStore.getCertificate( keyAlias );
            }
        }
        return certificate;
    }

    public static boolean isValidX509KeyEntry ( KeyStore keyStore , String alias ) throws KeyStoreException {
        String type = keyStore.getCertificate( alias ).getPublicKey().getFormat();
        return type.equalsIgnoreCase( "X.509" ) && keyStore.isKeyEntry( alias );
    }
}