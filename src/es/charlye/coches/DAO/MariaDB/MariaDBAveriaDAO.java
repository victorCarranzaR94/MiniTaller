package es.charlye.coches.DAO.MariaDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import es.charlye.coches.DAO.AveriaDAO;
import es.charlye.coches.Exception.DAOException;
import es.charlye.coches.Modelo.Averia;

public class MariaDBAveriaDAO implements AveriaDAO {

	final String INSERT="INSERT INTO AVERIA(ID_VEHI,FECHA_AVERIA,COMEN_AVERIA,PRECIO_REPUESTO,PRECIO_COBRADO,KM_ACTUAL) VALUES (?,?,?,?,?,?)";
	final String UPDATE="UPDATE AVERIA SET ID_AVERIA=?,ID_VEHI=?,FECHA_AVERIA=?,COMEN_AVERIA=?,PRECIO_REPUESTO=?,PRECIO_COBRADO=?,KM_ACTUAL=?";
	final String DELETE="DELETE FROM AVERIA WHERE ID_AVERIA=?";
	final String GETALL="SELECT ID_AVERIA,ID_VEHI,FECHA_AVERIA,COMEN_AVERIA,PRECIO_REPUESTO,PRECIO_COBRADO,KM_ACTUAL FROM AVERIA";
	final String GETGROUP="SELECT ID_AVERIA,ID_VEHI,FECHA_AVERIA,COMEN_AVERIA,PRECIO_REPUESTO,PRECIO_COBRADO,KM_ACTUAL FROM AVERIA WHERE ID_VEHI = ? ORDER BY FECHA_AVERIA DESC";
	final String GETONE="SELECT ID_AVERIA,ID_VEHI,FECHA_AVERIA,COMEN_AVERIA,PRECIO_REPUESTO,PRECIO_COBRADO,KM_ACTUAL FROM AVERIA WHERE ID_AVERIA = ?";
	final String GETMONTH="SELECT ID_AVERIA,ID_VEHI,FECHA_AVERIA,COMEN_AVERIA,PRECIO_REPUESTO,PRECIO_COBRADO,KM_ACTUAL FROM AVERIA WHERE FECHA_AVERIA BETWEEN ? AND LAST_DAY(?)";
		
	private Connection conn;
	private List<Averia> averias;
	
	public MariaDBAveriaDAO(Connection conn) {
		this.conn=conn;
	}
	
	@Override
	public void insertar(Averia a) throws DAOException {
		PreparedStatement stat=null;
		try {
			stat = conn.prepareStatement(INSERT);
			stat.setLong(1, a.getId_vehi());
			stat.setString(2, a.getFecha_averia());
			stat.setString(3, a.getComen_averia());
			stat.setDouble(4, a.getPrecio_repuesto());
			stat.setDouble(5, a.getPrecio_cobrado());
			stat.setInt(6, a.getKm());
			if(stat.executeUpdate()==0)
				throw new DAOException("Puede que no se haya guardado.");
		} catch (SQLException e) {
			throw new DAOException("Error en SQL", e);
		}finally{
			if(stat!=null)
				try {
					stat.close();
				} catch (SQLException e){
					throw new DAOException("Error en SQL", e);
				}
		}
	}

	@Override
	public void modificar(Averia a) throws DAOException {
		PreparedStatement stat=null;
		try {
			stat = conn.prepareStatement(UPDATE);
			stat.setLong(1, a.getId_averia());
			stat.setLong(2, a.getId_vehi());
			stat.setString(3, a.getFecha_averia());
			stat.setString(4, a.getComen_averia());
			stat.setDouble(5, a.getPrecio_repuesto());
			stat.setDouble(6, a.getPrecio_cobrado());
			if(stat.executeUpdate()==0)
				throw new DAOException("Puede que no se haya guardado.");
		} catch (SQLException e) {
			throw new DAOException("Error en SQL", e);
		}finally{
			if(stat!=null)
				try {
					stat.close();
				} catch (SQLException e){
					throw new DAOException("Error en SQL", e);
				}
		}
		
	}

	@Override
	public void eliminar(Averia a) throws DAOException {
		PreparedStatement stat=null;
		try {
			stat = conn.prepareStatement(DELETE);
			stat.setLong(1, a.getId_averia());
			if(stat.executeUpdate()==0)
				throw new DAOException("Puede que no se haya eliminado.");
		} catch (SQLException e) {
			throw new DAOException("Error en SQL", e);
		}finally{
			if(stat!=null)
				try {
					stat.close();
				} catch (SQLException e){
					throw new DAOException("Error en SQL", e);
				}
		}
		
	}

	@Override
	public List<Averia> obtenerTodos() throws DAOException {
		averias=new ArrayList<Averia>();
		PreparedStatement stat=null;
		try {
			stat = conn.prepareStatement(GETALL);
			ResultSet set=stat.executeQuery();
			while(set.next())
				averias.add(new Averia(set.getLong(1),set.getLong(2),set.getString(3),set.getString(4),set.getDouble(5),set.getDouble(6),set.getInt(7)));
		} catch (SQLException e) {
			throw new DAOException("Error en SQL", e);
		}finally{
			if(stat!=null)
				try {
					stat.close();
				} catch (SQLException e){
					throw new DAOException("Error en SQL", e);
				}
		}
		return averias;
	}

	@Override
	public Averia obtener(Long id) throws DAOException {
		PreparedStatement stat=null;
		Averia averia=null;
		try {
			stat = conn.prepareStatement(GETONE);
			stat.setLong(1, id);
			ResultSet set=stat.executeQuery();
			set.next();
			averia=new Averia(set.getLong(1),set.getLong(2),set.getString(3),set.getString(4),set.getDouble(5),set.getDouble(6),set.getInt(7));
		} catch (SQLException e) {
			throw new DAOException("Error en SQL", e);
		}finally{
			if(stat!=null)
				try {
					stat.close();
				} catch (SQLException e){
					throw new DAOException("Error en SQL", e);
				}
		}
		return averia;
	}
	
	/*
	 * id del vehiculo
	 */
	public List<Averia> obtenerGrupo(Long id) throws DAOException {
		averias = new ArrayList<Averia>();
		PreparedStatement stat=null;
		try {
			stat = conn.prepareStatement(GETGROUP);
			stat.setLong(1, id);
			ResultSet set=stat.executeQuery();
			while(set.next()){
				Averia averia=new Averia(set.getLong(1),set.getLong(2),set.getString(3),set.getString(4),set.getDouble(5),set.getDouble(6),set.getInt(7));
				averias.add(averia);
			}		
		} catch (SQLException e) {
			throw new DAOException("Error en SQL", e);
		}finally{
			if(stat!=null)
				try {
					stat.close();
				} catch (SQLException e){
					throw new DAOException("Error en SQL", e);
				}
		}
		return averias;
	}

	@Override
	public List<Averia> obtenerPorFecha(String fecha) throws DAOException {
		averias =new ArrayList<Averia>();
		PreparedStatement stat=null;
		try {
			stat = conn.prepareStatement(GETMONTH);
			stat.setString(1, fecha+"-1");
			stat.setString(2, fecha+"-1");
			ResultSet set=stat.executeQuery();
			while(set.next())
				averias.add(new Averia(set.getLong(1),set.getLong(2),set.getString(3),set.getString(4),set.getDouble(5),set.getDouble(6),set.getInt(7)));
		} catch (SQLException e) {
			throw new DAOException("Error en SQL", e);
		}finally{
			if(stat!=null)
				try {
					stat.close();
				} catch (SQLException e){
					throw new DAOException("Error en SQL", e);
				}
		}
		return averias;
	}
}
