package com.deb.pi.boardgame.junit.util;

import static org.hamcrest.CoreMatchers.equalTo ;
import static org.hamcrest.CoreMatchers.is ;
import static org.junit.Assert.assertThat ;

import org.junit.Before ;
import org.junit.Test ;

import com.deb.pi.boardgame.core.util.ObjectFactory ;

public class ObjectFactoryTestCase {

    private static final String RES_PATH = 
            "classpath:com/deb/pi/boardgame/junit/util/of-test-bean-def.xml" ;
    
    private ObjectFactory objFactory = null ;
    
    @Before
    public void loadObjectFactory() {
        
        ObjectFactory.setCustomResourcePath( RES_PATH ) ;
        objFactory = ObjectFactory.instance() ;
    }
    
    @Test public void loadSimpleBean() throws Exception {
        
        SampleBean bean = ( SampleBean )objFactory.getBean( "testBean" ) ;
        assertThat( bean.getFirstName(), is( equalTo( "Aniruddha" ) ) ) ;
        assertThat( bean.getLastName(), is( equalTo( "Deb" ) ) ) ;
        assertThat( bean.getAge(), is( equalTo( 14 ) ) ) ;
    }
    
    @Test public void loadImportedBean() throws Exception {
        
        SampleBean bean = ( SampleBean )objFactory.getBean( "importedBean" ) ;
        assertThat( bean.getFirstName(), is( equalTo( "Sandeep" ) ) ) ;
        assertThat( bean.getLastName(), is( equalTo( "Deb" ) ) ) ;
        assertThat( bean.getAge(), is( equalTo( 39 ) ) ) ;
    }
}
