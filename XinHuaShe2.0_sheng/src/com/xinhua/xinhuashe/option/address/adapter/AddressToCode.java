package com.xinhua.xinhuashe.option.address.adapter;

/**
 * 县区对应的地区码转换工具类
 * @author LongWei
 *
 */
public class AddressToCode {

	/**
	 * 县区地址转为对应区码
	 * @param address
	 * @return
	 */
	public static String addressToCode(String address) {
		if("太原".equals(address)){
			return "140100";
		}
		if("大同".equals(address)){
			return "037000";
		}
		if("阳泉".equals(address)){
			return "045000";
		}
		if("长治".equals(address)){
			return "046001";
		}
		if("晋城".equals(address)){
			return "048000";
		}
		if("朔州".equals(address)){
			return "036001";
		}
		if("晋中".equals(address)){
			return "140700";
		}
		if("运城".equals(address)){
			return "044000";
		}
		if("忻州".equals(address)){
			return "034000";
		}
		if("临汾".equals(address)){
			return "041000";
		}
		if("吕梁".equals(address)){
			return "033000";
		}
		if("小店".equals(address)){
			return "140105";
		}
		if("古交市".equals(address)){
			return "030200";
		}
		if("万柏林".equals(address)){
			return "030021";
		}
		if("晋源".equals(address)){
			return "140110";
		}
		if("城区".equals(address)){
			return "037001";
		}
		if("南郊区".equals(address)){
			return "037002";
		}
		if("浑源".equals(address)){
			return "037400";
		}
		if("灵丘".equals(address)){
			return "034400";
		}
		if("阳泉城区".equals(address)){
			return "045002";
		}
		if("阳泉矿区".equals(address)){
			return "045003";
		}
		if("阳泉郊区".equals(address)){
			return "045001";
		}
		if("盂县".equals(address)){
			return "045100";
		}
		if("长治郊区".equals(address)){
			return "046002";
		}
		if("襄垣".equals(address)){
			return "046200";
		}
		if("屯留".equals(address)){
			return "046100";
		}
		if("长子".equals(address)){
			return "046600";
		}
		if("晋城城区".equals(address)){
			return "048002";
		}
		if("沁水".equals(address)){
			return "048200";
		}
		if("阳城".equals(address)){
			return "048100";
		}
		if("泽州".equals(address)){
			return "048001";
		}
		if("朔城区".equals(address)){
			return "036002";
		}
		if("山阴".equals(address)){
			return "036900";
		}
		if("怀仁".equals(address)){
			return "038300";
		}
		if("和顺".equals(address)){
			return "032700";
		}
		if("寿阳".equals(address)){
			return "140725";
		}
		if("平遥".equals(address)){
			return "031100";
		}
		if("灵石".equals(address)){
			return "031301";
		}
		if("盐湖区".equals(address)){
			return "044001";
		}
		if("永济".equals(address)){
			return "044500";
		}
		if("河津".equals(address)){
			return "043300";
		}
		if("忻府区".equals(address)){
			return "034001";
		}
		if("定襄".equals(address)){
			return "335400";
		}
		if("五台".equals(address)){
			return "335500";
		}
		if("代县".equals(address)){
			return "034200";
		}
		if("原平".equals(address)){
			return "034100";
		}
		if("尧都区".equals(address)){
			return "041002";
		}
		if("洪洞".equals(address)){
			return "031600";
		}
		if("安泽".equals(address)){
			return "042500";
		}
		if("离石区".equals(address)){
			return "033099";
		}
		if("柳林".equals(address)){
			return "033300";
		}
		if("交口".equals(address)){
			return "032499";
		}
		if("孝义".equals(address)){
			return "032300";
		}
		if("静乐".equals(address)){
			return "035100";
		}
		return "140000";
	}
}
