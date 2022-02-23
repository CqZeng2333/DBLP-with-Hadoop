package connect_database;

import java.sql.*;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;

import utils.Document;

public class Selector {
	private static Connection conn = Connector.getConn();

    /*
     * Get the number of all the documents
     * return: int->number of document
     */
	public static int getDocNum() {
		Statement stmt = null;
		ResultSet rset = null;
    	try {
    		stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			rset = stmt.executeQuery("SELECT count(*) FROM PaperInfo;");
			if (rset.next()) {
				return rset.getInt(1);
			}
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return 0;
	}
    
    /*
     * Get a map of rank, document
     * according to document id
     * para: id - a TreeMap of scores->document id
     * return: a TreeMap of scores->document information
     */
    public static TreeMap<Double, Document> getDoc(TreeMap<Double, Integer> id) {
    	Statement stmt = null;
		ResultSet rset = null;
		String str = null;
		TreeMap<Double, Document> docSet = new TreeMap<>(new Comparator<Double>() {
			@Override
			public int compare(Double o1, Double o2) {
				return o2.compareTo(o1);
			}});
    	try {
    		stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			str = "SELECT * FROM PaperInfo WHERE paper_id = ";
			Iterator<Double> iter = id.keySet().iterator();
			Double score;
			while (iter.hasNext()) {
				score = iter.next();
				rset = stmt.executeQuery(str + id.get(score).toString());
				if (rset.next()) {
					Document doc = new Document.Builder() 
									.id(rset.getInt("paper_id"))
									.title(rset.getString("title"))
									.authors(rset.getString("author"))
									.year(rset.getInt("published_year"))
									.journal(rset.getString("journal"))
									.ee(rset.getString("paper_url")).build();
					docSet.put(score, doc);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return docSet;
    }
    
    /*
     * Get a map of docID - year gap between the 
     * published year of the doc and 2018
     * according to document id
     * para: id - a Set of document id
     * return: a TreeMap of docID->year gap
     */
    public static TreeMap<Integer, Integer> getYearGap(Collection<Integer> ID) {
    	Statement stmt = null;
		ResultSet rset = null;
		String str = null;
		TreeMap<Integer, Integer> yearGap = new TreeMap<>();
    	try {
    		stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			str = "SELECT published_year FROM PaperInfo WHERE paper_id = ";
			Iterator<Integer> iter = ID.iterator();
			Integer id;
			while (iter.hasNext()) {
				id = iter.next();
				rset = stmt.executeQuery(str + id.toString());
				if (rset.next()) {
					yearGap.put(id, 2022 - Integer.parseInt(rset.getString("published_year")));
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return yearGap;
    }
}
