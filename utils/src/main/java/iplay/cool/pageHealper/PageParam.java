package iplay.cool.pageHealper;


/**
 * @author wu.dang
 */
public class PageParam {
	private static final int MAX_PAGE_SIZE = 100;
	private static final int DEFAULT_PAGE_SIZE = 10;
	/**
	 * 页码
	 */
	private Integer pageNum = 1;
	/**
	 * 页大小
	 */
	private Integer pageSize = 10;

	public void setPageNum(Integer pageNum) {
		if (pageNum == null) {
			pageNum = 1;
		}
		if (pageNum < 1) {
			throw new IllegalArgumentException("invalid page number");
		}
		this.pageNum = pageNum;
	}

	public void setPageSize(Integer pageSize) {
		if (pageSize == null) {
			pageSize = DEFAULT_PAGE_SIZE;
		}
		if (pageSize < 1) {
			throw new IllegalArgumentException("invalid page size");
		}
		if (pageSize > MAX_PAGE_SIZE) {
			throw new IllegalArgumentException("too large page size");
		}
		this.pageSize = pageSize;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

}
