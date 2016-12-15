package com.deb.pi.boardgame.core.util ;

import org.springframework.context.support.GenericXmlApplicationContext ;

/**
 * Loads a consolidated application context from multiple resource paths.
 * The resource paths need to be qualified as per the following guidelines:
 * 
 * Prefix     Example                        Description
 * -----------------------------------------------------------------------------
 * classpath: classpath:com/myapp/config.xml Loaded from the classpath.
 * file:      file:/data/config.xml          Loaded as a URL, from the disk.
 * http:      http://myserver/logo.png       Loaded as a URL.
 */
public class ObjectFactory {

    private static final String ROOT_RESOURCE_PATH = 
                                 "classpath:com/deb/pi/boardgame/bean-def.xml" ;
    
    private static String customResourcePath = null ;
    private static ObjectFactory instance = null ;
    private static boolean reloadRequired = true ;
    
    private GenericXmlApplicationContext appCtx = new GenericXmlApplicationContext() ;
    
    private ObjectFactory(){}
    
    public static void setCustomResourcePath( String path ) {
        customResourcePath = path ;
        reloadRequired = true ;
    }
    
    public static ObjectFactory instance() {
        if( instance == null || reloadRequired ) {
            instance = new ObjectFactory() ;
            instance.initialize() ;
            reloadRequired = false ;
        }
        return instance ;
    }
    
    private void initialize() throws RuntimeException {
        
        String resPath = customResourcePath ;
        if( resPath == null ) {
            resPath = ROOT_RESOURCE_PATH ;
        }
        
        this.appCtx.load( resPath );
        this.appCtx.refresh() ;
    }

    public <T> T getBean( String name, Class<T> requiredType ) {
        return appCtx.getBean( name, requiredType ) ;
    }

    public Object getBean( String name ) {
        return appCtx.getBean( name ) ;
    }
}
