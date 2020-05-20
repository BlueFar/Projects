package in.trueowner.trueowner;

public class MyOrders_Get_Set {

    private String SellerID, BidderID, OrderDate, OrderStatus, ProductName, ProductID, BidderAdminID, SellerAdminID;
    private long OfferedPrice,DeliveryFee, RegisterFee;
    private String Image1;

    public MyOrders_Get_Set() {
    }

    public MyOrders_Get_Set(String SellerID, String BidderID, String OrderDate, String OrderStatus, String ProductName, String ProductID,
                            long OfferedPrice, long DeliveryFee, long RegisterFee, String Image1, String BidderAdminID, String SellerAdminID) {

        this.SellerID = SellerID;
        this.BidderID = BidderID;
        this.OrderDate = OrderDate;
        this.OrderStatus = OrderStatus;
        this.ProductID = ProductID;
        this.ProductName = ProductName;
         this.OfferedPrice = OfferedPrice;
        this.DeliveryFee = DeliveryFee;
        this.RegisterFee = RegisterFee;
        this.Image1 = Image1;
        this.BidderAdminID = BidderAdminID;
        this.SellerAdminID = SellerAdminID;
    }

    public String getSellerID() { return SellerID; }

    public String getBidderID() { return BidderID; }

    public String getOrderDate() { return OrderDate; }

    public String getOrderStatus() { return OrderStatus; }

    public String getProductName() { return ProductName; }

    public String getProductID() { return ProductID; }

    public long getDeliveryFee() { return DeliveryFee; }

    public long getPrice() {
        return OfferedPrice;
    }

    public long getRegisterFee() { return RegisterFee; }

    public String getImage1() { return Image1; }

    public String getBidderAdminID() { return BidderAdminID; }

    public String getSellerAdminID() { return SellerAdminID; }
}
