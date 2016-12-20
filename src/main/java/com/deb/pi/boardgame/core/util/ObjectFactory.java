package com.deb.pi.boardgame.core.util ;

import org.springframework.context.support.GenericXmlApplicationContext ;

import com.deb.pi.boardgame.core.device.LCD ;
import com.deb.pi.boardgame.core.gpio.GPIOManager ;

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

    private static final String LIVE_RESOURCE_PATH = 
            "classpath:com/deb/pi/boardgame/bean-def.xml" ;
    
    private static final String MOCK_RESOURCE_PATH = 
            "classpath:com/deb/pi/boardgame/bean-def-mock.xml" ;
    
    private static final String BN_GPIO_MANAGER = "GPIOManager" ;
    private static final String BN_LCD          = "LCD" ;
    
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
    
    private boolean isMockRun() {
        String runType = System.getProperty( "runType" ) ;
        if( runType == null ) {
            runType = System.getenv( "PI_RUN_TYPE" ) ;
        }
        
        if( runType != null && runType.equalsIgnoreCase( "mock" ) ) {
            return true ;
        }
        return false ;
    }
    
    private String getConfigResourcePath() {
        String resPath = customResourcePath ;
        if( resPath == null ) {
            if( isMockRun() ) {
                resPath = MOCK_RESOURCE_PATH ;
            }
            else {
                resPath = LIVE_RESOURCE_PATH ;
            }
        }
        return resPath ;
    }
    
    private void initialize() throws RuntimeException {
        this.appCtx.load( getConfigResourcePath() );
        this.appCtx.refresh() ;
    }

    public void reset() {
        customResourcePath = null ;
        reloadRequired = true ;
    }
    
    public <T> T getBean( String name, Class<T> requiredType ) {
        return appCtx.getBean( name, requiredType ) ;
    }

    public Object getBean( String name ) {
        return appCtx.getBean( name ) ;
    }
    
    public GPIOManager getGPIOManager() {
        return getBean( BN_GPIO_MANAGER, GPIOManager.class ) ;
    }
    
    public LCD getLCD() {
        return getBean( BN_LCD, LCD.class ) ;
    }
}
