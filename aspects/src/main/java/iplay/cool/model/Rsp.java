package iplay.cool.model;

import lombok.Data;

/**
 *
 * @author dove
 * @date 2022/7/7
 */
@Data
public class Rsp <T>{
	private int code;
	private String message;
	private T data;

	public static final int SUCCESS = 200;

	public boolean isSuccess(){
		return SUCCESS == this.code;
	}
}
