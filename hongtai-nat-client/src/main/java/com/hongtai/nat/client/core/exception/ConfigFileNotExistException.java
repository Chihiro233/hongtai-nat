package com.hongtai.nat.client.core.exception;

public class ConfigFileNotExistException extends RuntimeException{

    public ConfigFileNotExistException(String message){
        super(message);
    }

    public ConfigFileNotExistException(){
        super();
    }

}
