# 1. AOP 개념

AOP는 Aspect Oriented Programing의 약자로, 관점 지향 프로그래밍을 뜻한다. 공통 관심사(Aspect)의 분리(모듈화)를 통해 소스코드의 중복의 해소 및 어플리케이션의 책임 원칙을 좀덕 획일화한다. 하나의 예는 트랜잭션 잡업과 인증 절차를 예를들 수 있다. 즉, 기능 중에 주 기능에만 집중하고, 부가 기능은 Aspect로 다 분리 시킨다. 

# 2. AOP 용어

Target: 부가기능을 부여할 대상을 뜻한다. 즉, 주기능을 얘기한다.


Aspect: AOP의 기본 모듈이다. 한개 또는 그 이상의 포인트컷과 어드바이스의 조합으로 만들어진다.


Advice: 실질적으로 부가 기능의 구현체이다. Advice의 유형은 다음과 같다. Before, After, Around, AfterReturning, AfterThrowing이 있다. 
 - Before: Target을 실행하기전에 부가 기능 실행
 - After: Target실행 후 (해당 Target Exception 또는 정상리턴 여부 상관없이) 실행
 - Around: Before + AfterReturning
 - AfterReturning: Target 실행 후 성공적인 리턴할 때
 - AfterThrowing: Target 실행하다, Exception 던질 때
 
 
Join-Point: Advice가 적용될 위치를 표시한다(ex:메소드 실행 단계).


Point-Cut: Advice를 적용할 Target를 선별하는 역할을 한다. Annotation 또는 메소드의 정규식을 통해 표현한다.


Cross-Cutting Concertn: 횡단 공통 관심사이다. 이것은 이미지를 통해 연상하는 것이 더 빠르다.


# 3. Spring AOP와 AspectJ
Spring AOP는 공통적인 문제를 해결하고자 Spring IoC를 통한 간단한 AOP 구현이 목적.  
반면 AspectJ는 완전한 AOP를 제공하는 것이 목적. Spring AOP보다 강력하지만 복잡.  
AspectJ는 모든객체에 적용이 가능.

# 4. AOP Weaving
Aspect(부가기능)와 Application(핵심기능)의 Linking을 하는 과정이다. 해당 객체들을 묶어 새로운 객체를 생성한다.

### Spring AOP
Spring AOP의 Weaving 절차는 RunTime이다.(IoC 컨테이너 초기화 작업할 때). 
인터페이스 기준으로 하는 JDK Dynamic Proxy와 Class 기준으로 하는 CGLib Proxy가 존재한다. CGLib Proxy의 경우, AspectJ의 Weaving 처럼 바이트 코드를 조작한다. SpringAOP는 JDK Dynamic Proxy 패턴을 선호한다. 또한 Proxy 패턴 자체가 인터페이스를 끼고하는 페턴이다. 

### AspectJ
실제, AJC(Apsect Compiler)를 이용하여 Woven System 생성한다. 즉 부가기능과 핵심기능이 합쳐진 클래스 파일을 생성한다. AspectJ의 Weaving 타입은 아래와 같다.  
**Compile-Time Weaving**: Aspect의 클래스와 Aspect를 사용하는 class들을 AJC를 통해 컴파일을 한다. JAR를 이용하여 Weaving을 하는 경우, Post-Compile Weaving(Binary Weaving)을 사용하며, 일반 소스 코드의 경우, 일반 Compile-Time Weaving을 사용한다.  
**Load-Time Weaving**: 클래스로더를 통해 클래스가 JVM에 로딩되는 시점에 클래스의 바이트 코드를 조작한다. 즉, 객체를 메모리에 적재할 때 Weaving을 실현한다. 때문에, 다른 Weaving보다 속도 측면에서는 느리다.


# 5. Spring AOP, AspectJ 비교
|JoinPoint|Spring AOP|AspectJ|
|:------:|:-----:|:-----:|
|메서드 호출|X|O|
|메서드 실행|O|O|
|생성자 호출|X|O|
|생성자 실행|X|O|
|Static 초기화 실행|X|O|
|객체 초기화|X|O|
|필드 참조|X|O|
|필드 값 변경|X|O|
|핸들러 실행|X|O|
|Advice 실행|X|O|
