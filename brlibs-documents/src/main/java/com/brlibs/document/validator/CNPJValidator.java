package com.brlibs.document.validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.caelum.stella.validation.InvalidStateException;

public class CNPJValidator extends br.com.caelum.stella.validation.CNPJValidator {
    private static final Logger log = LogManager.getLogger();

    public static final int CNPJ_ONLY_DIGITS_SIZE = 14;
    public static final int CNPJ_ONLY_DIGITS_BRANCH_SIZE = 6;

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

        final String cnpj1Parent = extractParentNumbers( cnpj1 );
        final String cnpj2Parent = extractParentNumbers( cnpj2 );

        return cnpj1Parent.equals( cnpj2Parent );
    }

    public String extractParentNumbers ( String cnpj ) {
        int lastIndex = ( cnpj.length() - 1 );
        return cnpj.substring( 0 , lastIndex - CNPJ_ONLY_DIGITS_BRANCH_SIZE );
    }

    public String normalize ( String cnpj ) {
        if ( cnpj != null ) {
            cnpj = removeNonNumerics( cnpj );
            cnpj = addLeftZeros( cnpj );
        }
        return cnpj;
    }

    protected String removeNonNumerics ( String cnpj ) {
        return cnpj.replaceAll( "[^\\d]" , "" );
    }

    protected String addLeftZeros ( String cnpj ) {
        while ( cnpj.length() < CNPJ_ONLY_DIGITS_SIZE ) {
            cnpj = ( '0' + cnpj );
        }
        return cnpj;
    }
}