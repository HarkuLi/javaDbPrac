package com.harku.model;

public class UserFilter
{
    private String id;

    private String birthFrom;

    private String occupation;

    private String[] interest;

    private String name;

    private Boolean state;

    private String birthTo;
    
    private String token;
    
    private String account;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getBirthFrom ()
    {
        return birthFrom;
    }

    public void setBirthFrom (String birthFrom)
    {
        this.birthFrom = birthFrom;
    }

    public String getOccupation ()
    {
        return occupation;
    }

    public void setOccupation (String occupation)
    {
        this.occupation = occupation;
    }

    public String[] getInterest ()
    {
        return interest;
    }

    public void setInterest (String[] interest)
    {
        this.interest = interest;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public Boolean getState ()
    {
        return state;
    }

    public void setState (Boolean state)
    {
        this.state = state;
    }

    public String getBirthTo ()
    {
        return birthTo;
    }

    public void setBirthTo (String birthTo)
    {
        this.birthTo = birthTo;
    }
    
    public String getToken ()
    {
    	return token;
    }
    
    public void setToken (String token)
    {
    	this.token = token;
    }
    
    public String getAccount ()
    {
    	return account;
    }
    
    public void setAccount (String account)
    {
    	this.account = account;
    }
}