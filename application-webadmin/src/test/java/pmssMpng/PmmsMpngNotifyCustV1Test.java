package pmssMpng;

import com.course.app.webadmin.upms.model.pmms.request.PmmsMpngNotifyRequestV1;
import com.bocom.api.utils.ApiUtils;

/**
 * <pre>
 * 交行通知企业回调（通知接收方使用）
 * </pre>
 * <p>
 * </p>
 */
public class PmmsMpngNotifyCustV1Test {

	public static final String APIGW_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxSJrF8T/5rKB4NnwMjIxUer+ELf1PQXO2GSdZ/fvuQCclOR9tBlNWL4jFOftebeL+bvMVOJ+JHm/aSes1AN8YNIDGiFUpF6aDkSCaLynDdjK/mQTWhSNa2fO0GGO+ywOBTdYUjVjVtzJ48bbyG3NSylf1EdnBWnMpFa8qpXJXR4ELpVpMkPDC+93HBAlxEgUjhcIJlP5VdKIiudsmhE2T07qtpIQSuE5hntXP6X6GKJReCk+yek2QJITvIBq3cHPw8KDsHHCs7MaR81KI3onJRWyqFtTfVYTiKsd9EcYSxv+Gx5MOF8B/P4iJCD8uzx0FrqoB3k5OYGcz4tXs+h+9wIDAQAB";

	public static void main(String[] args) {
		try {
			/**
			 * notifyJson为银行通知给商户的JSON报文，商户通过创建订单时上送的notify_url地址，银行回调该地址
			 */
			String notifyJson = "{\"notify_biz_content\":{\"notify_url\":\"https://bp.winning-health.com.cn:11571/winupay/n/309/40124787-2/0\",\"tran_type\":\"PAY\",\"mer_tran_no\":\"WNCS20210526043954\",\"tran_state\":\"SUCCESS\",\"tran_state_code\":\"\",\"tran_state_msg\":\"\",\"partner_id\":\"\",\"mer_ptc_id\":\"131007420005887\",\"final_time\":\"20210526164025\",\"total_amount\":\"0.01\",\"buyer_pay_amount\":\"0.01\",\"trd_dsct_amount\":\"0.00\",\"pay_dsct_amount\":\"0.00\",\"currency\":\"CNY\",\"tran_content\":\"\",\"mer_memo\":\"天津市永久医院卫宁测试\",\"require_values\":{\"bank_tran_no\":null,\"third_party\":null,\"third_party_tran_no\":null,\"payment_info\":null,\"refund_info\":null}},\"sign\":\"gpYV8HsUOFuiJVvy4689IJirE5jW4C8TQhwQiPj/9XzrbZ1++7JY5mNzvq3PGqJRKLabBH3AVeqyUSFdV6Ic3btjqGnnp99lmQ7y0Ys86xN50z0vWj/0ZVbZnlhBGGTgR/kHo7knjOqEPu4qq61yZJXYCiUd0FMei7VuOzg5ltTB5I5x2VhHw38tYhbMLWWuyRpMkttLvuGf4hJfZHq0fgT44ZL+6SrJbvq48Y7s1Uv7zFgEFwhgbKKIMs+P93TNB8jJ147ChN1U9h5z2xBeC1HJZGRniLsR5kfRgBBWCf1VbxBsLp3twd2zahKwygnFSZ2xzZj3TJrq1HjG7oZrkQ==\"}";
			PmmsMpngNotifyRequestV1.PmmsMpngNotifyRequestV1Biz request = (PmmsMpngNotifyRequestV1.PmmsMpngNotifyRequestV1Biz) ApiUtils.parseNotifyJsonWithBocomSign(notifyJson, "UTF-8", APIGW_PUBLIC_KEY, PmmsMpngNotifyRequestV1.PmmsMpngNotifyRequestV1Biz.class);
			System.out.println(request.getMerPtcId());
		} catch (Exception xcp) {
			xcp.printStackTrace();
		}
	}
}