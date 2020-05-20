package in.trueowner.trueowner;

public class My_Offers_Get_Set {

    private String BidderNumber, BidderName, SellerName, OfferedPrice, OfferStatus, SellerID, ProductID, BidderID;
    private Boolean VerificationStatus, ReviewStatus, EditOfferStatus, FinalAcceptStatus, PayDeliveryStatus;


    public My_Offers_Get_Set() {
    }

    public My_Offers_Get_Set(String BidderNumber, String BidderName, String SellerName, String OfferedPrice, String OfferStatus, Boolean VerificationStatus, Boolean ReviewStatus, Boolean EditOfferStatus, Boolean FinalAcceptStatus, Boolean PayDeliveryStatus, String SellerID, String ProductID, String BidderID) {
        this.BidderNumber = BidderNumber;
        this.BidderName = BidderName;
        this.SellerName = SellerName;
        this.OfferedPrice = OfferedPrice;
        this.OfferStatus = OfferStatus;
        this.VerificationStatus = VerificationStatus;
        this.ReviewStatus = ReviewStatus;
        this.EditOfferStatus = EditOfferStatus;
        this.FinalAcceptStatus = FinalAcceptStatus;
        this.PayDeliveryStatus = PayDeliveryStatus;
        this.SellerID = SellerID;
        this.ProductID = ProductID;
        this.BidderID = BidderID;

    }

    public String getBidderNumber() {
        return BidderNumber;
    }

    public String getBidderName() {
        return BidderName;
    }

    public String getSellerName() { return SellerName; }

    public String getOfferedPrice() {
        return OfferedPrice;
    }

    public String getStatus() {
        return OfferStatus;
    }

    public String getSellerID() {return SellerID; }

    public String getProductID() { return ProductID; }

    public String getBidderID() { return BidderID; }

    public Boolean getVerificationStatus() { return VerificationStatus; }

    public Boolean getReviewStatus() { return ReviewStatus; }

    public Boolean getEditOfferStatus() { return EditOfferStatus; }

    public Boolean getFinalAcceptStatus() { return FinalAcceptStatus; }

    public Boolean getPayDeliveryStatus() { return PayDeliveryStatus; }
}
