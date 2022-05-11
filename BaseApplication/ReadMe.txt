to make a project that use the service locator and use our reporting services you need to do :
	* Create a project and create a class that is inherited from the class BaseApp.
	* the BaseApp class has the method getServiceLocater( ConfigBean ) which gives back the service locator 
		- the ConfigBean passed should be filled by your application ( mostly by command line arguments )
		- the ConfigBean is only used for the first call only as we cash the serviceLocatior so you may pass null in the calls other than the first call
 	and you are done !!!
 	
 	* to link the jar dependencies, use all jars linked with the BaseApplication project, and for your work add any needed jar that you may need.
 
 
################## Important note ##################
 * in the enReportingDAO project we have some DAOs that use AutoWire, spring has some issue AutoWiring classes from jar files that was not exported 
   using the option [Add Directory Entries] checked when exporting the jar, but this option is not available when we export a runnable jar, 
   so spring people says that they will not fix this issue ( i have seen some ticket in their bug tracer that was closed as wont fix regarding this issue )
   -- so as a workaround you need to export the project enReportingDAO as a jar file with the option [Add Directory Entries] checked, 
      and then link that jar file into your project
   