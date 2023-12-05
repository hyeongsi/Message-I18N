<h3>기능</h3>

* 사용자 선택에 따른 국제화 기능 구현

<h3>목적</h3>

* 다양한 LocaleResolver를 통한 메시지 및 국제화 기능 학습
<br><br><br>

<h2>프로젝트 환경 설정</h2>
https://start.spring.io/
<br><br>

* 의존관계 설정
  * Thymeleaf
  * Lombok
* Spring Web
  * Gradle
  * Java 8
  * Jar
 <br><br><br>
    
<h2>국제화 기능 구현</h2>

스프링은 `LocaleResolver` 인터페이스를 통해 **클라이언트의 언어 & 국가 정보를 인식**한다. 스프링 MVC는 `LocaleResolver`로 웹 요청과 관련된 `Locale` **객체를 추출**하여 알맞는 **언어 메시지를 선택**하게 된다.  
<br>
국제화는 메시지 기능을 토대로 구현할 수 있다. **메시지 기능을 구현**하기 위한 **파일을 먼저 생성**한다. 파일명은 스프링 부트가 기본으로 제공하는 `messages`를 사용한다.

<br><br>
메시지, 국제화에 대한 기초 지식은 아래의 링크를 확인해보길 바란다.<br>
https://coding-chronicle.tistory.com/106
 <br><br><br>

<h3>생성한 국가별 메시지 properties 파일</h3>

`src/resources/messages.properties` 와 기타 국가의 `properties`를 생성
<br><br>

<h3>🔴 application.properties</h3>

스프링 부트는 자동으로 `LocaleResolver`를 등록하고 관련 설정을 해준다.<br>

다루게 될 메시지 파일의 이름( **기본 messages** )와 인코딩 ( **기본 UTF-8** )를 `application.properties`에 작성한다. 이 둘은 기본값이라 생략해도 무방하다.

```
spring.messages.basename=messages
spring.messages.encoding=UTF-8
```

<h3>🔴 messages.properties</h3>

```
hello=안녕
hello.name=안녕 {0}

title=메시지, 국제화 테스트
subtitle=스프링

label.select=언어 선택

option.select=== 언어 선택 ==
option.lang.ko=한국어
option.lang.en=영어
option.lang.ja=일본어

button.submit=전송
```

기본 메시지 파일이다. 한국어를 기본 메시지 파일로 설정했고, 메시지는 ke와 value 형태로 사용한다.
<h3>🔴 messages_en.properties</h3>

```
hello=hello
hello.name=hello {0}

title=message, i18n test
subtitle=spring

label.select=select language

option.select=== select language ==
option.lang.ko=Korean
option.lang.en=English
option.lang.ja=Japanese

button.submit=submit
```

<br>

<h3>🔴 messages_ja.properties</h3>

```
hello=こんにちは
hello.name=こんにちは {0}

title=メッセージ、国際化テスト
subtitle=春

label.select=言語を選択

option.select=== 言語を選択 ==
option.lang.ko=韓国語
option.lang.en=英語
option.lang.ja=日本語

button.submit=転送
```

<h3>🔴 LocaleResolver 인터페이스</h3>

```java
public interface LocaleResolver {

	/**
	 * Resolve the current locale via the given request.
	 * Can return a default locale as fallback in any case.
	 * @param request the request to resolve the locale for
	 * @return the current locale (never {@code null})
	 */
	Locale resolveLocale(HttpServletRequest request);

	/**
	 * Set the current locale to the given one.
	 * @param request the request to be used for locale modification
	 * @param response the response to be used for locale modification
	 * @param locale the new locale, or {@code null} to clear the locale
	 * @throws UnsupportedOperationException if the LocaleResolver
	 * implementation does not support dynamic changing of the locale
	 */
	void setLocale(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Locale locale);

}
```

먼저 `LocaleResolver`에 대해 알아보자<br>

- `resolveLocale()` 메서드는 요청에 관련된 `Locale`을 리턴한다. `DispatcherServlet`은 등록되어 있는 `LocaleResolver`의
 `resolverLocale()` 메서드를 호출해서 사용할 `Locale`을 구한뒤 `Locale`에 맞게 웹 요청을 처리한다.

<br>

 스프링은 `LocaleResolver` 인터페이스의 다양한 구현체를 제공한다.


|클래스|설명|
|:--|:--|
|AcceptHeaderLocaleResolver|웹 브라우저가 전송한 헤더의 `Accept-Language` 값을 기반으로 `Locale`을 선택한다. `setLocale()` 메서드를 지원하지 않는다.|
|SessionLocaleResolver|세션으로부터 `Locale` 정보를 구한다. `setLocale()` 메서드는 세션에 `Locale` 정보를 저장한다.|
|CookieLocaleResolver|쿠키를 이용해서 `Locale` 정보를 구한다. `setLocale()` 메서드는 쿠키에 `Locale` 정보를 저장한다.|
|FixedLocaleResolver|웹 요청에 상관없이 특정한 `Locale`로 설정한다. `setLocale()` 메서드를 지원하지 않는다.|
	

참고로 `LocaleResolver`를 직접 스프링 빈으로 등록할 경우 빈의 이름을 `localeResolver`로 등록해야 한다.

그리고 `LocaleResolver`의 구현체가 어떤 것이냐에 따라서 `Locale` 지정 방식이 달라진다.

<br><br>
<h2>1. SessionLocaleResolver를 스프링 빈에 등록</h2>

<h3>🔴 WebConfig</h3>

```java
@Configuration
public class WebConfig {

    @Bean
    public LocaleResolver localeResolver() {
        return new SessionLocaleResolver();
    }
}
```

<h2>2. 컨트롤러에서 세션 값 처리</h2>

<h3>🔴 Language</h3>

`select` 형식으로 언어를 선택하여 변경 버튼을 누르면 해당 페이지들이 선택한 언어에 맞게 `Locale` 값이 변경되도록 할 것이다. `select` 안에 들어갈 값들을 동적으로 출력하기 위해 `enum`으로 정의한다.

`lang`값을 ko, en, ja라고 한 이유는 `select`의 `option`값을 `lang`값으로 해당 언어가 나오도록 설정하기 위함이다.

```java
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
```

<h3>🔴 LanguageController</h3>

```java
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
```

뷰에서 사용자가 변경할 언어를 선택해서 전송하면 `languageDto`가 해당 정보를 받고 
`languageDto.getLocalte()`을 통해 값을 가져와서
`session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale)` 를 통해 세션에 `locale` 정보를 지정하여 언어를 변경하게 된다.

<br>

`localeResolver`의 구현체로 `SessionLocaleResolver`를 등록시켰기 때문에 세션값을 토대로 언어가 변경된다.

<br>

<h3>🔴 SessionLocaleResolver</h3>

```java
public class SessionLocaleResolver extends AbstractLocaleContextResolver {

	public static final String LOCALE_SESSION_ATTRIBUTE_NAME = SessionLocaleResolver.class.getName() + ".LOCALE";

	private String localeAttributeName = LOCALE_SESSION_ATTRIBUTE_NAME;

	public void setLocaleAttributeName(String localeAttributeName) {
		this.localeAttributeName = localeAttributeName;
	}
```

`SessionLocaleResolver`의 `LOCALE_SESSION_NAME`은 **세션의 키값**으로 사용되며 이 **키값을 통해 사용 언어를 구분**한다.

<br>

<h2>2. 뷰 작업</h2>

<h3>🔴 home.html</h3>

```html
<!DOCTYPE html>
<html lang="ko" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>

<h1 th:text="#{title}">메시지, 국제화 테스트</h1>
<h2 th:text="#{hello.name(#{subtitle})}"></h2>
<form method="post" th:object="${languageDto}">
    <div th:text="#{label.select}">== 언어 선택 ==</div>
    <select th:field="*{locale}">
        <option value="" th:text="#{option.select}">언어 선택</option>
        <option th:each="language : ${languages}" th:with="lang=|option.lang.${language.lang}|"
                th:value="${language.locale}" th:text="#{${lang}}"></option>
    </select>

    <button type="submit" th:text="#{button.submit}">전송</button>
</form>

</body>
</html>
```

사용 언어를 선택하고 전송 버튼을 누르면 `LanguageController`의 `changeLocale(LanguageDto languageDto)`가 `locale` 정보를 받아 세션에 `locale`을 등록하여 사용 언어를 바꾼다.

<br>

``` html
 <option th:each="language : ${languages}" th:with="lang=|option.lang.${language.lang}|"
                th:value="${language.locale}" th:text="#{${lang}}"></option>
```


`language enum`값으로 반복을 돌며 `value`와 `text`를 세팅하게 된다. 이때 언어에 맞는 `text`값을 출력하기 위해 `th:with`를 통해 `properties`의 키값을 변수로 뽑은다음 해당 변수를 통해 메시지 조회하여 텍스트를 구성하도록 만들었다.

<br>

<h2>실행결과 (기본값, 한국어):</h2>

<img src="https://blog.kakaocdn.net/dn/bCxa9X/btsBvowrjJH/i9CGRndbXzPmjjaLILKkB1/img.png">

<h3>실행결과 (영어):</h3>

<img src="https://blog.kakaocdn.net/dn/ckgSQr/btsBvegmesP/2R0NE6GVYXTtmr0xRf09W1/img.png">

<h3>실행결과 (일본어):</h3>

<img src="https://blog.kakaocdn.net/dn/4nT90/btsBtHjjnc2/USgBFd8JjKkRHSp3SjMCe0/img.png">

<hr>

`SessionLocaleResolver`외에도 `CookieLocaleResolver`나 기타 `LoacleResolver`를 통해 국제화를 구현할 수 있습니다.

<br>

꼭 국제화가 아니더라도 이 기능을 활용해 복잡한 무언가의 값을 관리하거나 변경할 때 사용하면 도움이 될 것 같습니다.


<br><br><br>

👀 참고자료

쿠릉쿠릉 쾅쾅 2022.04.18. [[Spring] 기본 메시지 기능 V2 - 다양한 LocaleResolver 구현을 통한 국제화 기능 구현](https://terry9611.tistory.com/304)
