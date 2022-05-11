package com.eastnets.bicloader.endec;


public class ConnectionSettings
{

    private String userName;
    private String password;
    private String serviceName;
    private Integer portNumber;
    private String serverName;

    public ConnectionSettings()
    {
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getServiceName()
    {
        return serviceName;
    }

    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
    }

    public Integer getPortNumber()
    {
        return portNumber;
    }

    public void setPortNumber(Integer portNumber)
    {
        this.portNumber = portNumber;
    }

    public String getServerName()
    {
        return serverName;
    }

    public void setServerName(String serverName)
    {
        this.serverName = serverName;
    }
}
