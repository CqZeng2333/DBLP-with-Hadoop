package map_reduce;

import java.io.IOException;
import java.util.HashSet;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import reverted_index.Index;
import utils.Stemmer;

public class PaperAuthorTableMapper extends Mapper<Object, PaperDBWritable, Text, Text>  {
	private Text outputTerm = new Text();
	@Override
	protected void map(Object key, PaperDBWritable value,
			Mapper<Object, PaperDBWritable, Text, Text>.Context context)
			throws IOException, InterruptedException {
		String doc = value.getDoc_title() + " " + String.join(" ", value.getAuthors().split("|")); // title and author
		System.out.println(doc + " " + value.getDoc_id());
		String[] termList = Index.tokenize(doc);
		Text tf = new Text(String.format("%.16f", new Double(1 / ((double)termList.length)))); // Term frequency for each word in the document
		HashSet<String> stopWords = Index.getStopWords();
		for (String term : termList) {
			term = term.toLowerCase();
			// Remove stop words
			if (!stopWords.contains(term)) {
				// Do stemming
				Stemmer stemmer = new Stemmer();
				stemmer.add(term.toCharArray(), term.length());
				stemmer.stem();
				if (stemmer.toString().equals("")) {
					continue;
				}
				// Output the (term&docNum, 1) pairs
				outputTerm.set(stemmer.toString() + "&" + value.getDoc_id());
				context.write(outputTerm, tf);
			}
		}
	}
}
