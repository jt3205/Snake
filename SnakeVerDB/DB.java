package Project3_SnakeVerDB;

import java.sql.*;
import java.util.Scanner;
import java.util.Vector;

public class DB {

	Vector<String> name = new Vector<String>();
	Vector<Integer> point = new Vector<Integer>();

	DB() throws Exception {

		// TODO Auto-generated method stub

		Class.forName("oracle.jdbc.driver.OracleDriver");// oracle
		String url = "jdbc:oracle:thin:@localhost:1521:xe";// oracle join
		Connection c = DriverManager.getConnection(url, "hr", "hr");// connection

		String sql = "select * from rank order by point desc";// sql

		Statement st = c.createStatement();// statment

		ResultSet rs = st.executeQuery(sql);// result

		// 출력

		while (rs.next()) {// 출력while

			name.addElement(rs.getString(1));
			point.addElement(rs.getInt(2));

		}

		if (c != null)
			c.close();
	}

	DB(String name, int point) throws Exception {

		Class.forName("oracle.jdbc.driver.OracleDriver");// oracle
		String url = "jdbc:oracle:thin:@localhost:1521:xe";// oracle join
		Connection c = DriverManager.getConnection(url, "hr", "hr");// connection

		String i_n = name;
		int i_p = point;

		String sql = "insert into rank (name,point) VALUES(?,?)";

		PreparedStatement st = c.prepareStatement(sql);// statment
		st.setString(1, i_n);
		st.setInt(2, i_p);
		int abc = st.executeUpdate();

		if (c != null)
			c.close();
	}
}
