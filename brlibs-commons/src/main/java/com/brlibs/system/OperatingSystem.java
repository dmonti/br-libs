package com.brlibs.system;

public class OperatingSystem {
    private static String OS_NAME = System.getProperty( "os.name" ).toLowerCase();

    private static final String MAC = "mac";
    private static final String WIN = "win";
    private static final String SOLARIS = "sunos";
    private static final String[] UNIX = { "nix" , "nux" , "aix" };

    public static boolean isWindows () {
        return contains( OS_NAME , WIN );
    }

    public static boolean isMac () {
        return contains( OS_NAME , MAC );
    }

    public static boolean isUnix () {
        return contains( OS_NAME , UNIX );
    }

    public static boolean isSolaris () {
        return contains( OS_NAME , SOLARIS );
    }

    private static boolean contains ( String value , String... parts ) {
        boolean contains = false;
        for ( String part : parts ) {
            if ( value.indexOf( part ) != - 1 ) {
                contains = true;
                break;
            }
        }
        return contains;
    }
}