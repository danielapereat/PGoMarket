package com.example.gomarket;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class ListElement {
    public String nombre, descripcion, usuario, nit,ubicacion,  inicio, fin, contraseña,  nombrepersona, apellido, mail;
    public byte[] image;
    public int arrow, id,dias;

    public ListElement() {
    }

    //CONSTRUCTORES
    public ListElement(int id, String nit,  String nombre, String descripcion, String usuario,  String ubicacion, Integer dias, String inicio, String fin, byte[] image, int arrow) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.usuario = usuario;
        this.nit = nit;
        this.ubicacion = ubicacion;
        this.dias = dias;
        this.inicio = inicio;
        this.fin = fin;
        this.image = image;
        this.arrow = arrow;
        this.id = id;
    }


    public ListElement(String usuario, String contraseña, String nombrepersona, String apellido, String mail) {
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.nombrepersona = nombrepersona;
        this.apellido = apellido;
        this.mail = mail;
    }



    //SETERS
    public void setMail(String mail) { this.mail = mail; }

    public void setNombrepersona(String nombrepersona) {
        this.nombrepersona = nombrepersona;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setDias(Integer dias) {
        this.dias = dias;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    //GETTERS

    public String getMail() { return mail; }

    public String getNombrepersona() {
        return nombrepersona;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getNit() {
        return nit;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public Integer getDias() {
        return dias;
    }

    public String getInicio() {
        return inicio;
    }

    public String getFin() {
        return fin;
    }

    public byte[] getImage() {
        return image;
    }

    public String getContraseña() {
        return contraseña;
    }

    public String getApellido() {
        return apellido;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArrow() {
        return arrow;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        System.out.println("holssssss   "+ descripcion);
        return descripcion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }




}
