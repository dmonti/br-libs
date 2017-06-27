package com.brlibs.document.validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.caelum.stella.validation.InvalidStateException;

public class CNPJValidator extends br.com.caelum.stella.validation.CNPJValidator {
    private static final Logger log = LogManager.getLogger();

    public static final int CNPJ_FORMATTED_SIZE = 18;

    public static final int CNPJ_ONLY_DIGITS_SIZE = 14;
    public static final int CNPJ_ONLY_DIGITS_BRANCH_SIZE = 6;

    private static final char ZERO = '0';
    private static final String INVALID_ZEROS = "00000000000000";

    public CNPJValidator ( boolean isFormatted ) {
        super( isFormatted );
    }

    public boolean safeEquals ( String cnpj1 , String cnpj2 ) {
        return ( cnpj1 != null ) && ( cnpj2 != null ) && normalize( cnpj1 ).equals( normalize( cnpj2 ) );
    }

    public boolean isValid ( String cnpj ) {
        if ( cnpj == null || cnpj.trim().isEmpty() )
            return false;

        cnpj = normalize( cnpj );
        if ( ! isEligible( cnpj ) || INVALID_ZEROS.equals( cnpj ) ) {
            return false;
        }

        try {
            assertValid( cnpj );
        } catch ( InvalidStateException e ) {
            log.warn( "CNPJ '" + cnpj + "' inválido" , e );
            return false;
        }

        return true;
    }

    public boolean hasSameParents ( String cnpj1 , String cnpj2 ) {
        cnpj1 = normalize( cnpj1 );
        cnpj2 = normalize( cnpj2 );

        final String cnpj1Parent = extractMatrixPrefix( cnpj1 );
        final String cnpj2Parent = extractMatrixPrefix( cnpj2 );

        return cnpj1Parent.equals( cnpj2Parent );
    }

    public String extractMatrixPrefix ( String cnpj ) {
        int lastIndex = ( cnpj.length() - 1 );
        return cnpj.substring( 0 , lastIndex - CNPJ_ONLY_DIGITS_BRANCH_SIZE );
    }

    public String normalize ( String cnpj ) {
        if ( cnpj == null ) {
            return null;
        }
        if ( ! isFormatted() ) {
            cnpj = removeNonNumerics( cnpj );
        }
        cnpj = addLeftZeros( cnpj );
        return cnpj;
    }

    protected String removeNonNumerics ( String cnpj ) {
        return cnpj.replaceAll( "[^\\d]" , "" );
    }

    protected String addLeftZeros ( String cnpj ) {
        StringBuilder cnpjBuilder = new StringBuilder( cnpj );
        while ( ! isFormatted() && cnpjBuilder.length() < CNPJ_ONLY_DIGITS_SIZE ) {
            cnpjBuilder.insert( 0 , ZERO );
        }
        return cnpjBuilder.toString();
    }
}