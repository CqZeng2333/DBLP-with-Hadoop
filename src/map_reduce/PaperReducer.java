package map_reduce;

import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import connect_database.Selector;

public class PaperReducer extends Reducer<Text, Text, Text, Text> {

	@Override
	protected void reduce(Text key, Iterable<Text> values, Reducer<Text, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {
		String term = key.toString();
		HashMap<String, Double> tf = new HashMap<>(); // Term frequency for each term
		int totalCount = 0; // Total number of the documents containing this term
		// Extract document ID and term frequency in the document
		for (Text item : values) {
			String[] docNum_docCount = item.toString().split("&");
			String docNum = docNum_docCount[0];
			double docCount = Double.parseDouble(docNum_docCount[1]);
			tf.put(docNum, docCount);
			totalCount += 1;
		}
		// Get the number of all the document
		double allDocNum = Selector.getDocNum();
		double idf = allDocNum / (totalCount + 1);
		for (String docNum : tf.keySet()) {
			double doc_tf = tf.get(docNum);
			// Count TF-IDF value for the term in this document
			double tf_idf = doc_tf * Math.log(idf);
			context.write(new Text(term), new Text(docNum + "&" + String.format("%.4f", new Double(tf_idf))));
		}
	}
	
}
