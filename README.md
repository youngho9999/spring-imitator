# ✨ 자바로 직접 구현해보는 스프링 프레임워크 (My Own Spring) ✨

[![Java Version](https://img.shields.io/badge/Java-17%2B-blue.svg)](https://openjdk.java.net/)
[![Build Tool](https://img.shields.io/badge/Build-Gradle%20/%20Maven-brightgreen.svg)](https://gradle.org/) <!-- 사용하시는 빌드 툴에 맞게 수정하세요 -->

## 🌱 프로젝트 소개

이 프로젝트는 스프링 프레임워크의 핵심 기능들을 자바로 직접 구현해보면서, 그 내부 동작 원리를 깊이 있게 학습하고 이해하는 것을 목표로 합니다.

스프링을 사용하면서 편리하게 활용했던 기능들이 어떤 과정을 통해 동작하는지 직접 코드로 작성하며 '마법' 뒤에 숨겨진 원리들을 파헤쳐보고자 합니다.

## 🎯 프로젝트 목표

*   **IoC (Inversion of Control) 컨테이너**: Bean 정의, 생성, 관리 및 생명주기(Lifecycle) 직접 구현하기
*   **DI (Dependency Injection)**: 의존성 주입(생성자 주입, Setter 주입 등) 메커니즘 구현하기
*   **AOP (Aspect-Oriented Programming)**: 프록시(Proxy) 기반의 AOP 동작 원리 이해 및 구현하기
*   **웹 MVC**: DispatcherServlet, HandlerMapping, HandlerAdapter 등 스프링 MVC의 기본 구조 구현해보기

## 🔧 구현된 기능 (또는 구현 예정)

*   [ ] **IoC 컨테이너/ 의존성 주입 (DI)**:
    *   [X] `@Component` 스캔
    *   [X] 필드 기반 의존성 주입
    *   [ ] Setter 기반 의존성 주입
    *   [ ] 생성자 기반 의존성 주입
    *   [ ] `@Configuration`, `@Bean` 처리
*   [ ] **AOP**:
    *   [ ] JDK Dynamic Proxy 또는 CGLIB 기반 프록시 생성
    *   [ ] Pointcut, Advice 개념 구현
*   [ ] **기타**:
    *   [ ] (추가 기능이나 구현 중인 내용 기입)

*(구현 완료된 항목은 `[x]` 로 표시해주세요)*

## 🛠️ 기술 스택

*   **Language**: Java 17
*   **Build Tool**: Gradle 8.10
*   **Testing**: JUnit 5
*   **Library**: Reflections


## 📚 학습 여정 및 블로그

이 프로젝트를 진행하며 배우고 느낀 점, 마주친 문제와 해결 과정 등을 블로그에 꾸준히 기록하고 있습니다. 스프링 내부 구현에 대한 더 자세한 이야기가 궁금하시다면 아래 링크를 방문해주세요!

🔗 **스프링 직접 구현하기 시리즈**: **https://velog.io/@aplbly/series/%EB%82%98%EB%A7%8C%EC%9D%98-%EC%8A%A4%ED%94%84%EB%A7%81-%EB%A7%8C%EB%93%A4%EA%B8%B0**

🔗 **[1편] IoC와 DI 구현**: https://velog.io/@aplbly/%EC%8A%A4%ED%94%84%EB%A7%81-%EC%A7%81%EC%A0%91-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0-1-IoC%EC%99%80-DI-%EA%B5%AC%ED%98%84

