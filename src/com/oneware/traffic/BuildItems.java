package com.oneware.traffic;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import java.lang.reflect.Field;

public class BuildItems {
	private Connection connection = null;
	String serverName = "127.0.0.1";
	String portNumber = "3306";
	String sid = "mydb";
	String username = "root";
	String password = "1234";

	public BuildItems() {
		try {

			String driverName = "com.mysql.jdbc.Driver";
			Class.forName(driverName);

			// Create a connection to the database

			String url = "jdbc:mysql://" + serverName + ":" + portNumber + "/"
					+ sid;

			setConnection(DriverManager.getConnection(url, username, password));
			connection.setAutoCommit(false);
		} catch (SQLException d) {
			// Could not connect to the database
			d.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList getData(String SQLStatement) {
		ArrayList arry = new ArrayList();
		try {
			Statement statement = getConnection().createStatement();
			ResultSet result = statement.executeQuery(SQLStatement);
			ResultSetMetaData resultSetMetaData = result.getMetaData();
			int columnCount = resultSetMetaData.getColumnCount();

			HashMap dataMapStructure = new HashMap();
			for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
				dataMapStructure.put(resultSetMetaData.getColumnName(i),
						resultSetMetaData.getColumnType(i));

			}
			while (result.next()) {
				HashMap dataMap = new HashMap();
				Set set = dataMapStructure.entrySet();
				Iterator iterator = set.iterator();

				while (iterator.hasNext()) {
					Map.Entry map = (Map.Entry) iterator.next();
					dataMap.put(map.getKey(),
							result.getObject((String) map.getKey()));
				}
				arry.add(dataMap);
			}

			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arry;

	}

	public ArrayList getData(Object obj, String SQLStatement) {
		ArrayList arry = new ArrayList();
		try {
			Statement statement = getConnection().createStatement();
			ResultSet result = statement.executeQuery(SQLStatement);
			ResultSetMetaData resultSetMetaData = result.getMetaData();

			Object objInstance = new Object();
			while (result.next()) {
				try {
					objInstance = obj.getClass().newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				Field[] flds = objInstance.getClass().getDeclaredFields();

				for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
					try {
						char vchar = (char) 32;
						String currentColumn = resultSetMetaData
								.getColumnName(i).toLowerCase().trim();

						for (int q = 0; q < flds.length; q++) {
							if (flds[q]
									.getName()
									.replace("_", "")
									.equalsIgnoreCase(
											currentColumn.replace("_", ""))) {
								currentColumn = flds[q].getName();
								break;
							}
						}

						if (resultSetMetaData.getColumnType(i) == Types.VARCHAR) {
							objInstance
									.getClass()
									.getDeclaredField(currentColumn)
									.set(objInstance,
											result.getString(resultSetMetaData
													.getColumnName(i)));
						} else if (resultSetMetaData.getColumnType(i) == Types.INTEGER) {
							objInstance
									.getClass()
									.getDeclaredField(currentColumn)
									.set(objInstance,
											result.getInt(resultSetMetaData
													.getColumnName(i)));

						} else if (resultSetMetaData.getColumnType(i) == Types.DATE) {
							objInstance
									.getClass()
									.getDeclaredField(currentColumn)
									.set(objInstance,
											result.getDate(resultSetMetaData
													.getColumnName(i)));

						} else if (resultSetMetaData.getColumnType(i) == Types.BOOLEAN) {
							objInstance
									.getClass()
									.getDeclaredField(currentColumn)
									.set(objInstance,
											result.getBoolean(resultSetMetaData
													.getColumnName(i)));

						} else {
							objInstance
									.getClass()
									.getDeclaredField(currentColumn)
									.set(objInstance,
											(String) result
													.getObject(resultSetMetaData
															.getColumnName(i)));
						}

					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (NoSuchFieldException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				arry.add(objInstance);
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arry;
	}

	public boolean insert(String tableName, Object obj) {

		Statement statement;
		String sqlInsert = " insert into " + tableName + " (";
		String sqlValues = " values (";
		try {
			statement = getConnection().createStatement();

			ResultSet result = statement.executeQuery("select * from "
					+ tableName);
			ResultSetMetaData resultSetMetaData = result.getMetaData();

			Object objInstance = new Object();
			try {
				objInstance = obj.getClass().newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			Field[] flds = objInstance.getClass().getDeclaredFields();

			for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
				int autoIncrement = 0;
				String currentColumn = resultSetMetaData.getColumnName(i)
						.toLowerCase().trim();

				for (int q = 0; q < flds.length; q++) {
					if (flds[q].getName().replace("_", "")
							.equalsIgnoreCase(currentColumn.replace("_", ""))) {
						Annotation[] ann = flds[q].getAnnotations();
						if (ann.length > 0
								&& ann[0].toString()
										.endsWith("AutoIncrement()")) {
							autoIncrement = 1;
						}

						if (autoIncrement == 0) {
							sqlInsert = sqlInsert + "" + currentColumn + ",";
							try {

								if (flds[q].get(obj) != null) {
									if (flds[q].getType() == Date.class) {

										Date value = (Date) flds[q].get(obj);
										sqlValues = sqlValues + "'"
												+ (value.getYear() + 1900)
												+ "-" + (value.getMonth() + 1)
												+ "-" + value.getDate() + "',";
									} else if (flds[q].getType() == String.class) {
										String value = (String) flds[q]
												.get(obj);
										sqlValues = sqlValues + "'" + value
												+ "',";
									} else {

										Object value = flds[q].get(obj);
										sqlValues = sqlValues + "'" + value
												+ "',";

									}

								} else {

									sqlValues = sqlValues + "NULL,";
								}

							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							break;
						}

					}
				}

			}

			String SqlReady = sqlInsert.substring(0, sqlInsert.length() - 1)
					+ ")" + sqlValues.substring(0, sqlValues.length() - 1)
					+ ")";
			try {
				Statement stmt = getConnection().createStatement();

				int rows = stmt.executeUpdate(SqlReady);
				getConnection().commit();
				stmt.close();
				return true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block-
			e.printStackTrace();
		}
		return false;
	}

	
	
	/*to Update data for the object reflect DB 
	 * 
	 * 
	 * 
	 * 
	 * tableName is the table used in DB
	 * obj is the object of the class reflect the Data for table
	 */
	public boolean update(String tableName, Object obj) {
		Object objInstance = new Object();
		Statement statement;
		List<String> pk = new ArrayList();
		String pkColumns = "";

		try {
			objInstance = obj.getClass().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		try {
			statement = getConnection().createStatement();
			// ResultSet rs =
			// getConnection().getMetaData().getExportedKeys(null,null,
			// tableName);
			ResultSet columnRs = getConnection().getMetaData().getColumns(null,
					sid, tableName, null);
			String sqlCheck = "Select * from " + tableName;
			String sqlWhere = " where 1=1 and";

			Field[] flds = objInstance.getClass().getDeclaredFields();
			while (columnRs.next()) {
				for (int q = 0; q < flds.length; q++) {

					if (columnRs
							.getString("COLUMN_NAME")
							.replace("_", "")
							.equalsIgnoreCase(
									flds[q].getName().replace("_", ""))) {
						Annotation[] ann = flds[q].getAnnotations();
						for (int d = 0; d < ann.length; d++) {

							if (ann[d].toString().endsWith("Id()")) {

								sqlWhere = sqlWhere
										+ " "+columnRs.getString("COLUMN_NAME")
										+ " = ";
								if (flds[q].get(obj) != null) {
									if (flds[q].getType() == Date.class) {

										Date value = (Date) flds[q].get(obj);

										sqlWhere = sqlWhere + "'"
												+ (value.getYear() + 1900)
												+ "-" + (value.getMonth() + 1)
												+ "-" + value.getDate()
												+ "' and";
									} else if (flds[q].getType() == String.class) {
										String value = (String) flds[q]
												.get(obj);

										sqlWhere = sqlWhere + "'" + value
												+ "' and";
									} else {

										Object value = flds[q].get(obj);

										sqlWhere = sqlWhere + "'" + value
												+ "' and";
									}

								} else {

									sqlWhere = sqlWhere + "NULL and";
								}

							}

						}

					}
				}
			}
			/*
			 * while (rs.next()) { //pk.add(rs.getString("PKCOLUMN_NAME"));
			 * //pkColumns = pkColumns + rs.getString("PKCOLUMN_NAME") + ",";
			 * 
			 * sqlCheck = sqlCheck + rs.getString("PKCOLUMN_NAME") + "=";
			 * 
			 * 
			 * 
			 * 
			 * for (int q = 0; q < flds.length; q++) { Field[] flds =
			 * objInstance.getClass().getDeclaredFields(); if
			 * (rs.getString("PKCOLUMN_NAME") .replace("_", "")
			 * .equalsIgnoreCase( flds[q].getName().replace("_", ""))) {
			 * 
			 * sqlWhere=sqlWhere+rs.getString("PKCOLUMN_NAME")+" = "; if
			 * (flds[q].get(obj) != null) { if (flds[q].getType() == Date.class)
			 * {
			 * 
			 * Date value = (Date) flds[q].get(obj);
			 * 
			 * 
			 * sqlWhere=sqlWhere+"'" + (value.getYear() + 1900) + "-" +
			 * (value.getMonth() + 1) + "-" + value.getDate() + "' and"; } else
			 * if (flds[q].getType() == String.class) { String value = (String)
			 * flds[q].get(obj);
			 * 
			 * sqlWhere=sqlWhere+ "'" + value + "' and"; } else {
			 * 
			 * Object value = flds[q].get(obj);
			 * 
			 * sqlWhere=sqlWhere+"'" + value + "' and"; }
			 * 
			 * } else {
			 * 
			 * 
			 * sqlWhere=sqlWhere+"NULL and"; }
			 * 
			 * } } }
			 */

			sqlWhere = sqlWhere.substring(0, sqlWhere.length() - 3);
			if (!getData(sqlCheck + sqlWhere).iterator().hasNext()) {
				insert(tableName, obj);
			} else {
				String sqlUpdate = "Update " + tableName + " set ";
				columnRs.beforeFirst();
				while (columnRs.next()) {

					flds = objInstance.getClass().getDeclaredFields();

					sqlUpdate = sqlUpdate + columnRs.getString("COLUMN_NAME")
							+ "=";
					for (int q = 0; q < flds.length; q++) {

						if (columnRs
								.getString("COLUMN_NAME")
								.replace("_", "")
								.equalsIgnoreCase(
										flds[q].getName().replace("_", ""))) {
							if (flds[q].get(obj) != null) {
								if (flds[q].getType() == Date.class) {

									Date value = (Date) flds[q].get(obj);
									sqlUpdate = sqlUpdate + "'"
											+ (value.getYear() + 1900) + "-"
											+ (value.getMonth() + 1) + "-"
											+ value.getDate() + "',";
								} else if (flds[q].getType() == String.class) {
									String value = (String) flds[q].get(obj);
									sqlUpdate = sqlUpdate + "'" + value + "',";
								} else {

									Object value = flds[q].get(obj);
									sqlUpdate = sqlUpdate + "'" + value + "',";

								}

							} else {

								sqlUpdate = sqlUpdate + "NULL ,";
							}
						}
					}
				}

				try {
					Statement stmt = getConnection().createStatement();

					int rows = stmt.executeUpdate(sqlUpdate.substring(0,
							sqlUpdate.length() - 1) + sqlWhere);
					getConnection().commit();
					stmt.close();
					return true;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		} catch (Exception e) {
			e.getMessage();
		}

		return false;

	}

	public boolean delete(String tableName, Object obj){
		Object objInstance = new Object();
		Statement statement;


		try {
			objInstance = obj.getClass().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		try {
			Field[] flds = objInstance.getClass().getDeclaredFields();
			ResultSet columnRs = getConnection().getMetaData().getColumns(null,
					sid, tableName, null);
			String sqlWhere=" where 1=1 and";
			while (columnRs.next()) {
				for (int q = 0; q < flds.length; q++) {

					if (columnRs
							.getString("COLUMN_NAME")
							.replace("_", "")
							.equalsIgnoreCase(
									flds[q].getName().replace("_", ""))) {
						Annotation[] ann = flds[q].getAnnotations();
						for (int d = 0; d < ann.length; d++) {

							if (ann[d].toString().endsWith("Id()")) {

								sqlWhere = sqlWhere
										+ " "+columnRs.getString("COLUMN_NAME")
										+ " = ";
								if (flds[q].get(obj) != null) {
									if (flds[q].getType() == Date.class) {

										Date value = (Date) flds[q].get(obj);

										sqlWhere = sqlWhere + "'"
												+ (value.getYear() + 1900)
												+ "-" + (value.getMonth() + 1)
												+ "-" + value.getDate()
												+ "' and";
									} else if (flds[q].getType() == String.class) {
										String value = (String) flds[q]
												.get(obj);

										sqlWhere = sqlWhere + "'" + value
												+ "' and";
									} else {

										Object value = flds[q].get(obj);

										sqlWhere = sqlWhere + "'" + value
												+ "' and";
									}

								} else {

									sqlWhere = sqlWhere + "NULL and";
								}

							}

						}

					}
				}
			}
			
			Statement stmt = getConnection().createStatement();

			int rows = stmt.executeUpdate("Delete from "+tableName + sqlWhere.substring(0, sqlWhere.length() - 3));
			getConnection().commit();
			stmt.close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return false;
	}
	
	
	
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Connection getConnection() {
		return connection;
	}

}
