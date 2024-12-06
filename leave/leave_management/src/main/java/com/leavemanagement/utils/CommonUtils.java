package com.leavemanagement.utils;

import org.springframework.stereotype.Component;

@Component
public class CommonUtils {

	/**
	 * @param value
	 * @return true if value is not empty else false
	 */
	public static boolean isNullOrEmpty(Object value) {
		return value == null || value.toString().trim().isEmpty();
	}
	
	public static String convertString(String s )  
    {  
        int ctr = 0 ;  
        int n = s.length( ) ;  
        char ch[ ] = s.toCharArray( ) ;  
        int c = 0 ;   
        for ( int i = 0; i < n; i++ )  
        {  
            if( i == 0 )  
            ch[ i ] = Character.toUpperCase( ch[ i ] ) ;  
                ch[ c++ ] = ch[ i ] ;  
        }
        return String.valueOf( ch, 0, n - ctr ) ;  
    }  
}
