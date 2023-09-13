package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.models.Client;

public class ClientUtils {

    public static boolean compareClientByMail (Client client1, Client client2){
        return client1.getEmail().equals(client2.getEmail());
    }
}
