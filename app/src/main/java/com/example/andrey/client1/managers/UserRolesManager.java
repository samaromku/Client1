package com.example.andrey.client1.managers;

import com.example.andrey.client1.entities.UserRole;

import java.util.ArrayList;
import java.util.List;

public class UserRolesManager {
    UserRole userRole;
    UserRole createNewUserRole;
    UserRole updateUserRole;
    List<UserRole> userRoles;
    public static final UserRolesManager INSTANCE = new UserRolesManager();

    public UserRole getUpdateUserRole() {
        return updateUserRole;
    }

    public void setUpdateUserRole(UserRole updateUserRole) {
        this.updateUserRole = updateUserRole;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void addUserRole(UserRole userRole){
        userRoles.add(userRole);
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public UserRole getRoleById(int roleId){
        for (UserRole u: userRoles){
            if(u.getId()==roleId)
                return u;
        }
        return null;
    }

    public void updateUserRole(UserRole userRole){
        for(UserRole u : userRoles){
            if(u.getId()==userRole.getId()){
                userRoles.remove(userRoles.indexOf(u));
            }
        }
        userRoles.add(userRole);
    }

    public void setCreateNewUserRole(UserRole createNewUserRole) {
        this.createNewUserRole = createNewUserRole;
    }

    public UserRole getCreateNewUserRole() {
        return createNewUserRole;
    }

    public void removeAll(){
        if(userRoles.size()>0){
            userRoles.clear();
        }
    }

    public int getMaxId(){
        int max = 0;
        for(UserRole u:userRoles){
            if(u.getId()>max)
                max = u.getId();
        }
        return max;
    }

    public UserRole getRoleByUserId(int userId){
        for (UserRole u: userRoles){
            if(u.getUserId()==userId)
                return u;
        }
        return null;
    }

    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    private UserRolesManager(){
        userRoles  = new ArrayList<>();
    }
}
