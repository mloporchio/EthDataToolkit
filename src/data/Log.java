package data;

import java.util.List;

/**
 * Ethereum transaction log.
 * 
 * @author Matteo Loporchio
 */
public class Log {
	/**
	 * Address of the account that created the log.
	 */
	public final String address;

	/**
	 * List of associated topics.
	 */
	public final List<String> topics;

	/**
	 * Index of this log in the log list.
	 */
	public final String logIndex;
	
	/**
	 * Constructs a new transaction log.
	 * @param address address of the account
	 * @param topics list of topics
	 * @param logIndex index of the log
	 */
	public Log(String address, List<String> topics, String logIndex) {
		this.address = address;
		this.topics = topics;
		this.logIndex = logIndex;
	}
}
