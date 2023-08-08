package com.hongtai.nat.client.core.exception;

public class LoadConfigFailException extends RuntimeException{

    public LoadConfigFailException(String message){
        super(message);
    }

    public LoadConfigFailException(){
        super();
    }

}
