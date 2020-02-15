package game;

import java.sql.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.StringUtils;
public class gameData {

    static final  String JDBC_DRIVER = "org.postgresql.Driver";
    static final String DB_URL = "jdbc:postgresql://52.24.215.108:5432/CT";
    // username and password of database
    static final String USER = "CT";
    static final String PASS = "CT";
    /**
     * @param args
     */
    public static int[] get(){
        Connection conn = null;
        Statement stmt = null;
        //winner = winner==666?0:winner;
        int total = 0;
        int human = 0;
        int ai = 0;
        int draws = 0;
        int longest = 0;
        try{
            // Registering the JDBC driver
            Class.forName(JDBC_DRIVER);

            // Open link
            //System.out.println("connect to the database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            ResultSet rs;
            String sql;
            stmt = conn.createStatement();
            // Execute query
            //System.out.println("instantiating a statement object...");

            sql = "SELECT count(id) as total FROM gamedatas";
            rs = stmt.executeQuery(sql);
            // expand database result set
            while(rs.next()) {
                // search by field
                total = rs.getInt("total");
                rs.close();
                break;
            }
            sql = "SELECT max(rounds) as rounds FROM gamedatas";
            rs = stmt.executeQuery(sql);
            // expand database result set
            while(rs.next()) {
                // search by field
                longest = rs.getInt("rounds");
                rs.close();
                break;
            }
            sql = "SELECT sum(player0) as human FROM gamedatas";
            rs = stmt.executeQuery(sql);
            // expand database result set
            while(rs.next()){
                // search by field
                human  = rs.getInt("human");
                rs.close();
                break;
            }
            sql = "SELECT sum(player1)+sum(player2)+sum(player3)+sum(player4) as ai FROM gamedatas";
            rs = stmt.executeQuery(sql);
            // expand database result set
            while(rs.next()){
                // search by field
                ai  = rs.getInt("ai");
                rs.close();
                break;
            }
            sql = "SELECT sum(draws)/count(id) as draws FROM gamedatas";
            rs = stmt.executeQuery(sql);
            // expand database result set
            while(rs.next()){
                // search by field
                draws  = (int) Math.floor(rs.getInt("draws"));
                rs.close();
                break;
            }

            stmt.close();
            conn.close();
        }catch(SQLException se){
            // handling JDBC errors
            se.printStackTrace();
        }catch(Exception e){
            // handling Class.forName errors
            e.printStackTrace();
        }finally{
            // Close resource
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// do nothing
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        int data[] = new int[5];
        data[0]=total;
        data[1]=human;
        data[2]=ai;
        data[3]=draws;
        data[4]=longest;//[,human,ai,draws,longest]
        return data;
    }
    public static void update(boolean c,int[] score,int draw,int rounds,int winner) {
        // TODO Auto-generated method stub
        Connection conn = null;
        Statement stmt = null;
        winner = winner==666?0:winner;
        try{
            //Registering the JDBC driver
            Class.forName(JDBC_DRIVER);

            // open link
            //System.out.println("connect to the database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            ResultSet rs;
            String sql;
            stmt = conn.createStatement();
            if(!c){
                // execute query
                //System.out.println("instantiating a statement object...");

                sql = "SELECT id FROM gamedatas order by id desc limit 1";
                rs = stmt.executeQuery(sql);

                // expand database result set
                while(rs.next()){
                    // search by field
                    int id  = rs.getInt("id");
                    //String name = rs.getString("name");
                    //String url = rs.getString("url");
                    String str = "";
                    String value[] = new String[score.length];
                    for(int i=0;i<score.length;i++){
                        value[i] = "player"+i+"="+score[i];
                    }
                    str = StringUtils.join(value,",");
                    sql = "update gamedatas set rounds='"+rounds+"',draws='"+draw+"',winner='"+winner+"',"+str+" where id='"+id+"'";
                    stmt.executeUpdate(sql);
                    // Output Data
                /*System.out.print("ID: " + id);
                System.out.print(", site name: " + name);
                System.out.print(", site URL: " + url);
                System.out.print("\n");
                */
                    rs.close();
                    break;
                }
                // close when done
            }else{
                String str;
                String valuea[] = new String[5];
                String value[] = new String[5];
                for(int i=0;i<score.length;i++){
                    valuea[i] = "player"+i;//+"="+score[i];
                    value[i] = score[i]+"";
                }
                for(int j=score.length;j<5;j++){
                    valuea[j] = "player"+j;
                    value[j] = 0+"";
                }
                String stra = StringUtils.join(valuea,",");
                str = StringUtils.join(value,",");
                sql = "insert into gamedata (rounds,draws,winner,"+stra+") VALUES ("+rounds+","+draw+",'"+winner+"',"+str+")";
                //System.out.println(sql);
                stmt.executeUpdate(sql);
            }
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // handling JDBC errors
            se.printStackTrace();
        }catch(Exception e){
            // handling Class.forName errors
            e.printStackTrace();
        }finally{
            // close resource
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// do nothing
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        //System.out.println("Goodbye!");
    }

}
