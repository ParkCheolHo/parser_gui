package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*

1. 지정된 Application 클래스의 인스턴스를 생성한다.
2. init() 메소드 실행
3. start() 메소드 실행
4. 다음중 하나의 현상이 발생하면 어플리케이션이 종료된다.
    애플리케이션에서 Platform.exit()을 콜할때
    마지막 윈도우가 꺼지고 impliciteExit 어트리뷰트가 true일때
5. stop() 메소드 실행

The entry point for JavaFX applications is the Application class. he JavaFX runtime does the following, in order, whenever an application is launched:

1.Constructs an instance of the specified Application class
2.Calls the init() method
3.Calls the start(javafx.stage.Stage) method
4.Waits for the application to finish, which happens when either of the following occur:
    the application calls Platform.exit()
    the last window has been closed and the implicitExit attribute on Platform is true
5.Calls the stop() method

Parent : The base class for all nodes that have children in the scene graph;
scene : The JavaFX Scene class is the container for all content in a scene graph.
stage : The JavaFX Stage class is the top level JavaFX container. The primary Stage is constructed by the platform. Additional Stage objects may be constructed by the application.

scene graph 설명
http://docs.oracle.com/javafx/2/scenegraph/jfxpub-scenegraph.htm

Parent : scene graph 에 있는 모든 자식 노드들의 베이스 클래스
scene : scene graph 의 모든 데이터? 콘텐츠? 를 담는 컨테이너
stage : 하나의 윈도우. 제일 상위 레벨의 javafx 컨테이너.
*/

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/main.fxml"));
        primaryStage.setTitle("네이버영화 크롤러");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(t -> {
                Platform.exit();
                System.exit(0);
            });
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
