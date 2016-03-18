package sample.model;

/**
 * Created by ParkCheolHo on 2016-03-11.
 * 사용자 정의 exception 생성 클래스
 */
public class MyException extends Exception{

    public MyException(){
        super("사용자 정의 예외 발생");
    }
}
