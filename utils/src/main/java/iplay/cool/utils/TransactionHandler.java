package iplay.cool.utils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

/**
 * @author dove
 * @date 2022/9/20
 */
@Service
public class TransactionHandler {

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public <T> T runInTransaction(Supplier<T> supplier) {
		return supplier.get();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public <T> T runInNewTransaction(Supplier<T> supplier) {
		return supplier.get();
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void runInTransaction(Runnable runnable) {
		runnable.run();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void runInNewTransaction(Runnable runnable) {
		runnable.run();
	}
}
