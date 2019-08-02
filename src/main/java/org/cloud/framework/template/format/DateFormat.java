package org.cloud.framework.template.format;

import org.beetl.core.Format;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 格式检查编写模板
 * @author anhuifeng
 *
 */
public class DateFormat implements Format {

	@Override
	public Object format(Object data, String pattern) {
		
		if (data == null)
			return null;
		if (Date.class.isAssignableFrom(data.getClass())) {
			SimpleDateFormat sdf = null;
			if (pattern == null) {
				sdf = new SimpleDateFormat();
			} else {
				sdf = new SimpleDateFormat(pattern);
			}
			return sdf.format((Date) data);
		} else {
			//throw new RuntimeException("Arg Error:Type should be Date");
			return null;
		}

	}

}
