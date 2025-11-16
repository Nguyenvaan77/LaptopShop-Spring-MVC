package com.basis.anhangda37.domain.enums;

public enum PaymentParam {

    // Core fields gửi sang VNPAY
    VNP_VERSION("vnp_Version"),
    VNP_COMMAND("vnp_Command"),
    VNP_TMNCODE("vnp_TmnCode"),
    VNP_AMOUNT("vnp_Amount"),
    VNP_BANKCODE("vnp_BankCode"),
    VNP_CREATDATE("vnp_CreateDate"),
    VNP_CURRCODE("vnp_CurrCode"),
    VNP_IPADDR("vnp_IpAddr"),
    VNP_LOCALE("vnp_Locale"),
    VNP_ORDERINFO("vnp_OrderInfo"),
    VNP_ORDERTYPE("vnp_OrderType"),
    VNP_RETURNURL("vnp_ReturnUrl"),
    VNP_TXNREF("vnp_TxnRef"),
    VNP_EXPIREDATE("vnp_ExpireDate"),

    // Fields trả về từ VNPAY (IPN / Return URL)
    VNP_PAYDATE("vnp_PayDate"),
    VNP_TRANSACTIONNO("vnp_TransactionNo"),
    VNP_TRANSACTIONSTATUS("vnp_TransactionStatus"),
    VNP_RESPONSECODE("vnp_ResponseCode"),
    VNP_CARDTYPE("vnp_CardType"),
    VNP_BANKTRANNO("vnp_BankTranNo"),

    // Security fields
    VNP_SECUREHASH("vnp_SecureHash"),
    VNP_SECUREHASHTYPE("vnp_SecureHashType"),

    // Billing info (tùy chọn)
    VNP_BILL_MOBILE("vnp_Bill_Mobile"),
    VNP_BILL_EMAIL("vnp_Bill_Email"),
    VNP_BILL_FIRSTNAME("vnp_Bill_FirstName"),
    VNP_BILL_LASTNAME("vnp_Bill_LastName"),
    VNP_BILL_ADDRESS("vnp_Bill_Address"),
    VNP_BILL_CITY("vnp_Bill_City"),
    VNP_BILL_COUNTRY("vnp_Bill_Country"),
    VNP_BILL_STATE("vnp_Bill_State"),

    // Invoice info (tùy chọn)
    VNP_INV_CUSTOMER("vnp_Inv_Customer"),
    VNP_INV_COMPANY("vnp_Inv_Company"),
    VNP_INV_ADDRESS("vnp_Inv_Address"),
    VNP_INV_TAXCODE("vnp_Inv_Taxcode"),
    VNP_INV_TYPE("vnp_Inv_Type"),
    VNP_INV_PHONE("vnp_Inv_Phone"),
    VNP_INV_EMAIL("vnp_Inv_Email");

    private final String value;

    PaymentParam(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    // Helper: lookup từ string → enum
    public static PaymentParam fromValue(String value) {
        for (PaymentParam p : PaymentParam.values()) {
            if (p.value.equals(value)) {
                return p;
            }
        }
        return null;
    }
}
