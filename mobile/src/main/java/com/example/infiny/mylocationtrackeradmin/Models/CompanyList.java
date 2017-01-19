package com.example.infiny.mylocationtrackeradmin.Models;

import java.io.Serializable;

/**
 * Created by infiny on 19/1/17.
 */

public class CompanyList implements Serializable
{
    private String error;

    private User_list[] user_list;

    private String msg;

    public String getError ()
    {
        return error;
    }

    public void setError (String error)
    {
        this.error = error;
    }

    public User_list[] getUser_list ()
    {
        return user_list;
    }

    public void setUser_list (User_list[] user_list)
    {
        this.user_list = user_list;
    }

    public String getMsg ()
    {
        return msg;
    }

    public void setMsg (String msg)
    {
        this.msg = msg;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [error = "+error+", user_list = "+user_list+", msg = "+msg+"]";
    }
}
