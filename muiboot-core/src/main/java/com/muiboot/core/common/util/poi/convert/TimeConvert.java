package com.muiboot.core.common.util.poi.convert;

import com.muiboot.core.common.util.DateUtil;

/**
* <p>Description: 导出时间配置</p>
* @version 1.0 2018/10/12
* @author jin
*/
public class TimeConvert implements ExportConvert {

	@Override
	public String handler(Object val) {
		try {
			if (val == null)
				return "";
			else {
				return DateUtil.formatCSTTime(val.toString(), "yyyy-MM-dd HH:mm:ss");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

}
