package in.trueowner.trueowner;

public class Cart_Get_Set {

    private String ProductName,ProductID,SellerNumber, SellerID, BidderID, OfferStatus, SellerName, SellerCity, SellerState;
    private long  OfferedPrice, AcceptablePrice, AskedPrice;
    private String Image1;
    private Boolean VerificationStatus, ReviewStatus,EditOfferStatus, FinalAcceptStatus, PayDeliveryStatus;

    public Cart_Get_Set() {
    }

    public Cart_Get_Set( String SellerNumber, String ProductName, String ProductID, String SellerID, long OfferedPrice, long AcceptablePrice, long AskedPrice,
                        String Image1, String BidderID, String OfferStatus, Boolean VerificationStatus, Boolean ReviewStatus,Boolean EditOfferStatus, Boolean FinalAcceptStatus, Boolean PayDeliveryStatus, String SellerName, String SellerCity, String SellerState) {
        this.SellerNumber = SellerNumber;
        this.ProductName = ProductName;
        this.ProductID = ProductID;
        this.SellerID = SellerID;
        this.Image1 = Image1;
        this.BidderID = BidderID;
         this.OfferStatus = OfferStatus;
        this.VerificationStatus = VerificationStatus;
        this.ReviewStatus = ReviewStatus;
        this.EditOfferStatus = EditOfferStatus;
        this.FinalAcceptStatus = FinalAcceptStatus;
        this.PayDeliveryStatus = PayDeliveryStatus;
        this.OfferedPrice = OfferedPrice;
        this.AcceptablePrice = AcceptablePrice;
        this.AskedPrice = AskedPrice;
        this.SellerName = SellerName;
        this.SellerCity = SellerCity;
        this.SellerState = SellerState;
    }


    public String getName() {
        return ProductName;
    }


    public String getProductID() { return ProductID; }

    public String getSellerID() { return SellerID; }

    public String getImage1() { return Image1; }

    public String getStatus() { return OfferStatus; }

    public long getMyOffer() {  return OfferedPrice; }

    public long getAskedPrice() {return AskedPrice; }

    public long getAcceptablePrice() { return AcceptablePrice; }

    public String getProductName() { return ProductName; }

    public String getSellerNumber() { return SellerNumber; }

    public String getBidderID() { return BidderID; }

    public String getSellerName() { return SellerName; }

    public String getSellerCity() { return SellerCity; }

    public String getSellerState() { return SellerState; }

    public Boolean getVerificationStatus() { return VerificationStatus; }

    public Boolean getReviewStatus() { return ReviewStatus; }

    public Boolean getEditOfferStatus() { return EditOfferStatus; }

    public Boolean getFinalAcceptStatus() { return FinalAcceptStatus; }

    public Boolean getPayDeliveryStatus() { return PayDeliveryStatus; }

}
