package tv.accedo.one.sdk.model;

import java.io.Serializable;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class PaginatedParams extends OptionalParams implements Serializable {
	public static final int MIN_PAGE_SIZE = 1;
	public static final int DEFAULT_PAGE_SIZE = 20;
	public static final int MAX_PAGE_SIZE = 50;

	private int offset = 0;
	private int size = DEFAULT_PAGE_SIZE;

	/**
	 * @return The offset in the pagination, starting at 0.
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @return The number of results per page. Between 1 and 50, default is 20.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Sets the offset in the pagination, starting at 0.
	 * @param offset
	 * @return
	 */
	public PaginatedParams setOffset(int offset) {
		this.offset = offset;
		return this;
	}

	/**
	 * Sets the number of results per page. Between 1 and 50, default is 20.
	 * @param size
	 * @return
	 */
	public PaginatedParams setSize(int size) {
		this.size = size;
		return this;
	}

	public PaginatedParams() {
	}
    public PaginatedParams(String locale) {
        super(locale);
    }
    public PaginatedParams(int size, int offset) {
        this.size = size;
        this.offset = offset;
    }
    public PaginatedParams(OptionalParams optionalParams) {
        super(optionalParams);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaginatedParams)) return false;
        if (!super.equals(o)) return false;

        PaginatedParams that = (PaginatedParams) o;

        if (offset != that.offset) return false;
        if (size != that.size) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + offset;
        result = 31 * result + size;
        return result;
    }
}