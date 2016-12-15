package com.dreamer.service.pay.param;


/**
 * 网页支付提交Post数据给到API之后，API会返回XML格式的数据，这个类用来装这些数据
 */
public class UnifiedOrderResData extends PayResBaseData{
	
	
	public boolean paySuccess(){
		return businessSuccess();
	}
	
	


    private String device_info = "";

    //业务返回的具体数据（以下字段在return_code 和result_code 都为SUCCESS 的时候有返回）
    private String trade_type = "";
    private String prepay_id = "";
    private String code_url = "";

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }


    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

	public String getPrepay_id() {
		return prepay_id;
	}

	public void setPrepay_id(String prepay_id) {
		this.prepay_id = prepay_id;
	}

	public String getCode_url() {
		return code_url;
	}

	public void setCode_url(String code_url) {
		this.code_url = code_url;
	}
    

}
