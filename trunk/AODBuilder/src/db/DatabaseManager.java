package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.RowSetDynaClass;
import org.hsqldb.jdbc.jdbcDataSource;

import beans.db.DBWord;

public class DatabaseManager {
    
    private static final String USER="aoduser";
    private static final String PASS="aodPass09";
    private static final String DEFAULT_DB = "db";
    private static final String DB_PATH = "/resources/";
    
    private Connection conn; 
    private static DatabaseManager instance = null;

    private DatabaseManager(String db_file_name_prefix) throws Exception {

        // connect to the database.   This will load the db files and start the
        // database if it is not alread running.
        // db_file_name_prefix is used to open or create files that hold the state
        // of the db.
        // It can contain directory names relative to the
        // current working directory
        jdbcDataSource dataSource = new jdbcDataSource();

        dataSource.setDatabase("jdbc:hsqldb:" + db_file_name_prefix);

        conn = dataSource.getConnection(USER, PASS);
    }
    
    public static DatabaseManager getInstance() throws Exception{
    	if (instance == null)
    		instance = new DatabaseManager(DB_PATH+DEFAULT_DB);
    	return instance;
    }

    public static DatabaseManager getInstance(String db_file_name_prefix) throws Exception{
    	if (instance == null)
    		instance = new DatabaseManager(DB_PATH+db_file_name_prefix);
    	return instance;
    }

    public void shutdown() throws SQLException {

        Statement st = conn.createStatement();

        // db writes out to files and performs clean shuts down
        // otherwise there will be an unclean shutdown
        // when program ends
        st.execute("SHUTDOWN");
        conn.close();    // if there are no other open connection
    }

    public Statement getStatement() throws SQLException{
    	return conn.createStatement();
    }
    
    public PreparedStatement getPreparedStatement(String sql) throws SQLException{
    	return conn.prepareStatement(sql);
    }
    
    public synchronized Object getUniqueResult(String expression) throws SQLException {

    	Object ret = null;
        Statement st = null;
        ResultSet rs = null;

        st = conn.createStatement();         // statement objects can be reused with

        // repeated calls to execute but we
        // choose to make a new one each time
        rs = st.executeQuery(expression);    // run the query

        if (rs.next()){
        	ret = rs.getObject(1);
        }
        st.close();   
        
        return ret;
    }

    public synchronized int getID(String table, String condition) throws SQLException {

    	int id = -1;
        Statement st = null;
        ResultSet rs = null;

        st = conn.createStatement();         // statement objects can be reused with

        // repeated calls to execute but we
        // choose to make a new one each time
        String query = "SELECT ID FROM "+table;

        if (condition!=null && !condition.equals(""))
        	query += " WHERE "+condition;

        rs = st.executeQuery(query);    // run the query

        if (rs.next()){
        	id = rs.getInt(1);
        }
        st.close();    // NOTE!! if you close a statement the associated ResultSet is

        // closed too
        // so you should copy the contents to some other object.
        // the result set is invalidated also  if you recycle an Statement
        // and try to execute some other query before the result set has been
        // completely examined.
        
        return id;
    }
    
    //use for SQL command SELECT
    public synchronized RowSetDynaClass query(String expression) throws SQLException {

        Statement st = null;
        ResultSet rs = null;

        st = conn.createStatement();         // statement objects can be reused with

        // repeated calls to execute but we
        // choose to make a new one each time
        rs = st.executeQuery(expression);    // run the query
        
        RowSetDynaClass rsdc = new RowSetDynaClass(rs);

        st.close();    // NOTE!! if you close a statement the associated ResultSet is

        // closed too
        // so you should copy the contents to some other object.
        // the result set is invalidated also  if you recycle an Statement
        // and try to execute some other query before the result set has been
        // completely examined.
        
        return rsdc;
    }


    //use for SQL command SELECT
    public synchronized List<DBWord> getWords(String table, String[] columnsToFetch, String[] conditions) throws SQLException {

    	List<DBWord> words= new ArrayList<DBWord>();
        Statement st = null;
        ResultSet rs = null;
        
        String select = "";
        if (columnsToFetch!=null && columnsToFetch.length>0){
        	select = "SELECT";
        	for (String column: columnsToFetch){
        		if (!select.equals("SELECT"))
        			select+=",";
        		select+=" "+column;
        	}
        }else{
        	select = "SELECT *";
        }
        
        String from = " FROM "+table+" ";
        String where="";
        if (conditions!=null&&conditions.length>0){
        	for (String condition: conditions){
        		if (!where.equals(""))
        			where+=" AND";
        		else
        			where="WHERE";
        		where+=" "+condition;
        	}
        }
        
        st = conn.createStatement();         // statement objects can be reused with

        String query = select+from+where;
        
        rs = st.executeQuery(query); 

        if (rs!=null){
        	
            ResultSetMetaData meta   = rs.getMetaData();
            int               colmax = meta.getColumnCount();
            int               i;
            Object            o = null;

            for (; rs.next(); ) {
            	//create new word
            	DBWord word = new DBWord();

            	//add properties
            	for (i = 1; i <= colmax; ++i) {
                    o = rs.getObject(i);
                    if (o!=null){
	                	if (meta.getColumnLabel(i).equals("WORD"))
	                		word.setWord((String) o);
	                	if (meta.getColumnLabel(i).equals("ID"))
	                		word.setId((Integer) o);
	                	if (meta.getColumnLabel(i).equals("LANGUAGE"))
	                		word.setLanguage((String) o);
	                	if (meta.getColumnLabel(i).equals("TYPE"))
	                		word.setType((String) o);
                    }
                }
                //add word to list
            	words.add(word);
            }                
        }
        
        st.close();  
        
        return words;
    }
//    //use for SQL command SELECT
//    public synchronized ResultSet select(String table, String[] columnsToFetch, String[] conditions) throws SQLException {
//
//        Statement st = null;
//        ResultSet rs = null;
//        
//        String select = "";
//        if (columnsToFetch!=null && columnsToFetch.length>0){
//        	select = "SELECT";
//        	for (String column: columnsToFetch){
//        		if (!select.equals("SELECT"))
//        			select+=",";
//        		select+=" "+column;
//        	}
//        }else{
//        	select = "SELECT *";
//        }
//        
//        String from = " FROM "+table+" ";
//        String where="";
//        if (conditions!=null&&conditions.length>0){
//        	for (String condition: conditions){
//        		if (!where.equals(""))
//        			where+=" AND";
//        		else
//        			where="WHERE";
//        		where+=" "+condition;
//        	}
//        }
//        
//        st = conn.createStatement();         // statement objects can be reused with
//
//        String query = select+from+where;
//        
//        rs = st.executeQuery(query); 
//
//        st.close();  
//        
//        return rs;
//    }
    
//use for SQL commands CREATE, DROP, INSERT and UPDATE
    public synchronized int update(String expression) throws SQLException {

        Statement st = null;

        st = conn.createStatement();    // statements

        int i = st.executeUpdate(expression);    // run the query

        if (i == -1) {
            System.out.println("db error : " + expression);
        }

        st.close();
        
        return i;
    }    // void update()

    public static void dump(ResultSet rs) throws SQLException {

        // the order of the rows in a cursor
        // are implementation dependent unless you use the SQL ORDER statement
        ResultSetMetaData meta   = rs.getMetaData();
        int               colmax = meta.getColumnCount();
        int               i;
        Object            o = null;

        // the result set is a cursor into the data.  You can only
        // point to one row at a time
        // assume we are pointing to BEFORE the first row
        // rs.next() points to next row and returns true
        // or false if there is no next row, which breaks the loop
        for (; rs.next(); ) {
            for (i = 0; i < colmax; ++i) {
                o = rs.getObject(i + 1);    // Is SQL the first column is indexed

                // with 1 not 0
                System.out.print(o.toString() + " ");
            }

            System.out.println(" ");
        }
    }                                       //void dump( ResultSet rs )

    

}    

