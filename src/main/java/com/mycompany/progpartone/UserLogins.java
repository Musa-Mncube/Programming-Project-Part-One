
package com.mycompany.progpartone;

/**
 *
 * @author Student
 */
public class UserLogins {
    
    private String name;
    private String surname;
    public String userName;
    public String phoneNumber;
    public String Password;
    
    public UserLogins(String userName, String phoneNumber, String Password){
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.Password = Password;
    
}

             public boolean CheckuserName(String userName){
        if(userName.contains("_") && userName.length()<= 5){
            return true;
        }else{
            return false;
        }
        }
     
  
public boolean checkPhoneNumber(String phoneNumber){
        return phoneNumber.startsWith("+27") && phoneNumber.length() == 12;
}

public boolean checkPassword(String Password){
    //at least 8 characters long, one uppercase, letter, at least one digit
  boolean hasLength = Password.length()>= 8;
  boolean hasCapital = Password.matches(".*[A-Z].*");
  boolean hasNumber = Password.matches(".*[0-9].*");
  boolean hasSpecial = Password.matches(".*[^A-Za-z)-9].*");
  return hasLength && hasCapital && hasNumber && hasSpecial;
  
    }
    
public String registerUser(String userName, String phoneNumber, String Password){
    if (!CheckuserName(userName)){
        return "Username is not correctly formatted, please ensure that your username contains an underscore and is not more than five characters in length.";
    }
        if (!checkPhoneNumber(phoneNumber)){
            return "The phone number you entered is not correctly formatted, please ensure your phone number starts with +27 and is twelve characters long.";
        }
        if (!checkPassword(Password)){
            return "Your password has been formatted incorrectly, please ensure your password contains at least eight characters, a capital letter, a number and a special character.";
        }
    return "Registration successful.";
}

public boolean UserLogin(String userName, String phoneNumber, String Password){
    //Username.equals(Username) and Password.equals(Password) always
    return this.userName.equals(userName)&& this.Password.equals(Password);
}

public String returnUserLoginStatus(Boolean LoginSuccessful, boolean LoginUnsuccessful){
    if(LoginSuccessful){
        return "Welcome back " + name + surname + " it is great to see you. ";
    }else if(LoginUnsuccessful){
        return "Username or password incorrect";
    }else{
        return "Login Unsuccessful";
    }
}
}


