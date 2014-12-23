package com.example.myapp.parse;


import com.google.api.client.util.Key;

public class ResponseDataWrapper {
    @Key
    private ResponseData responseData;
  /*  @Key
    private Object responseDetails;
    @Key
    private int responseStatus;*/

    public ResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(ResponseData responseData) {
        this.responseData = responseData;
    }

  /*  public Object getResponseDetails() {
        return responseDetails;
    }

    public void setResponseDetails(Object responseDetails) {
        this.responseDetails = responseDetails;
    }

    public int getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(int responseStatus) {
        this.responseStatus = responseStatus;
    }*/
}
