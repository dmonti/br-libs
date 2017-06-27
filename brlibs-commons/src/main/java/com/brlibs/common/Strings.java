package com.brlibs.common;

public class Strings {
    public static boolean isNullOrEmpty ( String string ) {
        return string == null || string.isEmpty();
    }

    public static String removeNonNumerics ( String value ) {
        return value.replaceAll( "[^\\d]" , "" );
    }
}