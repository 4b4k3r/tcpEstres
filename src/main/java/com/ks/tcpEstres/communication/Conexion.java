package com.ks.tcpEstres.communication;

import com.ks.lib.tcp.Cliente;
import com.ks.lib.tcp.EventosTCP;
import com.ks.lib.tcp.Tcp;
import com.ks.tcpEstres.model.Connection;
import lombok.Data;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

@Data
public class Conexion implements EventosTCP
{
    private static final Hashtable<String, List<Conexion>> conexiones = new Hashtable();
    private static int sequential = 1;
    private Tcp tcp;
    private List<String> messagesToSend;
    private int connectionNumber;
    private int duration;
    private boolean validateConnection;
    private Thread sendThread;
    private long sendMessages;
    private String name;
    private boolean isValid;
    private static int tasksFinished;
    private static int totalMessages;

    public static void newConnection(Connection connection)
    {
        int counter = 1;
        try
        {
            while (sequential <= connection.getQuantity())
            {
                Conexion conexion = new Conexion();
                conexion.setMessagesToSend(connection.getMessagesToSend());
                conexion.setConnectionNumber(sequential);
                conexion.setDuration(connection.getDuration());
                conexion.validateConnection = connection.getValidateConnection();
                conexion.setName(connection.getName());
                sequential++;

                Tcp tcp = new Cliente();

                tcp.setIP(connection.getIp());
                tcp.setPuerto(connection.getPort());
                tcp.setEventos(conexion);
                conexion.setTcp(tcp);

                List<Conexion> conexions = conexiones.getOrDefault(connection.getName(), new LinkedList());

                System.out.println("Se ha configurado la conexion numero " + counter + " de las pruebas " + connection.getName());
                conexions.add(conexion);
                conexiones.put(connection.getName(), conexions);
                counter++;
                conexion.getTcp().conectar();
            }

            while (tasksFinished != connection.getQuantity())
            {
                Thread.sleep(500);
            }

            conexiones.get(connection.getName()).forEach(conexion -> ((Cliente) conexion.tcp).cerrar());
            System.out.println("Finalizo la ejecucion de " + tasksFinished + " conexiones de la prueba " + connection.getName() + " de tipo cliente enviando un total de " + totalMessages + " mensaje(s)");
        }
        catch (Exception e)
        {
            System.out.println("Error al configurar alguna conexion del objeto de conexiones " + connection.getName());
        }
    }

    private Conexion()
    {
        sendThread = new Thread();
        sendMessages = 0;
        isValid = false;
        tasksFinished = 0;
        totalMessages = 0;
    }

    public void conexionEstablecida(Cliente cliente)
    {
        System.out.println("Conexon establecida de la conexion " + name + " numero " + connectionNumber);

        try
        {
            if (!this.sendThread.isAlive())
            {
                sendThread = new Thread(this::asyncSend);
                sendThread.setDaemon(true);
                sendThread.setName("Client" + connectionNumber);
                sendThread.start();
            }
        }
        catch (Exception e)
        {
            System.out.println("Error al enviar la cola de mensajes desde la conexion " + name + " numero " + connectionNumber + "debido a: " + e);
        }
    }

    private void asyncSend()
    {
        try
        {
            if (isValidateConnection())
            {
                while (!isValid)
                {
                    Thread.sleep(200);
                }
            }

            System.out.println("Enviando cola de mensajes desde la conexion " + name + " numero " + connectionNumber + " durante " + duration + " minuto(s)");

            final long limitTime = System.currentTimeMillis() + (duration * 60 * 1000);

            while (limitTime > System.currentTimeMillis())
            {
                this.messagesToSend.forEach(message -> {
                    if (((Cliente) this.tcp).isConnected())
                    {
                        this.tcp.enviar(message);
                        sendMessages++;

                        try
                        {
                            Thread.sleep(30);
                        }
                        catch (InterruptedException e)
                        {
                        }
                    }
                });
                Thread.sleep(10);
            }

            totalMessages += sendMessages;
            tasksFinished++;

            System.out.println("El envio de mensajes de la conexion :" + name + " numero " + connectionNumber + " ha finalizado con un total de " + sendMessages + " mesajes enviados");

            this.messagesToSend.clear();
        }
        catch (Exception e)
        {
            System.out.println("Error durante el envio de mensajes asincronos de la conexion " + name + " numero " + connectionNumber);
        }
    }

    public void errorConexion(String s)
    {
        //System.out.println("error de conexion: " + s);
    }

    public void datosRecibidos(String s, byte[] bytes, Tcp tcp)
    {
        //System.out.println("Mensaje recibido: " + s);
        if (isValidateConnection() && s.contains("SENDTRAN"))
        {
            System.out.println("Validando conexion " + name + " numero " + connectionNumber + " enviando SENDTRAN");
            this.tcp.enviar("SENDTRAN");
            isValid = true;
        }
    }

    public void cerrarConexion(Cliente cliente)
    {
        //System.out.println("Error de conexion");
    }
}
