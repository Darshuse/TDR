package profilemigration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.eastnets.application.BaseApp;
import com.eastnets.config.ConfigBean;
import com.eastnets.config.DBType;
import com.eastnets.domain.Pair;
import com.eastnets.domain.admin.Profile;
import com.eastnets.domain.admin.ProfileDetails;
import com.eastnets.domain.admin.User;
import com.eastnets.domain.viewer.MessageSearchTemplate;
import com.eastnets.service.ServiceLocator;


public class ProfileMigrationApp extends BaseApp {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3270177407242576225L;
	public static String what = "en.Reporting User profiles migration tool 3.1.0 ";
	static Logger log = Logger.getLogger(ProfileMigrationApp.class.getName());
	private List<User> userList;
	ServiceLocator serviceLocator;
	public void displayUsage(String value){

		System.out.printf("%s: missing or invalid paramter\n", value);
		System.out.println("Usage : \n");
		System.out.println("  -U\t\t: user name for database connection");
		System.out.println("  -P\t\t: password for databasae connection");
		System.out.println("  -IP\t\t: The IP address or server name for database connection, default is localhost.");
		System.out.println("  -dbtype\t: oracle or mssql database, default is oracle");
		System.out.println("  -port\t\t: database port, default 1521 for oracle and 1433 for mssql");
		System.out.println("  -dbname\t: database name");
		System.out.println("  -servicename\t: service name");
		
		
	}
	

	
	public boolean loadParameters(String[] args)  {
		
		log.debug("passing user paramter into application...");
		if(args.length==0){
			displayUsage("");
			return false;
		}
		ConfigBean configBean = new ConfigBean();
		try {


			// default config values for database connection
			configBean.setDatabaseType(DBType.ORACLE);
			configBean.setPortNumber("1521");
			configBean.setDatabaseName("");
			for (int i = 0; i < args.length; i++) {
				String value = args[i];
				if ("-U".equals(value)) {

					if (i++ < args.length) {
						configBean.setUsername(args[i]);
					} else {
						displayUsage(value);
						return false;
					}
				} else if ("-P".equals(value)) {

					if (i++ < args.length) {
						configBean.setPassword(args[i]);
					} else {
						displayUsage(value);
						return false;
					}
				} else if ("-IP".equals(value)) {

					if (i++ < args.length) {
						configBean.setServerName(args[i]);
					} else {
						displayUsage(value);
						return false;
					}
				} else if ("-dbname".equals(value)) {

					if (i++ < args.length) {
						configBean.setDatabaseName(args[i]);
					} else {
						displayUsage(value);
						return false;
					}
				} else if ("-servicename".equalsIgnoreCase(value)) {
					if (i++ < args.length) {
						configBean.setDbServiceName(args[i]);
					} else {
						displayUsage(value);
						return false;
					}
				}else if ("-dbtype".equals(value)) {

					if (i++ < args.length) {
						configBean
						.setDatabaseType(args[i].toLowerCase().equals(
								"mssql") ? DBType.MSSQL : DBType.ORACLE);
						if (args[i].toLowerCase().equals("mssql")
								&& configBean.getPortNumber().equals("1521"))
							configBean.setPortNumber("1433");
					} else {
						displayUsage(value);
						return false;
					}
				} else if ("-port".equals(value)) {

					if (i++ < args.length) {
						configBean.setPortNumber(args[i]);
					} else {
						displayUsage(value);
						return false;
					}
				} else {
					displayUsage(value);
					return false;
				}
			}
			String validateDbConn=checkDBConnection(configBean);
			if(!validateDbConn.equals("VALID")){
				displayUsage(validateDbConn);
				return false;
			}
			init(configBean);
		} catch (Exception ex) {
			displayUsage("");
			return false;
		}
		log.debug("passing paramter into application successfully");

		return true;
	}
	
	public String checkDBConnection(ConfigBean cnfgBean) {
		log.debug("checking DB paramters...");
		if (cnfgBean.getServerName() == null
				|| cnfgBean.getServerName().length() <= 0)
			return "-IP";
		if (cnfgBean.getUsername() == null
				|| cnfgBean.getUsername().length() <= 0)
			return "-U";
		if (cnfgBean.getPassword() == null
				|| cnfgBean.getPassword().length() <= 0)
			return "-P";
		if (cnfgBean.getPortNumber() == null
				|| cnfgBean.getPortNumber().length() <= 0)
			return "-port";
		if (cnfgBean.getDatabaseName() == null || cnfgBean.getDatabaseName().length() <= 0){
			if(cnfgBean.getDbServiceName()== null ||cnfgBean.getDbServiceName().length() == 0){
				return "-dbname or -servicename";
			}
		}
			
		log.debug("DB paramters are vaild..");

		return "VALID";
	}
	
	public static void main(String[] args) throws Exception {
		ProfileMigrationApp profileMigrationApp=new  ProfileMigrationApp();
		if (profileMigrationApp.loadParameters(args) == false) {
			return;
		}
		profileMigrationApp.init();
		
		//1. group all profile users into sets based on their roles.
		HashMap<Long, ArrayList<RolesUsers>> profilesRoles=profileMigrationApp.getProfilesUsersRoles();
		//2.get the profiles details for the effected profile(profile that has to be updated).
		List<ProfileDetails> profilesDetailsList=profileMigrationApp.getProfilesDetails(profilesRoles);

		//3. do the update on both profile and user level.
		for(ProfileDetails profileDetails:profilesDetailsList ){
			profileMigrationApp.migrateProfile(profileDetails,profilesRoles);
		}
		
		profileMigrationApp.updateAuthenticationMethod();
	/*	
		for (Long key : profilesRoles.keySet()) {
			ArrayList<RolesUsers> list=profilesRoles.get(key);
			System.out.println("profile:"+key);
			for(RolesUsers rec:list){
				System.out.println(rec.getUserIds().size()+""+rec.getRolesStr());
			}
			
		}*/
	}
	public void migrateProfile(ProfileDetails profileDetails, HashMap<Long, ArrayList<RolesUsers>> profilesRoles) throws Exception{
	
		
		System.out.println(String.format("Start handling profile[%d:%s]",profileDetails.getProfile().getGroupId(),profileDetails.getName() ));
		String oldProfileName=profileDetails.getProfile().getName();
		long oldProfileId=profileDetails.getProfile().getGroupId();
		ArrayList<RolesUsers> profGroupedRolesList=profilesRoles.get(profileDetails.getProfile().getGroupId());
		
		if(profGroupedRolesList.size()==1){ //all users in the profile has same roles, so no need to create new profile.
			RolesUsers rolesUsers=profGroupedRolesList.get(0);
			System.out.println(String.format("\tAll users in profile [%d:%s] have same roles.",profileDetails.getProfile().getGroupId(),profileDetails.getName()));
			profileDetails.getProfile().setProfileRoles(rolesUsers.getPorfileRoles());
			serviceLocator.getAdminService().updateProfile("", profileDetails,null);
			System.out.println(String.format("\tAssign roles [%s] to profile  [%d:%s].",rolesUsers.getRolesStr(),profileDetails.getProfile().getGroupId(),profileDetails.getName()));
		}else{
			int i=0;
			for(RolesUsers rolesUser:profGroupedRolesList){
				i++;
				// 1. create new profiles
				profileDetails.getProfile().setProfileRoles(rolesUser.getPorfileRoles());
				
				// 2. change profile name
				profileDetails.getProfile().setName(oldProfileName+"_"+i);
				// 3. insert the new profile into db
				serviceLocator.getAdminService().addProfile("", profileDetails);
				
				// 4. get the new profile id.
				Profile newProfile=serviceLocator.getAdminService().getProfile("", profileDetails.getProfile().getName());
				System.out.println(String.format("\tCreate new profile [%d:%s] with roles[%s].",newProfile.getGroupId(),newProfile.getName(),rolesUser.getRolesStr()));
				//5. move users to their own new profile
				for(Long userId :rolesUser.getUserIds()){
					User user=serviceLocator.getAdminService().getUser(userId);
					user.setProfile(newProfile);
					serviceLocator.getAdminService().setUserGroupId("", user.getUserId(), newProfile.getGroupId());
					System.out.println(String.format("\tAssign user [%s] to profile  [%d:%s].",user.getUserName(),newProfile.getGroupId(),newProfile.getName()));
				}
				
				if(rolesUser.getRolesStr().contains("SIDE_VIEWER")){
					//6. update Message Search Templates profile id
					List<MessageSearchTemplate > templates=serviceLocator.getViewerService().getMsgSearchTemplates(oldProfileId);

					for(MessageSearchTemplate template :templates){

						User user=serviceLocator.getAdminService().getUser(template.getUserId());
						template.setName(template.getName()+"_"+i);
						template.setProfileId(newProfile.getGroupId());
						template.setCreatedBy(user);
						serviceLocator.getViewerService().addNewMsgSearchTemplate(template);
					}
				}
				//serviceLocator.getViewerService().changeMessageSearchTemplateProfileId(oldProfileId, newProfile.getGroupId());
			}
			
			
		}
		System.out.println(String.format("Finish handling profile[%d:%s]",oldProfileId,oldProfileName));
		
	}
	/*
	 * Group all profile users into sets based on their roles
	 * 
	 */
	public HashMap<Long, ArrayList<RolesUsers>> getProfilesUsersRoles(){
		// get 3.1 profile's roles 
		List<Pair<String, String>> newRolesPairs=serviceLocator.getAdminService().getRoles("");
		ArrayList<String> profileRolesList=new ArrayList<String>();
		for(Pair<String, String> pair:newRolesPairs){
			profileRolesList.add(pair.getKey());
		}

		
		
		HashMap<Long, ArrayList<RolesUsers>> profilesRoles=new HashMap<Long, ArrayList<RolesUsers>>();
		List<String> roles;
		for(User user:userList){
			long profileId=user.getProfile().getGroupId();
			if(!user.isDatabaseUserOld()){ //non database has no roles
				roles =getUserProfileRole(profileId);
			}else{
				roles =serviceLocator.getAdminService().getUserRoles(user.getUserName());
			}
				
			
			
			
			String roleStr=manipulateRoles(profileRolesList,roles);
			
			
			
			if(profilesRoles.containsKey(profileId)){
				ArrayList<RolesUsers> profileRecList=profilesRoles.get(profileId);
				boolean founded=false;
				for(RolesUsers rec:profileRecList){
					if(rec.getRolesStr().equals(roleStr)){
						rec.add(user.getUserId());
						founded=true;
						break;
					}
				}
				if(!founded){
					RolesUsers rec=new RolesUsers();
					rec.add(user.getUserId());
					rec.setRolesStr(roleStr);
					profileRecList.add(rec);
				}
			}else{
				RolesUsers rec=new RolesUsers();
				rec.add(user.getUserId());
				rec.setRolesStr(roleStr);
				ArrayList<RolesUsers> newroles=new ArrayList<RolesUsers>();
				newroles.add(rec);
				profilesRoles.put(profileId, newroles);
			}
			
		}
		
		return profilesRoles;
	}
	/*
	 * This method create the roles string as following:
	 * 1. create new list of roles, that will be moved to profile in rep3.1
	 * 2. sort these roles
	 * 3. merge the roles in the new list to single string with # separator
	 */
	private  String manipulateRoles(List< String> profileRoles,List<String>roles){

		ArrayList<String> newList=new ArrayList<String>();
		for (String role:roles  ) {
			if(profileRoles.contains(role)){
				newList.add(role);
				if(role.equalsIgnoreCase("SIDE_OPER")){
					newList.add("SIDE_ARRCHIVE_ERSTORE");
				}
			}
		}

		Collections.sort(newList);

		String roleStr="";
		for(String role:newList ){
			roleStr+="#"+role;
		}
		return roleStr;
	}
	
	
	private List<ProfileDetails> getProfilesDetails(HashMap<Long, ArrayList<RolesUsers>> profilesRoles){
		
		List<Profile> profiles = serviceLocator.getAdminService().getProfiles("");
		//get effected profiles
		List<Profile> effectedProfiles = new ArrayList<Profile>();
		
		
		for (Profile profile : profiles) {
			if(profilesRoles.containsKey(profile.getGroupId())){
				effectedProfiles.add(profile);
			}
		}
		
		List<ProfileDetails> effectedProfilesDetails =serviceLocator.getSecurityService().getProfileDetails("", effectedProfiles);
		return effectedProfilesDetails;
	}
	
	private List<String> getUserProfileRole(Long profileId){
		List<String> roles=new ArrayList<String>();
		List<Integer> groupAccessProgramIds = serviceLocator.getAdminService().getGroupAccessProgramIds(profileId);
		
		for (int id:groupAccessProgramIds){
			if(id==0){
				roles.add("SIDE_WATCHDOG");
			}else if(id==1){
				roles.add("SIDE_REPORT");
			}else if(id==2){
				roles.add("SIDE_VIEWER");
			}else if(id==4){
				roles.add("SIDE_EVENT");
			}else if(id==5){
				roles.add("SIDE_DASHBOARD");
			}
		}
		
		return roles;
	}
	
	public void init() {
		serviceLocator = getServiceLocater();
		// get all users
		userList = serviceLocator.getAdminService().getUsers("");
	}
	
	public void updateAuthenticationMethod() {

		serviceLocator.getAdminService().changeAuthMethodForNonDbUsers(1L);//1: means LDAP
	}
}