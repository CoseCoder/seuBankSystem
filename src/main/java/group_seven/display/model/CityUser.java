package group_seven.display.model;

public class CityUser {
	private String city;
	private Long cardAmount;
	private String xData,yData;//xdata是纬度，ydata是经度
	public String getCity() {
		return city;
	}

	public Long getCardAmount() {
		return cardAmount;
	}

	public void setCardAmount(Long cardAmount) {
		this.cardAmount = cardAmount;
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

	public void setCity(String city) {
		this.city = city;
	}

}
