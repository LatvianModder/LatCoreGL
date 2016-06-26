package com.latmod.latcoregl;

public enum Side
{
    CLIENT("Client", false, true),
    SERVER("Server", true, false),
    UNIVERSAL("Uni", true, true),
    NONE("None", false, false);

    public boolean isServer;
    public boolean isClient;
    private String name;

    Side(String s, boolean server, boolean client)
    {
        name = s;
        isServer = server;
        isClient = client;
    }

    @Override
    public String toString()
    {
        return name;
    }
}