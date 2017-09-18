package group_seven.display.model;

//交易类型（支付工具）
public class TradeType {
	private String tradetype;
	private Long number;

	/**
	 * @return the number
	 */
	public Long getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(Long number) {
		this.number = number;
	}

	/**
	 * @return the tradetype
	 */
	public String getTradetype() {
		return tradetype;
	}

	/**
	 * @param tradetype the tradetype to set
	 */
	public void setTradetype(String tradetype) {
		this.tradetype = tradetype;
	}
	
}
