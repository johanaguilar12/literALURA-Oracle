package com.alura.literALURA.service;


public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
