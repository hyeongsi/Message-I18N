package com.example.springmessagei18nsutdy.web.language;

import com.example.springmessagei18nsutdy.domain.language.Language;
import com.example.springmessagei18nsutdy.domain.language.LanguageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpSession;
import java.util.Locale;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LanguageController {

    @GetMapping
    public String home(@ModelAttribute LanguageDto languageDto, Model model) {
        model.addAttribute("languages", Language.values());
        return "/home";
    }

    @PostMapping
    public String changeLocale(@ModelAttribute LanguageDto languageDto, HttpSession session) {
        log.info(languageDto.toString());
        Locale locale = languageDto.getLocale();
        session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);
        return "redirect:/";
    }

}
