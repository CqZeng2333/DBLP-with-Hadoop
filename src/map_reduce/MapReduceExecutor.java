package map_reduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.db.DBConfiguration;
import org.apache.hadoop.mapreduce.lib.db.DBInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


import connect_database.Connector;

public class MapReduceExecutor {
	public void executeTitle() throws ClassNotFoundException, IOException, InterruptedException {
		Configuration conf = new Configuration();
		Job titleJob = Job.getInstance(conf);
		titleJob.setJobName("PaperTitleJob");
		titleJob.setJarByClass(MapReduceExecutor.class);
		
		titleJob.setMapperClass(PaperTitleTableMapper.class);
		titleJob.setCombinerClass(PaperCombiner.class);
		titleJob.setReducerClass(PaperReducer.class);
		
		titleJob.setMapOutputKeyClass(Text.class);
        titleJob.setMapOutputValueClass(Text.class);
		titleJob.setOutputKeyClass(Text.class);
		titleJob.setOutputValueClass(Text.class);
		
		titleJob.setInputFormatClass(DBInputFormat.class);
		DBConfiguration.configureDB(titleJob.getConfiguration(), Connector.driverName, Connector.dbUrl, Connector.user, Connector.password);
		DBInputFormat.setInput(titleJob, PaperDBWritable.class, 
				"PaperInfo", null, null, "*");
		FileOutputFormat.setOutputPath(titleJob, new Path("data/paper-retrieval/title"));
		
		titleJob.waitForCompletion(true);
	}
	
	public void executeAuthor() throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		Job authorJob = Job.getInstance(conf);
		authorJob.setJobName("PaperAuthorJob");
		authorJob.setJarByClass(MapReduceExecutor.class);
		
		authorJob.setMapperClass(PaperAuthorTableMapper.class);
		authorJob.setCombinerClass(PaperCombiner.class);
		authorJob.setReducerClass(PaperReducer.class);
		
		authorJob.setMapOutputKeyClass(Text.class);
		authorJob.setMapOutputValueClass(Text.class);
		authorJob.setOutputKeyClass(Text.class);
		authorJob.setOutputValueClass(Text.class);
		
		authorJob.setInputFormatClass(DBInputFormat.class);
		DBConfiguration.configureDB(authorJob.getConfiguration(), Connector.driverName, Connector.dbUrl, Connector.user, Connector.password);
		DBInputFormat.setInput(authorJob, PaperDBWritable.class, 
				"PaperInfo", null, null, "*");
		FileOutputFormat.setOutputPath(authorJob, new Path("data/paper-retrieval/author"));
		
		authorJob.waitForCompletion(true);
	}
	
	public void executeJournal(String journalName, String journalShortName) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		Job journalJob = Job.getInstance(conf);
		journalJob.setJobName("PaperJournalJob");
		journalJob.setJarByClass(MapReduceExecutor.class);
		
		journalJob.setMapperClass(PaperAuthorTableMapper.class);
		journalJob.setCombinerClass(PaperCombiner.class);
		journalJob.setReducerClass(PaperReducer.class);
		
		journalJob.setMapOutputKeyClass(Text.class);
		journalJob.setMapOutputValueClass(Text.class);
		journalJob.setOutputKeyClass(Text.class);
		journalJob.setOutputValueClass(Text.class);
		
		journalJob.setInputFormatClass(DBInputFormat.class);
		DBConfiguration.configureDB(journalJob.getConfiguration(), Connector.driverName, Connector.dbUrl, Connector.user, Connector.password);
		DBInputFormat.setInput(journalJob, PaperDBWritable.class, 
				"select * from PaperInfo where journal = \'" + journalName + "\'", 
				"select count(*) from PaperInfo where journal = \'" + journalName + "\'");
		FileOutputFormat.setOutputPath(journalJob, new Path("data/paper-retrieval/journal/" + journalShortName));
		
		journalJob.waitForCompletion(true);
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		MapReduceExecutor mre = new MapReduceExecutor();
//		mre.executeTitle();
		mre.executeAuthor();
//		mre.executeJournal("ACM Trans. Interact. Intell. Syst.", "TIIS");
//		mre.executeJournal("ACM Trans. Intell. Syst. Technol.", "TIST");
//		mre.executeJournal("ACM Trans. Knowl. Discov. Data", "TKDD");
//		mre.executeJournal("ACM Trans. Inf. Syst.", "TOIS");
//		mre.executeJournal("ACM Trans. Internet Techn.", "TOIT");
//		mre.executeJournal("ACM Trans. Web", "TWEB");
		
	}
}
