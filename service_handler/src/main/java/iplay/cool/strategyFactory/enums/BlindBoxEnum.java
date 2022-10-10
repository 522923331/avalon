package iplay.cool.strategyFactory.enums;

/**
 * @author dove
 * @date 2022/9/28
 */
public enum BlindBoxEnum {
	/**
	 * 骑士与契约
	 */
	MIDGARD_SAGA("MidgardSaga", "骑士与契约"),
	OATH_OF_PEAK("OathofPeak", "玄中记"),
	;
	private String handlerType;
	private String desc;
	BlindBoxEnum(String handlerType,String desc){
		this.handlerType = handlerType;
		this.desc = desc;
	}

	public String getHandlerType() {
		return handlerType;
	}

	public String getDesc() {
		return desc;
	}
}
