package com.ugb.controlesbasicos;

public class utilidades {
    static String urlConsulta = "http://192.168.1.3:5984/amigos/_design/amigos/_view/amigos";
    static String urlMto = "http://192.168.1.3:5984/amigos";

    public String generarIdUnico(){
        return java.util.UUID.randomUUID().toString();
    }
}
