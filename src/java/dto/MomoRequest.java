/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;

/**
 *
 * @author ptd
 */
public class MomoRequest implements Serializable{
      private String partnerCode;
      
      private String partnerName; // option
      
      private String storeId;   // option
      
      private String requestId; // == orderId
      
      private String requestType;
      
      private String ipnUrl;
      
      private String redirectUrl;
      
      private String orderId;
      
      private String orderInfo;
      
      private String extraData; // codebase64 - or ""
      
      private boolean autoCapture; // default is true. if false transaction is not auto capture
      
      private String lang; // vi - en
      
      private String signature;
      
      private long amount;

    public MomoRequest() {
    }

    public MomoRequest(String partnerCode, String partnerName, String storeId, String requestId, String requestType, String ipnUrl, String redirectUrl, String orderId, String orderInfo, String extraData, boolean autoCapture, String lang, String signature, long amount) {
        this.partnerCode = partnerCode;
        this.partnerName = partnerName;
        this.storeId = storeId;
        this.requestId = requestId;
        this.requestType = requestType;
        this.ipnUrl = ipnUrl;
        this.redirectUrl = redirectUrl;
        this.orderId = orderId;
        this.orderInfo = orderInfo;
        this.extraData = extraData;
        this.autoCapture = autoCapture;
        this.lang = lang;
        this.signature = signature;
        this.amount = amount;
    }

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getIpnUrl() {
        return ipnUrl;
    }

    public void setIpnUrl(String ipnUrl) {
        this.ipnUrl = ipnUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public boolean isAutoCapture() {
        return autoCapture;
    }

    public void setAutoCapture(boolean autoCapture) {
        this.autoCapture = autoCapture;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
      
      
      
}