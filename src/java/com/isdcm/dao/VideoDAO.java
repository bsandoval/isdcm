package com.isdcm.dao;

import com.isdcm.dto.VideoDTO;
import com.isdcm.jdbc.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VideoDAO {
    
    
    public void insertVideo(VideoDTO video){
        try {
            DataSource ds = DataSource.getInstance();
            //here mydb is database name, mydb is username and password
            String insertNewUserSQL = "INSERT INTO MYDB.VIDEO (TITULO, AUTOR, FECHA_CREACION, DURACION, REPRODUCCIONES, DESCRIPCION, FORMATO) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = ds.getConnection().prepareStatement(insertNewUserSQL);
            pstmt.setString(1, video.getTitulo());
            pstmt.setString(2, video.getAutor());
            java.sql.Date date1 = new java.sql.Date(video.getFechaCreacion().toEpochDay());
            java.sql.Time time1 = new java.sql.Time(video.getDuracion().toNanoOfDay());
            pstmt.setDate(3, date1);
            pstmt.setTime(4, time1);
            pstmt.setInt(5, video.getReproducciones());
            pstmt.setString(6, video.getDescripcion());
            pstmt.setString(7, video.getFormato());
            pstmt.executeUpdate();
            ds.getConnection().close();  
        }
        catch(SQLException e){ System.out.println(e);}
    }
    
    public List getVideos(){
        List<VideoDTO> L = new ArrayList<VideoDTO>();
        try{
            DataSource ds = DataSource.getInstance();
            String sql = "SELECT * FROM MYDB.VIDEO";
            PreparedStatement statement = ds.getConnection().prepareStatement(sql);

            ResultSet result = statement.executeQuery();

            while(result.next()){
                VideoDTO video = new VideoDTO();
                video.setTitulo(result.getString("titulo"));
                video.setAutor(result.getString("autor"));
                video.setFechaCreacion(result.getDate("fecha_creacion").toLocalDate());
                video.setDuracion(result.getTime("duracion").toLocalTime());
                video.setReproducciones(result.getInt("reproducciones"));
                video.setDescripcion(result.getString("descripcion"));
                video.setFormato(result.getString("formato"));
                L.add(video);
            }
            ds.getConnection().close();
        }
        catch(SQLException e){ System.out.println(e);}
        return L;
    }
}
