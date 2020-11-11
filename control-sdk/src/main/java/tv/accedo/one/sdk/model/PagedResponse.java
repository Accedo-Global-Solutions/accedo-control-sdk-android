package tv.accedo.one.sdk.model;

import org.json.JSONArray;

import java.io.Serializable;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class PagedResponse implements Serializable {
    private JSONArray entries;
	private int total;
	private int size;
	private int offset;

    public JSONArray getEntries() {
        return entries;
    }
    public int getTotal() {
        return total;
    }
    public int getSize() {
        return size;
    }
    public int getOffset() {
        return offset;
    }

    public PagedResponse(JSONArray entries, int total, int size, int offset) {
        this.entries = entries;
        this.total = total;
        this.size = size;
        this.offset = offset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PagedResponse)) return false;

        PagedResponse that = (PagedResponse) o;

        if (total != that.total) return false;
        if (size != that.size) return false;
        if (offset != that.offset) return false;
        if (entries != null ? !entries.equals(that.entries) : that.entries != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = entries != null ? entries.hashCode() : 0;
        result = 31 * result + total;
        result = 31 * result + size;
        result = 31 * result + offset;
        return result;
    }
}