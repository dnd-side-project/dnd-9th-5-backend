# 🪄 DND 9th OZ BackEnd

## 🛠 Tech Stack

- Language
    - Java 11
- Framework
    - Spring Boot 2.7
- DB
    - Spring JPA
    - MySQL
    - Redis
- Infra
    - AWS(EC2, S3, RDS)
- CI/CD
    - Docker
    - Jenkins

<br>

## 🔖 Commit Convention

- `feat` : 새로운 기능 추가
- `fix` : 버그 수정
- `design` : UI만 수정하는 경우
- `refactor` : 코드 리팩토링
- `test` : 테스트 코드를 추가하는 경우
- `add` : 이미지, 폰트, 에셋, 더미데이터 등 추가
- `set` : 패키지 설치 및 제거
- `docs` : 문서 수정 (ex.README)
- `chore` : 기타

<br>

## 🌲 Project Structure
```bash
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── dndoz
    │   │           └── PosePicker
    │   │               ├── PosePickerApplication.java
    │   │               ├── config
    │   │               ├── controller
    │   │               ├── dao
    │   │               ├── domain
    │   │               ├── exception
    │   │               └── service
    └───└── resources
            └── application.properties
```
