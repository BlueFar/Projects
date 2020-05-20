package in.trueowner.trueowner;

public class AdminDropBuyer_List_Get_Set {

    private String  BuyerName, Imei1, productID;

    public AdminDropBuyer_List_Get_Set() {
    }

    public AdminDropBuyer_List_Get_Set(String BuyerName, String Imei1, String productID) {
        this.BuyerName = BuyerName;
        this.Imei1 = Imei1;
        this.productID = productID;

    }

    public String getBuyerName() { return BuyerName; }

    public String getImei1() { return Imei1; }

    public String getProductID() { return productID; }
}