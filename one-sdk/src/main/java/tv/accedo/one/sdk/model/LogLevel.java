package tv.accedo.one.sdk.model;

import android.util.Log;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public enum LogLevel{
	off(0, Log.DEBUG),
	error(1, Log.ERROR),
	warn(2, Log.WARN),
	info(3, Log.INFO),
	debug(4, Log.DEBUG);
	
	public final int level;
	public final int logcatPriority;
	
	LogLevel(int level, int logcatPriority){
		this.level = level;
		this.logcatPriority = logcatPriority;
	}
}