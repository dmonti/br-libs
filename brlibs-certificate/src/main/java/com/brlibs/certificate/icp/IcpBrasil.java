package com.brlibs.certificate.icp;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateParsingException;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.brlibs.certificate.X509CertificateLoader;

public class IcpBrasil {
    private static final Logger log = LogManager.getLogger();

    private static IcpBrasil INSTANCE;

    private Set < TrustAnchor > trustIcpAnchors;

    public static IcpBrasil getInstance () {
        if ( INSTANCE == null ) {
            INSTANCE = new IcpBrasil();
        }
        return INSTANCE;
    }

    public void initialize () throws GeneralSecurityException , IOException {
        log.info( "Initializing ICP Brasil trusted anchors certificates." );

        final Set < TrustAnchor > trustAnchors = new HashSet < TrustAnchor >();
        trustAnchors.addAll( loadCertificatePath( "ICP-Brasil.crt" ) );
        trustAnchors.addAll( loadCertificatePath( "ICP-Brasilv2.crt" ) );
        trustAnchors.addAll( loadCertificatePath( "ICP-Brasilv5.crt" ) );
        setTrustIcpAnchors( trustAnchors );
    }

    private Set < TrustAnchor > loadCertificatePath ( String streamPath ) throws GeneralSecurityException , IOException {
        final Set < TrustAnchor > trustAnchors = new HashSet < TrustAnchor >();
        InputStream crtStream = IcpBrasil.class.getClassLoader().getResourceAsStream( streamPath );
        try {
            trustAnchors.addAll( loadCertificateAnchors( crtStream ) );
        } catch ( CertificateParsingException e ) {
            log.warn( "Exception loading certificate " + streamPath , e );
        }
        return trustAnchors;
    }

    private Set < TrustAnchor > loadCertificateAnchors ( InputStream crtStream ) throws GeneralSecurityException , IOException {
        final Set < TrustAnchor > trustAnchors = new HashSet < TrustAnchor >();
        List < X509Certificate > certificates = X509CertificateLoader.loadList( crtStream );
        for ( X509Certificate trustedRootCert : certificates ) {
            trustAnchors.add( new TrustAnchor( trustedRootCert , null ) );
        }
        return trustAnchors;
    }

    public boolean isTrusted ( X509Certificate certificate , List < Certificate > intermediateCertificates ) {
        if ( trustIcpAnchors == null || trustIcpAnchors.isEmpty() ) {
            log.warn( "ICP Brasil trusted anchors not initialized" );
            throw new RuntimeException( "Cadeia de certificados ICP Brasil não encontrada." );
        }

        X509CertSelector certSelector = new X509CertSelector();
        certSelector.setCertificate( certificate );

        try {
            PKIXBuilderParameters pkixParams = new PKIXBuilderParameters( getTrustIcpAnchors() , certSelector );
            pkixParams.setRevocationEnabled( false );

            CertStore certStore = CertStore.getInstance( "Collection" , new CollectionCertStoreParameters( intermediateCertificates ) , "BC" );
            pkixParams.addCertStore( certStore );

            CertPathBuilder builder = CertPathBuilder.getInstance( "PKIX" , "BC" );
            builder.build( pkixParams );
            return true;
        } catch ( GeneralSecurityException e ) {
            return false;
        }
    }

    public Set < TrustAnchor > getTrustIcpAnchors () {
        return trustIcpAnchors;
    }

    public void setTrustIcpAnchors ( Set < TrustAnchor > trustIcpAnchors ) {
        this.trustIcpAnchors = trustIcpAnchors;
    }
}