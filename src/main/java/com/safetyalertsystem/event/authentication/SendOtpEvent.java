package com.safetyalertsystem.event.authentication;

import org.springframework.context.ApplicationEvent;
import com.safetyalertsystem.entity.user.User;

public class SendOtpEvent extends ApplicationEvent 
{
    private final User user;

    public SendOtpEvent(Object source, User user) 
    {
        super(source);
        this.user = user;
    }

    public User getUser() 
    {
        return user;
    }
}
