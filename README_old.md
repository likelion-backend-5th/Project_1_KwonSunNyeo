# LIKELION-MiniProject-1st

---
## ♻️멋사마켓♻️️
<img src="https://upload.wikimedia.org/wikipedia/commons/4/48/LIKELION_brandsymbol.png" width="200" alt="symbol">

> **개인 미니 프로젝트** <br/> **제작자: 권선녀** [@sssssn](https://github.com/sssssn) <br/> **제작 기간: 2023.06.29 ~ 2023.07.05**

## 프로젝트 소개 🦁
오늘날 많이 사용하고 있는 당근마켓, 중고나라 등을 착안하여 **본인만의 중고 제품 거래 플랫폼의 백엔드**를 만들어보는 미니 프로젝트입니다. </br>
1. 사용자가 중고 물품을 자유롭게 올리고
2. 댓글을 통해 소통하며
3. 최종적으로 구매 제안에 대하여 수락할 수 있는 형태

---
## 시작 가이드 🌈
### Environments
For building and running the project you need:

- IntelliJ Ultimate
- Spring Boot 3.1.1
  - Build Tool: Gradle
  - Dependencies:
    - Spring Boot DevTools
    - Lombok
    - Spring Web
    - Spring Data JPA

---
## Stacks 👩🏻‍💻
### Environment
![Github](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=GitHub&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ%20Idea-000000?style=for-the-badge&logo=IntelliJ%20IDEA&logoColor=white)
![Visual Studio Code](https://img.shields.io/badge/Visual%20Studio%20Code-007ACC?style=for-the-badge&logo=Visual%20Studio%20Code&logoColor=white)

### Development
![java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=Java&logoColor=white)
![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=Spring%20Boot&logoColor=white)
![SQLite](https://img.shields.io/badge/SQLite-003B57?style=for-the-badge&logo=SQLite&logoColor=white)

### Communication
![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white)
![Discord](https://img.shields.io/badge/Discord-5865F2?style=for-the-badge&logo=Discord&logoColor=white)

---
## 주요 기능 📝

### 1️⃣ 물품 등록 (Item)
- [중고 거래](https://github.com/likelion-backend-5th/MiniProject_Basic_KwonSunNyeo/commits/day1)를 목적으로 물품에 대한 정보 등록
- 해당 정보는 누구나 조회 가능
- 해당 정보는 수정 및 삭제 그리고 이미지 첨부 가능

### 2️⃣ 댓글 등록 (Comment)
- [등록된 물품에 대한 질문](https://github.com/likelion-backend-5th/MiniProject_Basic_KwonSunNyeo/commits/day2)을 목적으로 댓글 등록
- 해당 댓글은 누구나 조회 가능
- 해당 댓글은 수정 및 삭제 가능
- 해당 댓글의 답글 등록 및 수정 가능

### 3️⃣ 구매 제안 (Proposal)
- [등록된 물품에 대한 구매 제안](https://github.com/likelion-backend-5th/MiniProject_Basic_KwonSunNyeo/commits/day3)을 목적으로 제안 등록
- 해당 제안은 물품의 주인과 제안을 등록한 사용자만 가능
- 해당 제안은 수정 및 삭제 가능
- 대상 물품의 주인은 구매 제안을 수락 및 거절 가능
- 해당 제안을 등록한 사용자는 수락 상태인 경우 구매 확정 가능
- [가독성을 높이기 위한 리팩토링](https://github.com/likelion-backend-5th/MiniProject_Basic_KwonSunNyeo/commits/refactor)

---
## Architecture 📦

### 디렉토리 구조
<details>
<summary>여기를 눌러주세요!🌱</summary>

```bash
📦market
 ┣ 📂gradle
 ┃ ┗ 📂wrapper
 ┃ ┃ ┣ 📜gradle-wrapper.jar
 ┃ ┃ ┗ 📜gradle-wrapper.properties
 ┣ 📂src
 ┃ ┣ 📂main
 ┃ ┃ ┣ 📂java
 ┃ ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┃ ┗ 📂likelion
 ┃ ┃ ┃ ┃ ┃ ┗ 📂market
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ItemController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CommentController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ProposalController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ItemDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MessageResponseDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ResponseDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CommentDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ProposalDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CommentPageDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ItemPageDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ItemReadDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ProposalPageDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ItemEntity.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ItemStatus.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CommentEntity.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ProposalEntity.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ProposalStatus.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ItemRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CommentRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ProposalRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ItemService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CommentService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ProposalService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜MarketApplication.java
 ┃ ┃ ┣ 📂resources
 ┃ ┃ ┃ ┣ 📂static
 ┃ ┃ ┃ ┣ 📂templates
 ┃ ┃ ┃ ┗ 📜application.yaml
 ┃ ┃ ┗ 📂generated
 ┃ ┗ 📂test
 ┃ ┃ ┗ 📂java
 ┃ ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┃ ┗ 📂likelion
 ┃ ┃ ┃ ┃ ┃ ┗ 📂market
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜MarketApplicationTests.java
 ┣ 📂image
 ┃ ┗ 📂1
 ┃ ┃ ┗ 📜image.png
 ┣ 📂postman
 ┃ ┣ 📂day1
 ┃ ┃ ┣ 📜day1_delete.png
 ┃ ┃ ┣ 📜day1_delete_error.png
 ┃ ┃ ┣ 📜day1_delete_error_.png
 ┃ ┃ ┣ 📜day1_get.png
 ┃ ┃ ┣ 📜day1_get_page.png
 ┃ ┃ ┣ 📜day1_post.png
 ┃ ┃ ┣ 📜day1_put.png
 ┃ ┃ ┣ 📜day1_put_error.png
 ┃ ┃ ┣ 📜day1_put_image.png
 ┃ ┃ ┗ 📜day1_put_image_error.png
 ┃ ┣ 📂day2
 ┃ ┃ ┣ 📜day2_delete.png
 ┃ ┃ ┣ 📜day2_delete_error.png
 ┃ ┃ ┣ 📜day2_delete_error_.png
 ┃ ┃ ┣ 📜day2_get.png
 ┃ ┃ ┣ 📜day2_post.png
 ┃ ┃ ┣ 📜day2_put.png
 ┃ ┃ ┣ 📜day2_put_error.png
 ┃ ┃ ┣ 📜day2_put_reply.png
 ┃ ┃ ┗ 📜day2_put_reply_error.png
 ┃ ┣ 📂day3
 ┃ ┃ ┣ 📜day3_delete.png
 ┃ ┃ ┣ 📜day3_delete_error.png
 ┃ ┃ ┣ 📜day3_delete_error_.png
 ┃ ┃ ┣ 📜day3_get.png
 ┃ ┃ ┣ 📜day3_get_status.png
 ┃ ┃ ┣ 📜day3_post.png
 ┃ ┃ ┣ 📜day3_put_confirm.png
 ┃ ┃ ┣ 📜day3_put_confirm_error.png
 ┃ ┃ ┣ 📜day3_put_confirm_error_.png
 ┃ ┃ ┣ 📜day3_put_confirm_error_1.png
 ┃ ┃ ┣ 📜day3_put_price.png
 ┃ ┃ ┣ 📜day3_put_price_error.png
 ┃ ┃ ┣ 📜day3_put_price_error_.png
 ┃ ┃ ┣ 📜day3_put_status.png
 ┃ ┃ ┣ 📜day3_put_status_error.png
 ┃ ┃ ┗ 📜day3_put_status_error_.png
 ┃ ┗ 📜0629-0705 project.postman_collection.json
 ┣ 📜.gitignore
 ┣ 📜build.gradle
 ┣ 📜gradlew
 ┣ 📜gradlew.bat
 ┣ 📜HELP.md
 ┣ 📜settings.gradle
 ┣ 📜db.sqlite
 ┗ 📜README_old.md
```
</details>
