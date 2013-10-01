package com.domsplace.Villages.DataManagers;

import com.domsplace.Villages.Bases.Base;
import static com.domsplace.Villages.Bases.Base.getPlugin;
import com.domsplace.Villages.Bases.DataManager;
import com.domsplace.Villages.Enums.ManagerType;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.file.YamlConfiguration;

public class SQLManager extends DataManager {
    private String host;
    private String port;
    private String username;
    private String password;
    private String database;
    private String prefix;
    
    private Connection connection;
    private Statement statement;
    
    public SQLManager() {
        super(ManagerType.SQL);
    }
    
    public void setupSQL(String host, String port, String username, String password, String database, String prefix) {
        this.host = host.replaceAll("%", "E");
        this.port = port.replaceAll("%", "E");
        this.username = username.replaceAll("%", "E");
        this.password = password.replaceAll("%", "E");
        this.database = database.replaceAll("%", "E");
        this.prefix = prefix.replaceAll("%", "E");
    }
    
    public String sqlEscape(Object o) {
        String str = o.toString();
        str = str.replace("\\", "\\\\");
        str = str.replace("'", "\\'");
        //str = str.replace("\0", "\\0");
        str = str.replace("\n", "\\n");
        str = str.replace("\r", "\\r");
        str = str.replace("\"", "\\\"");
        str = str.replace("\\x1a", "\\Z");
        str = str.replaceAll("%", "@");
        return str;
    }

    public String sqlUnescape(Object o) {
        String str = o.toString();
        str = str.replaceAll("\\\\", "\\");
        str = str.replaceAll("\\'", "'");
        str = str.replace("\\n", "\n");
        str = str.replace("\\r", "\r");
        str = str.replace("\\\"", "\"");
        str = str.replace("\\Z", "\\x1a");
        str = str.replaceAll("@", "%");
        return str;
    }
    
    public String prepareQuery(String query) {
        query = query.replaceAll("%db%", this.database);
        query = query.replaceAll("%t%", this.prefix);
        
        return query;
    }
    
    public boolean connect() {
        try {
            tryConnect();
            return true;
        } catch(ClassNotFoundException e) {
            error("You don't appear to have the MySQL Driver Installed.", e);
            return false;
        } catch(SQLException e) {
            error("Failed to Connect to the Database!", e);
            return false;
        }
    }
    
    public void tryConnect() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://"+host+":"+port+"/";
        connection = DriverManager.getConnection(url,username,password);
    }
    
    public boolean query(String query) {
        try {
            return tryQuery(query);
        } catch(SQLException e) {
            error("Query Error!", e);
            return false;
        }
    }
    
    public boolean tryQuery(String query) throws SQLException {
        query = prepareQuery(query);
        Base.debug("[SQL] Running: \"" + query + "\"");
        PreparedStatement sqlStmt = connection.prepareStatement(query);
        boolean result = sqlStmt.execute(query);
        return true;
    }
    
    public long queryReturnID(String query) {
        try {
            return tryQueryReturnID(query);
        } catch(SQLException e) {
            error("Query Error!", e);
            return -1;
        }
    }
    
    public long tryQueryReturnID(String query) throws SQLException {
        query = prepareQuery(query);
        Base.debug("[SQL] Running (ID Return): \"" + query + "\"");
        Statement stmt = connection.createStatement();
        stmt.execute(query, Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = stmt.getGeneratedKeys();
        rs.next();
        return rs.getLong(1);
    }
    
    public List<Map<String, String>> fetch(String query) {
        try {
            return tryFetch(query);
        } catch(Exception e) {
            return new ArrayList<Map<String, String>>();
        }
    }
    
    public List<Map<String, String>> tryFetch(String query) throws SQLException {
        query = this.prepareQuery(query);
        Base.debug("[SQL] Fetching: \"" + query + "\"");
        Statement stmt = connection.createStatement();
        ResultSet result = stmt.executeQuery(query);
        
        List<Map<String, String>> results = new ArrayList<Map<String, String>>();
        
        while (result.next()){
            Map<String, String> data = new HashMap<String, String>();
            for(int i = 1; i <= result.getMetaData().getColumnCount(); i++) {
                data.put(result.getMetaData().getColumnName(i), result.getString(result.getMetaData().getColumnName(i)));
            }
            results.add(data);
        }
        
        return results;
    }
    
    @Override
    public void tryLoad() throws IOException {
        Base.useSQL = false;
        if(connection == null) return;
        
        //Create Tables
        InputStream queries = getPlugin().getResource("dbqueries.yml");
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(queries);
        
        for(String s : yml.getKeys(false)) {
            String query = yml.getString(s);
            if(!query(query)) throw new IOException ("Failed to create " + s + " table.");
        }
        
        Base.useSQL = true;
    }

    public String dateToSQL(Date date) {
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String now = ft.format(date);
        return now;
    }
    
    public Date sqlToDate(String sqlDate) {
        SimpleDateFormat fat = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        Date returnDate = new Date();
        try {
            returnDate = fat.parse(sqlDate);
        } catch (ParseException ex) {
            return new Date();
        }
        return returnDate;
    }
}
