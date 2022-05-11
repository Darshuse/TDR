package com.eastnets.test.admin;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.eastnets.test.admin.testcase.BICsTest;
import com.eastnets.test.admin.testcase.LoaderTest;
import com.eastnets.test.admin.testcase.ProfileTest;
import com.eastnets.test.admin.testcase.UnitsTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ ProfileTest.class, UnitsTest.class,BICsTest.class, LoaderTest.class })
public class AdminTestsSuite {

}
