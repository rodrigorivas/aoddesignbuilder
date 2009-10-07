package db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import beans.db.DBBean;
import beans.db.DBWord;

public class WordsHelper extends TableHelper{
	
	private static final String CREATE_TABLE =
		"CREATE TABLE WORDS ( " +
			"id INTEGER GENERATED BY DEFAULT AS IDENTITY" +
				"(START WITH 1) NOT NULL PRIMARY KEY, " +
			"language VARCHAR(3), " +
			"word VARCHAR(256), " +
			"type VARCHAR(3))";
	
	private static final String INSERT = 
		"INSERT INTO WORDS" +
			"(word,language,type) " +
		"VALUES (?,?,?)";
	
	private static final String UPDATE = 
		"UPDATE WORDS" +
			"SET word = ?, language = ?, type = ? " +
		"WHERE id = ?";
	
	private static final String GET_BY_ID = 
		"SELECT " +
			"id, " +
			"language, " +
			"word, " +
			"type " +
		"FROM WORDS " +
		"WHERE id = ?";

	private static final String GET_FULL_WORD = 
		"SELECT " +
			"w.id as id, " +
			"w.language as language, " +
			"w.word as word, " +
			"w.type as type," +
			"s.synonym_ref as synonym_id " +
		"FROM WORDS w inner join SYNONYMS s on w.id = s.word_ref " +
		"WHERE w.word = ?";
	
	private static final String GET_BY_WORD = 
		"SELECT " +
			"id, " +
			"language, " +
			"word, " +
			"type " +
		"FROM WORDS " +
		"WHERE word = ?";

	private static WordsHelper instance;
	
	private WordsHelper(DatabaseManager db) {
		super(db);
	}
	
	public static WordsHelper getInstance(DatabaseManager db){
		if (instance == null)
			instance = new WordsHelper(db);
		
		return instance;
	}

    public int createTable() throws SQLException{
        return db.update(CREATE_TABLE);
    }
    
    @Override
	public int insert(DBBean bean) throws Exception{
    	int ret = 0;
    	if (bean!=null){
    		DBWord word = null;
    		try{
    			word = (DBWord)bean;
    		}catch (Exception e) {
				// TODO: handle exception
    			throw e;
			}
    		PreparedStatement ps = null;
    		try{
		    	ps = db.getPreparedStatement(INSERT);
		    	
		    	ps.setString(1, word.getWord());
		    	ps.setString(2, word.getLanguage());
		    	ps.setString(3, word.getType());
		    	
		    	ret = ps.executeUpdate();
    		}catch (Exception e) {
				// TODO: handle exception
				throw e;
			}finally{
				if (ps != null)
					ps.close();
			}
    	}
    	
    	return ret;
	}

	@Override
	public int update(DBBean bean) throws Exception {
    	int ret = 0;
		if (bean!=null && bean.getId()!=0){
    		DBWord word = null;
    		try{
    			word = (DBWord)bean;
    		}catch (Exception e) {
				// TODO: handle exception
    			throw e;
			}
    		PreparedStatement ps = null;
    		try{
		    	ps = db.getPreparedStatement(UPDATE);
		    	
		    	ps.setString(1, word.getWord());
		    	ps.setString(2, word.getLanguage());
		    	ps.setString(3, word.getType());
		    	ps.setInt(4, word.getId());
		    	
		    	ret = ps.executeUpdate();
    		}catch (Exception e) {
				// TODO: handle exception
				throw e;
			}finally{
				if (ps != null)
					ps.close();
			}
		}
		return ret;		
	}

	@Override
	public DBBean getById(int id) throws Exception {
		DBWord word = null;
		if (id != 0){   	
    		PreparedStatement ps = null;
    		try{
		    	ps = db.getPreparedStatement(GET_BY_ID);
		    	
		    	ps.setInt(1, id);
		    	
		    	ResultSet rs = ps.executeQuery(); 

		        if (rs.next()){
		        	word = new DBWord();
		        	word.setId(rs.getInt("id"));
		        	word.setLanguage(rs.getString("language"));
		        	word.setType(rs.getString("type"));
		        	word.setWord(rs.getString("word"));
		        }	

    		}catch (Exception e) {
				// TODO: handle exception
				throw e;
			}finally{
				if (ps != null)
					ps.close();
			}
		}
		return word;
	}

	public DBWord getFullWord(String wordStr) throws Exception {
		DBWord word = null;
		if (wordStr != null){   	
    		PreparedStatement ps = null;
    		try{
		    	ps = db.getPreparedStatement(GET_FULL_WORD);
		    	
		    	ps.setString(1, wordStr);
		    	
		    	ResultSet rs = ps.executeQuery(); 

		        if (rs.next()){
		        	word = new DBWord();
		        	word.setId(rs.getInt("id"));
		        	word.setLanguage(rs.getString("language"));
		        	word.setType(rs.getString("type"));
		        	word.setWord(rs.getString("word"));
		        	int synonym_id = rs.getInt("synonym_id");
		        	word.addSynonym((DBWord) getById(synonym_id));
		        	
		        	while (rs.next()){
		        		synonym_id = rs.getInt("synonym_id");
			        	word.addSynonym((DBWord) getById(synonym_id));	        		
		        	}
		        }

    		}catch (Exception e) {
				// TODO: handle exception
				throw e;
			}finally{
				if (ps != null)
					ps.close();
			}
		}
		return word;
	}

	public DBWord getByWord(String value) throws SQLException {
		if (value!=null){
	    	DBWord word = null;
	    	PreparedStatement ps = db.getPreparedStatement(GET_BY_WORD);
	    	
	    	ps.setString(1, value);
	    	
	    	ResultSet rs = ps.executeQuery(); 

	        if (rs.next()){
	        	word = new DBWord();
	        	word.setId(rs.getInt("id"));
	        	word.setLanguage(rs.getString("language"));
	        	word.setType(rs.getString("type"));
	        	word.setWord(rs.getString("word"));
	        }
    	
	    	return word;
		}
		return null;		
	}

}
