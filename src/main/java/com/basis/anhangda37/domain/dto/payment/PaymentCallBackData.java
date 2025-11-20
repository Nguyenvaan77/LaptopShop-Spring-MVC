package com.basis.anhangda37.domain.dto.payment;

import java.util.Map;


public class PaymentCallBackData {
    private String vnp_TxnRef;
    private String ipAddress;
    private Map<String, String> params;

    public PaymentCallBackData() {
        
    }
    
    public PaymentCallBackData(String ipAddress, Map<String, String> params, String vnp_TxnRef) {
        this.ipAddress = ipAddress;
        this.params = params;
        this.vnp_TxnRef = vnp_TxnRef;
    }

    public String getIpAddress() {
        return ipAddress;
    }
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    public Map<String, String> getParams() {
        return params;
    }
    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getVnp_TxnRef() {
        return vnp_TxnRef;
    }

    public void setVnp_TxnRef(String vnp_TxnRef) {
        this.vnp_TxnRef = vnp_TxnRef;
    }
    

    
}
