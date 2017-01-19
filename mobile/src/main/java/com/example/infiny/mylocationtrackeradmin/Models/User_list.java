package com.example.infiny.mylocationtrackeradmin.Models;

/**
 * Created by infiny on 19/1/17.
 */
public class User_list
{
    private String id;

    private String company_id;

    private String phone;

    private String address;

    private String user_type;

    private String description;

    private String name;

    private String track_id_reg;

    private String created_at;

    private String dateofbirth;

    private String email_id;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getCompany_id ()
    {
        return company_id;
    }

    public void setCompany_id (String company_id)
    {
        this.company_id = company_id;
    }

    public String getPhone ()
    {
        return phone;
    }

    public void setPhone (String phone)
    {
        this.phone = phone;
    }

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public String getUser_type ()
{
    return user_type;
}

    public void setUser_type (String user_type)
    {
        this.user_type = user_type;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getTrack_id_reg ()
    {
        return track_id_reg;
    }

    public void setTrack_id_reg (String track_id_reg)
    {
        this.track_id_reg = track_id_reg;
    }

    public String getCreated_at ()
    {
        return created_at;
    }

    public void setCreated_at (String created_at)
    {
        this.created_at = created_at;
    }

    public String getDateofbirth ()
    {
        return dateofbirth;
    }

    public void setDateofbirth (String dateofbirth)
    {
        this.dateofbirth = dateofbirth;
    }

    public String getEmail_id ()
    {
        return email_id;
    }

    public void setEmail_id (String email_id)
    {
        this.email_id = email_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", company_id = "+company_id+", phone = "+phone+", address = "+address+", user_type = "+user_type+", description = "+description+", name = "+name+", track_id_reg = "+track_id_reg+", created_at = "+created_at+", dateofbirth = "+dateofbirth+", email_id = "+email_id+"]";
    }
}
