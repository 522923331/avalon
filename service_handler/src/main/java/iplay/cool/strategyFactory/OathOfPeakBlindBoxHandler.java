package iplay.cool.strategyFactory;

import iplay.cool.strategyFactory.enums.BlindBoxEnum;
import iplay.cool.strategyFactory.param.BlindBoxOpenParam;
import iplay.cool.strategyFactory.vo.BlindBoxVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author dove
 * @date 2022/9/28
 */
@Slf4j
@Component
public class OathOfPeakBlindBoxHandler implements BlindBoxHandler{
	@Override
	public String getHandlerType() {
		return BlindBoxEnum.OATH_OF_PEAK.getHandlerType();
	}

	@Override
	public BlindBoxVO openBox(BlindBoxOpenParam param) {
		//具体业务处理。。。。
		return null;
	}
}
