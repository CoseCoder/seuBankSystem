package group_seven.display.model;

public class BillInfo {
    String province;
    Double money;
    Integer billtype;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Integer getBillType() {
        return billtype;
    }

    public void setBillType(Integer billType) {
        billtype = billType;
    }
}
