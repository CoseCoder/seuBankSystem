package group_seven.display.model;

public class CreditAll {
    private String BillNumber,Account,BillDate,BillExInfo, BillSite,ExReason;
    private Double Money;

    public String getBillNumber() {
        return BillNumber;
    }

    public void setBillNumber(String billNumber) {
        BillNumber = billNumber;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public String getBillDate() {
        return BillDate;
    }

    public void setBillDate(String billDate) {
        BillDate = billDate;
    }

    public String getBillExInfo() {
        return BillExInfo;
    }

    public void setBillExInfo(String billExInfo) {
        BillExInfo = billExInfo;
    }

    public String getBillSite() {
        return BillSite;
    }

    public void setBillSite(String billSite) {
        BillSite = billSite;
    }

    public String getExReason() {
        return ExReason;
    }

    public void setExReason(String exReason) {
        ExReason = exReason;
    }

    public Double getMoney() {
        return Money;
    }

    public void setMoney(Double money) {
        Money = money;
    }
}
