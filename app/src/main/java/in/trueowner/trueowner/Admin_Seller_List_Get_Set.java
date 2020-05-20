package in.trueowner.trueowner;

public class Admin_Seller_List_Get_Set {

    private String  SellerName, Imei1, SellerID;

    public Admin_Seller_List_Get_Set() {
    }

    public Admin_Seller_List_Get_Set(String SellererName, String Imei1, String SellerID) {
        this.SellerName = SellerName;
        this.Imei1 = Imei1;
        this.SellerID = SellerID;

    }

    public String getSellerName() { return SellerName; }

    public String getImei1() { return Imei1; }

    public String getUserID() { return SellerID; }
}