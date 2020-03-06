/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reloj_Sistema;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author gilda
 */
public class Reloj_Sistema {

    public static String Hora_sistema;
/**
 * Crea un formato de forma HH:mm:ss para la hora
 * Asigna la hora del sistema al atributo estatico
 */
    public static void setHora_sistema() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        Hora_sistema = dateFormat.format(date);
    }
/**
 * @return Devuelve la hora que se asigno
 * @deprecated 
 */
    public static String getHora_sistema() {
        return Hora_sistema;
    }

}
