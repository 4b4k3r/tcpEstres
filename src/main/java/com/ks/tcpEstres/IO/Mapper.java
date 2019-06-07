package com.ks.tcpEstres.IO;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@Data
public class Mapper
{
    protected static final ObjectMapper mapper = new ObjectMapper();

    protected <T> List readAndGetList(String sourceFile, Class<T> sourceClass) throws Exception
    {
        try
        {
            if (!new File(sourceFile).exists())
            {
                throw new Exception("El archivo " + sourceFile + " no existe");
            }

            return (Arrays.asList(mapper.readValue(new File(sourceFile), getClassArray(sourceClass))));
        }
        catch (Exception e)
        {
            throw new Exception("Error al convertir json debido a: " + e);
        }
    }

    private <T> Class<? extends T[]> getClassArray(Class<T> tClass)
    {
        return  (Class<? extends T[]>) Array.newInstance(tClass, 0).getClass();
    }
}
