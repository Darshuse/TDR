package profilemigration;

import java.util.ArrayList;
import java.util.List;

/*
 * this class will group user ids that have same roles
 * roles will be represented as single string with # separator like: SIDE_REPORT#SIDE_WATCHDOG 
 */
public class RolesUsers {
	ArrayList<Long> userIds = new ArrayList<Long>();
	String rolesStr = "";

	public ArrayList<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(ArrayList<Long> userIds) {
		this.userIds = userIds;
	}


	public String getRolesStr() {
		return rolesStr;
	}

	public void setRolesStr(String rolesStr) {
		this.rolesStr = rolesStr;
	}

	public void add(long userId) {
		userIds.add(userId);
	}
	public List<String> getPorfileRoles(){
		List<String> profileRoles=new ArrayList<String>();
		
		String [] roles= rolesStr.split("#");
		for(String role:roles){
			if(!role.trim().isEmpty()){
				profileRoles.add(role.trim());
			}
			//System.out.println(role);
		}
		return profileRoles;
	}
}
