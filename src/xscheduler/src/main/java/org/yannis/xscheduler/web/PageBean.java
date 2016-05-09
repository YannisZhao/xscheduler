package org.yannis.xscheduler.web;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 
     * ClassName: PageBean <br/> 
     * Function: Page result set. <br/> 
     * date: 2015年11月27日 上午9:51:22 <br/> 
     * 
     * @author Yanjun Zhao , zhaoyjun0222@gmail.com
     * @version  
     * @since JDK 1.7
 */
public class PageBean implements Serializable {

	private static final long serialVersionUID = -8289222468659629733L;

	/**
	 * 总记录数
	 */
	protected long total;

	/**
	 * 记录集
	 */
	protected List<Map<String, Object>> rows;


	/**
	 * @return the total
	 */
	public long getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(long total) {
		this.total = total;
	}

	/**
	 * @return the rows
	 */
	public List<Map<String, Object>> getRows() {
		return rows;
	}

	/**
	 * @param rows
	 *            the rows to set
	 */
	public void setRows(List<Map<String, Object>> rows) {
		this.rows = rows;
	}

}
