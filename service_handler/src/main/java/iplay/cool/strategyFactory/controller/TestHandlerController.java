package iplay.cool.strategyFactory.controller;

import iplay.cool.strategyFactory.BlindBoxHandlerFactory;
import iplay.cool.strategyFactory.enums.BlindBoxEnum;
import iplay.cool.strategyFactory.param.BlindBoxOpenParam;
import iplay.cool.strategyFactory.vo.BlindBoxVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author dove
 * @date 2022/9/28
 */
@RestController
@RequestMapping("/handler")
public class TestHandlerController {

	@Resource
	private BlindBoxHandlerFactory blindBoxHandlerFactory;

	@PostMapping("/openBlindBox")
	public BlindBoxVO openBlindBox(BlindBoxOpenParam param){
		//handlerType可以通过多种方式获取，从配置或者网页上或者从表中获取，或者分接口，根据不同接口硬编码type
		String handlerType = BlindBoxEnum.MIDGARD_SAGA.getHandlerType();
		return blindBoxHandlerFactory.getBlindBoxHandler(handlerType).openBox(param);
	}
}
