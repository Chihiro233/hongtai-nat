package com.hongtai.nat.client.core.exception;

public class ConnectFailException extends RuntimeException{


    public ConnectFailException(){
        super();
    }

    public ConnectFailException(String message){
        super(message);
    }
}
