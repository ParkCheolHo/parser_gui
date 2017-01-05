# Naver Movie crawler 

목적 
영화 홈페이지 제작을 위한 네이버영화 홈페이지에서 영화 데이터 파싱하는 프로그램
영화 홈페이지 개발(https://github.com/ParkCheolHo/movie_information_by_Porster) 에 필요한 데이터를 축척하기 위해 만든 크롤러 입니다.


### 개발환경
  * java8
  * mysql 5.7.12
### 사용된 라이브러리
  * jsoup (https://jsoup.org/)
### 기능목록 
   * 년도별 영화 데이터 xml/mysql 저장
   * 영화별 포스터 저장
   * 영화별 영화인물 포스터와 데이터 저장


### 기본화면 
javafx를 이용하여 유저 인터페이스를 구현하였습니다.

![Alt text](https://raw.githubusercontent.com/ParkCheolHo/parser_gui/master/img/screenshot/1.png)
<p style="text-align:center">그림1. 메인화면</p>

![Alt text](https://raw.githubusercontent.com/ParkCheolHo/parser_gui/master/img/screenshot/2.png)
<p style="text-align:center">그림2. 메인 세팅 화면</p>

![Alt text](https://raw.githubusercontent.com/ParkCheolHo/parser_gui/master/img/screenshot/3.png)
<p style="text-align:center">그림3. 데이터베이스 세팅 화면</p>

![board content](https://raw.githubusercontent.com/ParkCheolHo/parser_gui/master/img/screenshot/4.png)
<p style="text-align:center">그림4. 포스터 저장 세팅 화면</p>


### MySql을 사용하여 데이터 베이스 저장 시 필요한 테이블 생성 sql
옵션 중에서 mysql 저장 옵션을 활성 되었을때 아래의 테이블이 반드시 있어야 저장이 가능합니다.
```sql
CREATE TABLE `movies` (
   `code` int(11) NOT NULL,
   `name` varchar(90) DEFAULT NULL,
   `engname` varchar(90) DEFAULT NULL,
   `contry_id` int(11) DEFAULT NULL,
   `storyname` varchar(200) DEFAULT NULL,
   `story` varchar(3000) DEFAULT NULL,
   `year` int(11) DEFAULT NULL,
   `grade` int(11) DEFAULT NULL,
   PRIMARY KEY (`code`)
 )
 ```
 쿼리1 영화데이터가 저장 되는 테이블
```sql
 CREATE TABLE `relrationship_actor` (
   `movie_index` int(11) NOT NULL,
   `actors_index` int(11) NOT NULL,
   `option` int(11) NOT NULL,
   PRIMARY KEY (`actors_index`,`movie_index`,`option`)   
 ) 
 ```
 쿼리2 출연 테이블

 ```sql
 CREATE TABLE `relrationship_genre` (
   `movie_index` int(11) NOT NULL,
   `genre_index` int(11) NOT NULL,
   PRIMARY KEY (`movie_index`,`genre_index`)
 ) 
 ```
 쿼리3 장르 관계 테이블

 ```sql 
CREATE TABLE `actors` (
   `code` int(11) NOT NULL,
   `name` varchar(70) DEFAULT NULL,
   PRIMARY KEY (`code`)
 ) 
 ```
쿼리4 영화인물 테이블

### 클래스 다이어 그램
![board content](https://raw.githubusercontent.com/ParkCheolHo/parser_gui/master/img/screenshot/diagram.png)

### 개발 후기
처음 자바로 만든 윈도우 프로그램을 만들어 봤습니다. javafx를 이용하여 프로그램을 만들면서 많은 공부를 하게 된것 같습니다. 개인적으로 mvc 패턴을 이용하여 개발을 하는것이 
새로 웠고 재미있었습니다. 비록 일반 사용자에게 배포하기 에는 무리가 있지만 리팩토링을 하면 일반배포도 가능할것 같습니다.
