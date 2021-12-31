package map_reduce;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class PaperCombiner extends Reducer<Text, Text, Text, Text> {

	@Override
	protected void reduce(Text key, Iterable<Text> values, Reducer<Text, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {
		String[] term_docNum = key.toString().split("&");
		String term = term_docNum[0];
		String docNum = term_docNum[1];
		Text outputTerm = new Text(term);
		double countTF = 0;
		// Sum up the term frequency for the term in this document
		for (Text tx : values) {
			double tf = Double.parseDouble(tx.toString());
			countTF += tf;
		}
		Text outputValue = new Text(docNum + "&" + String.format("%.16f", new Double(countTF)));
		context.write(outputTerm, outputValue);
	}
	
	
	
}
