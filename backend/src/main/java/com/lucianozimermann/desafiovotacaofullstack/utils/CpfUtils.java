package com.lucianozimermann.desafiovotacaofullstack.utils;

public class CpfUtils
{
    public static String stripCpfMask(String cpf) {
        return cpf.replaceAll("\\D", "");
    }
}
