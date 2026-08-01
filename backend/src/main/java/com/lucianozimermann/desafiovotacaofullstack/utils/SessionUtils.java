package com.lucianozimermann.desafiovotacaofullstack.utils;

import com.lucianozimermann.desafiovotacaofullstack.entity.Session;

import java.time.LocalDateTime;

public class SessionUtils {

    public static boolean isOpen(Session session) {
        return session.getEndDate().isAfter(LocalDateTime.now());
    }
}