package com.ks.tcpEstres.IO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public abstract class Reader
{
    protected String absoluteFilePath;

    public void read() throws IOException
    {
        File dataFile = new File(absoluteFilePath);

        if (dataFile.exists())
        {
            BufferedReader reader = new BufferedReader(new FileReader(absoluteFilePath));
            this.read(reader);
            reader.close();
        }
        else
        {
            System.out.println("Arcvhivo: " + absoluteFilePath + " no existe");
        }
    }

    abstract void read(BufferedReader reader) throws IOException;
}