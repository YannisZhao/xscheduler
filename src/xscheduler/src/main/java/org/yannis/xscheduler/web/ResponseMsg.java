/** 
 * Project Name:uaas 
 * File Name:ResponseVO.java 
 * Package Name:org.yannis.uaas.vo 
 * Date:2015年11月27日上午9:40:28 
 * Copyright (c) 2015, zhaoyjun0222@gmail.com All Rights Reserved. 
 * 
 */  
      
package org.yannis.xscheduler.web;

import java.io.Serializable;
  
/** 
 * ClassName:ResponseVO <br/> 
 * Function: Value object of response. <br/> 
 * Date:     2015年11月27日 上午9:40:28 <br/> 
 * @author   Yanjun Zhao , zhaoyjun0222@gmail.com
 * @version  1.0 
 * @param <T>
 * @since    JDK 1.7 
 * @see
 * 		PageBean       
 */
public final class ResponseMsg implements Serializable {
	
	private static final long serialVersionUID = -331565365393423241L;

	private boolean status;
	
	private String message;

	/** 
	 * status. 
	 * 
	 * @return the status 
	 */
	public boolean getStatus() {
		return status;
	}

	/** 
	 * status. 
	 * 
	 * @param status 
	 *			the status to set
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}

	/** 
	 * message. 
	 * 
	 * @return the message 
	 */
	public String getMessage() {
		return message;
	}

	/** 
	 * message. 
	 * 
	 * @param message 
	 *			the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
  