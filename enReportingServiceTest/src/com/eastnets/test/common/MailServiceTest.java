package com.eastnets.test.common;

import org.junit.Test;

import com.eastnets.test.BaseTest;

public class MailServiceTest extends BaseTest {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2712110679100150347L;

	@Test
    public void testSendMail(){

    	this.getServiceLocater().getMailService().sendMail("Important notice", "SAlababneh@eastnets.com", "Dieeeee", null, false);

    }
}
