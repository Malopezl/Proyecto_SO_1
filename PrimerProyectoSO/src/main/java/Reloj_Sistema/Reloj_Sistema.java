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
 * @author tito88
 */
public class Reloj_Sistema {

    public static String Hora_sistema;

    public static void setHora_sistema() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        Hora_sistema = dateFormat.format(date);
    }

    public static String getHora_sistema() {
        return Hora_sistema;
    }

}
