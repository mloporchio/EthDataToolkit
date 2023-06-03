package filter;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import data.Block;
import utils.Bits;

/**
 * <p>This program parses a list of Ethereum blocks (in compressed JSON format)
 * and extracts, for each block, the corresponding height and <code>logsBloom</code> field.</p>
 * 
 * <p>The program takes two parameters as inputs.</p>
 * <ol>
 * 	<li><code>inputFile</code>: path of the compressed JSON input file.</li>
 * 	<li><code>outputFile</code>: path of the binary output file.</li>
 * </ol>
 * 
 * <p>The program outputs a binary file containing a sequence of data chunks, each representing
 * a block and structured as follows.</p>
 * <ol>
 * 	<li><code>blockId</code>: block height (4 bytes).</li>
 * 	<li><code>logsBloom</code>: Bloom filter of the block (256 bytes).</li>
 * </ol>
 * 
 * @author Matteo Loporchio
 */
public class FilterExtractor {
	public static final Gson gson = new Gson();
	
	public static void main(String[] args) {
		// Check and parse command-line arguments.
		if (args.length < 2) {
			System.err.println("Usage: FilterExtractor <inputFile> <outputFile>");
			System.exit(1);
		}
		final String inputFile = args[0], outputFile = args[1];
		// Reads GZIP-compressed JSON file.
		try (
			JsonReader in = new JsonReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(inputFile))));
			DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)));
		) {
			in.beginArray();
			while (in.hasNext()) {
				// Deserialize block and extract identifier and filter.
				Block b = gson.fromJson(in, Block.class);
				int blockId = Integer.decode(b.number);
				byte[] filterBytes = Bits.fromHex(b.logsBloom.substring(2));
				out.writeInt(blockId);
				out.write(filterBytes);
			}
			in.endArray();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
