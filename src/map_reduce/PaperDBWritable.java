package map_reduce;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.lib.db.DBWritable;

import utils.Document;

public class PaperDBWritable extends Document implements Writable, DBWritable {

	@Override
	public void readFields(DataInput input) throws IOException {
		this.setDoc_id(input.readInt());
		this.setDoc_title(input.readUTF());
		this.setAuthors(input.readUTF());
		this.setDoc_year(input.readInt());
		this.setDoc_journal(input.readUTF());
		this.setDoc_ee(input.readUTF());
	}

	@Override
	public void write(DataOutput output) throws IOException {
		output.writeInt(this.getDoc_id());
		output.writeUTF(this.getDoc_title());
		output.writeUTF(this.getAuthors());
		output.writeInt(this.getDoc_year());
		output.writeUTF(this.getDoc_journal());
		output.writeUTF(this.getDoc_ee());
	}

	@Override
	public void readFields(ResultSet resultSet) throws SQLException {
		this.setDoc_id(resultSet.getInt(1));
		this.setDoc_title(resultSet.getString(2));
		this.setAuthors(resultSet.getString(3));
		this.setDoc_year(resultSet.getInt(4));
		this.setDoc_journal(resultSet.getString(5));
		this.setDoc_ee(resultSet.getString(6));
	}

	@Override
	public void write(PreparedStatement pstmt) throws SQLException {
		pstmt.setInt(1, this.getDoc_id());
		pstmt.setString(2, this.getDoc_title());
		pstmt.setString(3, this.getAuthors());
		pstmt.setInt(4, this.getDoc_year());
		pstmt.setString(5, this.getDoc_journal());
		pstmt.setString(5, this.getDoc_ee());
	}
}
