package org.example.fund_tracker_project.service;

import java.util.HashMap;
import java.util.Map;

public class GestorYahoo {

        private static final Map<String, String> SUFIJOS_YAHOO = new HashMap<>(); //voy a usar un hashmap que almacene un valor junto con su clave (sera el sufijo)
    //el sufijo de yahoo depende del mic, en el caso de activos de eeuu no se anhade nada

        static {
            // España
            SUFIJOS_YAHOO.put("XMAD", ".MC");
            SUFIJOS_YAHOO.put("BMEX", ".MC");

            // Europa
            SUFIJOS_YAHOO.put("XETR", ".DE");
            SUFIJOS_YAHOO.put("XFRA", ".F");
            SUFIJOS_YAHOO.put("XPAR", ".PA");
            SUFIJOS_YAHOO.put("XAMS", ".AS");
            SUFIJOS_YAHOO.put("XMIL", ".MI");
            SUFIJOS_YAHOO.put("XLON", ".L");
            SUFIJOS_YAHOO.put("XSWX", ".SW");
            SUFIJOS_YAHOO.put("XBRU", ".BR");
            SUFIJOS_YAHOO.put("XLIS", ".LS");

            // Otros mercados
            SUFIJOS_YAHOO.put("XTSE", ".TO");
            SUFIJOS_YAHOO.put("XTKS", ".T");
            SUFIJOS_YAHOO.put("XHKG", ".HK");
            SUFIJOS_YAHOO.put("XASX", ".AX");
            SUFIJOS_YAHOO.put("XSES", ".SI");
            SUFIJOS_YAHOO.put("XNSE", ".NS");
        }

        public static String construirTickerYahoo(String ticker, String micCode) {
            if (ticker == null || ticker.isBlank()) {
                return null;
            }

            String sufijo = SUFIJOS_YAHOO.getOrDefault(micCode, "");
            return ticker + sufijo;
        }
    }

