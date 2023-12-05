package com.example.springmessagei18nsutdy.domain.language;

import lombok.Getter;

import java.util.Locale;

@Getter
public enum Language {
    KOREA("ko", Locale.KOREA),
    ENGLISH("en", Locale.ENGLISH),
    JAPAN("ja", Locale.JAPAN);

    private String lang;
    private Locale locale;

    Language(String lang, Locale locale) {
        this.lang = lang;
        this.locale = locale;
    }
}
