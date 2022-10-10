package iplay.cool.strategyFactory;

import iplay.cool.strategyFactory.param.BlindBoxOpenParam;
import iplay.cool.strategyFactory.vo.BlindBoxVO;

/**
 * @author dove
 * @date 2022/9/28
 */
public interface BlindBoxHandler {
	/**
	 * 获取类型
	 * @return String
	 */
	String getHandlerType();

	/**
	 * 开启盲盒 不同游戏开启方式不同
	 * @param param 入参
	 * @return BlindBoxVO
	 */
	BlindBoxVO openBox(BlindBoxOpenParam param);
}
