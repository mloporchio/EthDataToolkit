package filter;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.zip.GZIPInputStream;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import data.Block;
import utils.Bits;

/**
 * <p>This program parses a list of Ethereum blocks (in compressed JSON format)
 * and computes the number of ones in the corresponding <code>logsBloom</code> field.
 * 
 * <p>The program takes two input parameters.</p>
 * <ol>
 * 	<li><code>inputFile</code>: path of the compressed JSON input file;</li>
 * 	<li><code>outputFile</code>: path of the output CSV file.</li>
 * </ol>
 * 
 * <p>The program outputs a CSV file where each row contains the following fields.</p>
 * <ol>
 * 	<li><code>blockId</code>: identifier of the block (i.e., its height);</li>
 * 	<li><code>timestamp</code>: timestamp of the block;</li>
 * 	<li><code>numOnes</code>: number of ones in the <code>logsBloom</code> filter of the block.</li>
 * </ol>
 * 
 * @author Matteo Loporchio
 */
public class FilterStats {
	public static final Gson gson = new Gson();
	
	public static void main(String[] args) {
		// Check and parse command-line arguments.
		if (args.length < 2) {
			System.err.println("Usage: FilterStats <inputFile> <outputFile>");
			System.exit(1);
		}
		final String inputFile = args[0];
		final String outputFile = args[1];
		// Reads GZIP-compressed JSON file.
		try (
			JsonReader in = new JsonReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(inputFile))));
			PrintWriter out = new PrintWriter(outputFile);
		) {
			in.beginArray();
			while (in.hasNext()) {
				// Deserialize block and extract all fields of interest.
				Block b = gson.fromJson(in, Block.class);
				int blockId = Integer.decode(b.number);
				long timestamp = Long.decode(b.timestamp);
				byte[] filterBytes = Bits.fromHex(b.logsBloom.substring(2));
				int numOnes = Bits.countOnes(filterBytes);
				out.printf("%d,%d,%s\n", blockId, timestamp, numOnes);
			}
			in.endArray();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
