package in.trueowner.trueowner;

public class AdminRegisterList_Get_Set {

    private String  FullName, MobileNumber, UserID;

    public AdminRegisterList_Get_Set() {
    }

    public AdminRegisterList_Get_Set(String FullName, String MobileNumber, String UserID) {
        this.FullName = FullName;
        this.MobileNumber = MobileNumber;
        this.UserID = UserID;

    }

    public String getFullName() { return FullName; }

    public String getMobileNumber() { return MobileNumber; }


    public String getUserID() { return UserID; }
}