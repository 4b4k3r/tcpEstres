package com.ks.tcpEstres.model;

import com.ks.tcpEstres.IO.Mapper;
import lombok.Data;

import java.util.List;

@Data
public class Connection extends Mapper
{
    private List<Connection> connections;

    private String name;
    private String ip;
    private Integer port;
    private Integer quantity;
    private String filesRoute;
    private List<String> messagesToSend;
    private Integer duration;
    private Boolean validateConnection;

    public List<Connection> recoverConnections(String url) throws Exception
    {
        return connections = readAndGetList(url, Connection.class);
    }
}
