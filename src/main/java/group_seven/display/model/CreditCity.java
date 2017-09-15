package group_seven.display.model;

public class CreditCity {
    private String City;
    private Integer amount;
    private String xData,yData;//xdata是纬度，ydata是经度

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getxData() {
        return xData;
    }

    public void setxData(String xData) {
        this.xData = xData;
    }

    public String getyData() {
        return yData;
    }

    public void setyData(String yData) {
        this.yData = yData;
    }
}
