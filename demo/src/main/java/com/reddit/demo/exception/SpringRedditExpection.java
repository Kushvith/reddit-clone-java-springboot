package com.reddit.demo.exception;

public class SpringRedditExpection extends RuntimeException{
    public SpringRedditExpection(String exMessage){
        super(exMessage);
    }
}
