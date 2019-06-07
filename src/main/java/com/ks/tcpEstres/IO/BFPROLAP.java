package com.ks.tcpEstres.IO;

import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Data
public class BFPROLAP extends Reader
{
    public BFPROLAP(String filePath)
    {
        absoluteFilePath = filePath;
    }

    private List<String> lines;

    @Override
    void read(BufferedReader reader) throws IOException
    {
        lines = new LinkedList();
        String line;
        while ((line = reader.readLine()) != null)
        {
            lines.add(line);
        }
    }
}
