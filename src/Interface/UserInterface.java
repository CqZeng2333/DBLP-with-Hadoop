package Interface;

import java.util.ArrayList;
import java.util.TreeMap;

import utils.Document;
import connect_database.Selector;
import reverted_index.Index;
import utils.SearchType;

public class UserInterface {
//	public static void main(String[] args) {
//		Index index = new Index();
//		index.loadTF_IDF("data/paper-retrieval/title/part-r-00000");
//		//index.loadTF_IDF("data/paper-retrieval/author/part-r-00000");
//		//index.loadTF_IDF("data/paper-retrieval/journal/TIIS/part-r-00000");
//		index.countAll();
//		
//		TreeMap<String, Double> queryValue = index.getQuery("security");
//		TreeMap<Double, Integer> topK = index.getTopK(10, queryValue);
//		TreeMap<Double, Document> result = Selector.getDoc(topK);
//		System.out.println("Retrieval Result: ");
//		for (Document d : result.values()) {
//			System.out.println(d);
//		}
//	}
	
	public static ArrayList<String> query(String query, String sType, String journal) {
		if (query == null) {
			return new ArrayList<>();
		}
		
		String loadPath = "";
		if (sType.equals(SearchType.journal.toString())) {
			loadPath = "data/paper-retrieval/journal/" + journal + "/part-r-00000";
		} else if (sType.equals(SearchType.author.toString())) {
			loadPath = "data/paper-retrieval/author/part-r-00000";
		} else {
			loadPath = "data/paper-retrieval/title/part-r-00000";
		}
		Index index = new Index();
		index.loadTF_IDF(loadPath);
		index.countAll();
		
		TreeMap<String, Double> queryValue = index.getQuery(query);
		TreeMap<Double, Integer> topK = index.getTopK(10, queryValue);
		TreeMap<Double, Document> result = Selector.getDoc(topK);
		ArrayList<String> queryResult = new ArrayList<>();
		System.out.println("Retrieval Result: ");
		for (Document d : result.values()) {
			System.out.println(d);
			queryResult.add(d.toString());
		}
		
		return queryResult;
		
	}
	
	public static void main(String[] args) {
		Server server = new Server();
		server.acceptClients();
	}
}
