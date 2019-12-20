package com.ks.tcpEstres;

import com.ks.tcpEstres.IO.BFPROLAP;
import com.ks.tcpEstres.communication.Conexion;
import com.ks.tcpEstres.model.Connection;

import java.util.Date;
import java.util.List;

public class Application
{
    public static void main(String[] args) throws Exception
    {
        System.out.println("Inicia sesion de pruebas " + new Date().toString());
        List<Connection> connections = new Connection().recoverConnections("./config/connectionsTest.json");

        connections.forEach(connection -> {
            try
            {
                BFPROLAP bfprolap = new BFPROLAP(connection.getFilesRoute());
                bfprolap.read();
                connection.setMessagesToSend(bfprolap.getLines());
            }
            catch (Exception e)
            {
                System.out.println("Error Leyendo el archivo " + connection.getFilesRoute());
            }
        });

        connections.forEach(Conexion::newConnection);
        System.out.println("Finalizo la tarea de " + connections.size() + " test(s)");
        System.exit(0);
    }
}
