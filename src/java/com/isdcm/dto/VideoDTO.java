package com.isdcm.dto;

import com.isdcm.jdbc.DataSource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.sql.*;

public class VideoDTO {
    private int id;
    private String titulo;
    private String autor;
    private LocalDate fechaCreacion;
    private LocalTime duracion;
    private int reproducciones;
    private String descripcion;
    private String formato;
    
    public VideoDTO() {
    }
    
    public VideoDTO(int id, String ti, String au, LocalDate fe, LocalTime du, int re, String de, String fo){
        this.id = id;
        this.titulo = ti;
        this.autor = au;
        this.fechaCreacion = fe;
        this.duracion = du;
        this.reproducciones = re;
        this.descripcion = de;
        this.formato = fo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalTime getDuracion() {
        return duracion;
    }

    public void setDuracion(LocalTime duracion) {
        this.duracion = duracion;
    }

    public int getReproducciones() {
        return reproducciones;
    }

    public void setReproducciones(int reproducciones) {
        this.reproducciones = reproducciones;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }
}
