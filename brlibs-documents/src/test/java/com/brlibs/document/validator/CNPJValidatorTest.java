package com.brlibs.document.validator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CNPJValidatorTest {

    private CNPJValidator cnpjValidator;

    @Before
    public void setup () {
    }

    @Test
    public void testValidCNPJs () {
        cnpjValidator = new CNPJValidator( true );
        Assert.assertTrue( cnpjValidator.isValid( "00.881.753/0001-53" ) );
        Assert.assertTrue( cnpjValidator.isValid( "08.145.915/0001-05" ) );
        Assert.assertTrue( cnpjValidator.isValid( "34.526.425/0001-50" ) );

        cnpjValidator = new CNPJValidator( false );
        Assert.assertTrue( cnpjValidator.isValid( "00881753000153" ) );
        Assert.assertTrue( cnpjValidator.isValid( "881753000153" ) );
        Assert.assertTrue( cnpjValidator.isValid( "08145915000105" ) );
        Assert.assertTrue( cnpjValidator.isValid( "8145915000105" ) );
        Assert.assertTrue( cnpjValidator.isValid( "34526425000150" ) );
    }

    @Test
    public void testInvalidCNPJs () {
        cnpjValidator = new CNPJValidator( false );
        Assert.assertFalse( cnpjValidator.isValid( null ) );
        Assert.assertFalse( cnpjValidator.isValid( "" ) );
        Assert.assertFalse( cnpjValidator.isValid( "00000000000000" ) );
        Assert.assertFalse( cnpjValidator.isValid( "11111111111111" ) );
    }

    @Test
    public void testHasSameParents () {
        cnpjValidator = new CNPJValidator( true );
        Assert.assertTrue( cnpjValidator.hasSameParents( "34.526.425/1111-11" , "34.526.425/2222-22" ) );
        Assert.assertFalse( cnpjValidator.hasSameParents( "34.526.425/1111-11" , "35.526.425/2222-22" ) );

        cnpjValidator = new CNPJValidator( false );
        Assert.assertTrue( cnpjValidator.hasSameParents( "00881753111111" , "00881753222222" ) );
        Assert.assertTrue( cnpjValidator.hasSameParents( "08145915111111" , "08145915222222" ) );

        Assert.assertFalse( cnpjValidator.hasSameParents( "00881753111111" , "00981753222222" ) );
        Assert.assertFalse( cnpjValidator.hasSameParents( "08145915111111" , "08146915222222" ) );
    }
}