package pmssMpng;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.bocom.api.DefaultBocomClient;
import com.course.app.webadmin.upms.model.pmms.request.FeeSplitInfo;
import com.course.app.webadmin.upms.model.pmms.request.LocationInfo;
import com.course.app.webadmin.upms.model.pmms.request.MPNG210001RequestV1;
import com.course.app.webadmin.upms.model.pmms.request.ReqHead;
import com.course.app.webadmin.upms.model.pmms.request.RequireFields;
import com.course.app.webadmin.upms.model.pmms.request.RoyaltyInfo;
import com.course.app.webadmin.upms.model.pmms.request.TermInfo;
import com.course.app.webadmin.upms.model.pmms.response.MPNG210001ResponseV1;

/**
 * <pre>
 * 主扫聚合码接口@MPNG-CIPP
 * </pre>
 * <p>
 * </p>
 */
public class MPNG210001V1Test {

    public static final String MY_PRIVATE_KEY =
        "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCER3xnjjYb4na9yf49wJIE/ykXuk9q4QKYJk/+LQuVUmf3HYUmVvngB2jwrMWr0x+Asg3XFyoctmZBDci3mgElQW6u1jNBUEUqbU+9oHT496D1g7KRVZZOWqaSZmFkPYf6Kr+uZgUtQEHeKiTuG3wIR2COSzuEsBvW8Q0RobHiCXXVmKpfpkB2cktwHzfu4sgTNrnvru5EDRFUMk72sMa5M9gasmY8XIVF711LWFreqVTKZPTuMTTIbZuVUd+vADWB7Y48IIEEsdIbbH8qwnTrEIvvWEJkhRYMpPvioLzqlepXZ8xNPD3Hu/SxHRRS+rQHiQvIpkqUK0JxOG+JpD8NAgMBAAECggEAczzrzjOARA6or3K3wQhwrVW1cOxon43i+rX13lNT0gR2ejwHcX04Nsl9zJ5XnvdQbIexsv6FO3pT3rypbfoGoXQ9fqcxA+dwOiP84hBVl0fzu6+98wsC5MPodYYntnI8cd6oGsDaJa/jMi64+Y6MIZI9NfmoolHiDwD1B7U8CNRAZR4XSTd22kmaF222cipgjpxrJozsJWSlwXsaw6ZEYsSraP76ZU1GiKZPKayLxQtTmHmqL/1SgVb2gBnW2FpTTdeanxjhnvE1Ikg28bfZPpA8c0jItjNpFvmyaf/3C2pKJBYKkAuyQSjMexS6anBX4G4LRfcDzrJzY12eHWrb6QKBgQC5iOg9JqxOmDCBN4I7hd3ax9bFPKQe7NO1Hs2cgyOCp3Ph/yK8/OIlGSBDHVlXPOpq80OwmOGU5AXJTBRpgDLC7gF9gphd9l+sIkHtQ5+mWld+jr7qFlW95y+H51PDk0nSg2GIkyID3t3G0NBooDM/XaXyndljBOQwMVpVBdqAWwKBgQC2hKwRCb2C6ZAssw8Cq5LWR2psFONbqAmA6aO9GgFKF0mBsyq0sstz/nE/XBURc+zOnYr72WFMMaoVkNPTxwqXNcHu4vbyepfBSWBDSJxndlMKXcld5+GZCyl9nUFLHGjYbNUsMgahaR5l9eNiMbHzKHdRNw7Trf2gLnvWlH/atwKBgHaP3pj1VbcD2js41ahj61obyktQbTC19rQPcyVJSC/+AjexaumKUJmUbOF2p9jvvH2L5v27NKyI6omwbHreyZF3tswV4HhrMMg3PBn61NLheHgPkEDW9QPd8KnQUd7xCIPWIUW9gEco7GBYoFW7ygP8PEfGR1zXi1qgrzEWBINxAoGBAIe3h6rzrzOVMVvLmbHGmt0zJXNQ5O7WuJpfIR4QJD1Pq50B+RN8j8Skwtj2Q8RCiyfoBdzX+32IwIxgsOKc8scVdiuIIVCU0Bst0KK3b3WPipf+vSlSr3H51tRNBqwvZ9bmtmyyljOX0r7GZFoz0bqZsH93jVLh4eImLneU3K1pAoGAMUCNCHIpMSd+d5ewR9VxXY1DjGHvIC98EdsjL0S7QcVrWb9mh6Br5G6zvlIJcMhIaQm3C1ETLaHG+K9XRrA+HcJo1ApeMT7MgBmIC+cG1aepZREDruNI3G8r4bDS6pHSDKk+Iu8shpEUurxZCzjBX2n5OeMJx+xDgYUq5H4Bn5I=";
    public static final String APIGW_PUBLIC_KEY =
        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxSJrF8T/5rKB4NnwMjIxUer+ELf1PQXO2GSdZ/fvuQCclOR9tBlNWL4jFOftebeL+bvMVOJ+JHm/aSes1AN8YNIDGiFUpF6aDkSCaLynDdjK/mQTWhSNa2fO0GGO+ywOBTdYUjVjVtzJ48bbyG3NSylf1EdnBWnMpFa8qpXJXR4ELpVpMkPDC+93HBAlxEgUjhcIJlP5VdKIiudsmhE2T07qtpIQSuE5hntXP6X6GKJReCk+yek2QJITvIBq3cHPw8KDsHHCs7MaR81KI3onJRWyqFtTfVYTiKsd9EcYSxv+Gx5MOF8B/P4iJCD8uzx0FrqoB3k5OYGcz4tXs+h+9wIDAQAB";
    public static final String BOCOM_PRD_KEY =
        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAihC4Qj8mlFxlO54qhUOmwWTKqhTCrfzZIeWFqS+QH9BxiChja6Za8oYVhyP+kiRiWxffBTzT25DPUfaDiQVCnlhdqlfOHHZ2Gp291pKrmF/E4WxLk50zYE1d69osw20GY0EVxhpml5bOxumxughpKwPKCrcYtN7MXAeWUEpl7AzqPNUgV+KlmE7TxB9tWcP6jeSn4/PQ47BfYmi2LI25UXfaFrUSNITkePoIYVZnP6FVpsC2grTdnPeUgfaCB3f/fPjEwRPrCHXCMopEWQQGIvqZuaZkFaQAd5XYfQnRytnF8nPofuRCDOHZYV2ldb5fVfsne/PuWmKBnBghebcw+QIDAQAB";
    public static final String APP_ID = "appoqjyz202305110001";
    // public static final String APIGW_URL_ADDRESS = "http://182.119.88.215:8090";//内网uat
    public static final String APIGW_URL_ADDRESS = "https://117.184.192.242:9443";// 外网uat
    // public static final String APIGW_URL_ADDRESS = "http://127.0.0.1:9501";
    // public static final String APIGW_URL_ADDRESS = "https://open.bankcomm.com";//生产地址

    public static void main(String[] args) {
        try {
            DefaultBocomClient client =
                new DefaultBocomClient(APP_ID, MY_PRIVATE_KEY, APIGW_PUBLIC_KEY);
            /**
             * 测试环境可以忽略SSL证书告警，生产环境不可忽略
             */
            client.ignoreSSLHostnameVerifier();
            MPNG210001RequestV1 request = new MPNG210001RequestV1();
            request.setServiceUrl(APIGW_URL_ADDRESS + "/api/pmssMpng/MPNG210001/v1");
            MPNG210001RequestV1.MPNG210001RequestV1Biz bizContent =
                new MPNG210001RequestV1.MPNG210001RequestV1Biz();
            ReqHead reqHead = new ReqHead();
            bizContent.setReqHead(reqHead);
            reqHead.setTransTime("20210316095211");
            reqHead.setVersion("V-1.0");
            MPNG210001RequestV1.MPNG210001RequestV1Biz.ReqBody reqBody =
                new MPNG210001RequestV1.MPNG210001RequestV1Biz.ReqBody();
            bizContent.setReqBody(reqBody);
            reqBody.setValidPeriod("20210316095311");
            reqBody.setLatitude("1");
            reqBody.setTerminalInfo("00000001");
            reqBody.setLocationId("102.13，112.09");
            reqBody.setPartnerId("");
            reqBody.setJumpUrl("");
            reqBody.setMerMemo("备注");
            List<RequireFields> require_fieldsList = new ArrayList<RequireFields>();
            RequireFields require_fields = new RequireFields();
            require_fieldsList.add(require_fields);
            reqBody.setRequireFields(require_fieldsList);
            require_fields.setRequireField("1");
            reqBody.setDisablePayChannels("credit");
            reqBody.setCurrency("CNY");
            LocationInfo location_info = new LocationInfo();
            reqBody.setLocationInfo(location_info);
            location_info.setTelecomnetworkid("1");
            location_info.setLbssignal3("1");
            location_info.setLbssignal1("1");
            location_info.setLbssignal2("1");
            location_info.setLocationcd1("1");
            location_info.setIccid("1");
            location_info.setLocationcd3("1");
            location_info.setTelecomlbssignal("1");
            location_info.setMobilecountrycd("1");
            location_info.setLocationcd2("1");
            location_info.setTelecomsystemid("1");
            location_info.setMobilenetworknum("1");
            location_info.setLbsnum2("1");
            location_info.setTelecomlbs("1");
            location_info.setLbsnum1("1");
            location_info.setLbsnum3("1");
            reqBody.setLongitude("1");
            List<RoyaltyInfo> royalty_infoList = new ArrayList<RoyaltyInfo>();
            RoyaltyInfo royalty_info = new RoyaltyInfo();
            royalty_infoList.add(royalty_info);
            reqBody.setRoyaltyInfo(royalty_infoList);
            royalty_info.setAmount("1");
            royalty_info.setSerialNo("1");
            reqBody.setTranScene("B2C-API-DISPLAYCODE");
            reqBody.setIp("1");
            reqBody.setMerPtcId("131079921009176");
            TermInfo term_info = new TermInfo();
            reqBody.setTermInfo(term_info);
            term_info.setDeviceType("1");
            term_info.setSecretText("1");
            term_info.setAppVersion("1");
            term_info.setSerialNum("1");
            term_info.setEncryptRandNum("1");
            term_info.setNetworkLicense("1");
            reqBody.setMerTradeTime("095711");
            reqBody.setNotifyUrl("");
            reqBody.setMerTradeDate("20210316");
            reqBody.setShopId("332211001");
            FeeSplitInfo fee_split_info = new FeeSplitInfo();
            reqBody.setFeeSplitInfo(fee_split_info);
            fee_split_info.setPartnerAmount("1");
            fee_split_info.setMerAmount("1");
            reqBody.setPayMerTranNo("12185644621782145186");
            reqBody.setTotalAmount("1.00");
            reqBody.setLocation("ONLINE");
            reqBody.setTranContent("商品名称：xxx");
            request.setBizContent(bizContent);
            MPNG210001ResponseV1 response =
                client.execute(request, UUID.randomUUID().toString().replace("-", "1"));
            if (response.isSuccess()) {
                System.out.println("success");
                System.out.println(response.getRspBody());
            } else {
                System.out.println(response.getRspCode());
                System.out.println(response.getRspMsg());
            }
        } catch (Exception xcp) {
            xcp.printStackTrace();
        }
    }
}
