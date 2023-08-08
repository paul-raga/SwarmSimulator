import it.unicam.cs.ragazzinipaul.api.io.RobotCreator;
import it.unicam.cs.ragazzinipaul.api.io.ShapeCreator;
import it.unicam.cs.ragazzinipaul.api.io.SwarmController;
import it.unicam.cs.ragazzinipaul.api.model.dynamics.BufferedCommands;
import it.unicam.cs.ragazzinipaul.api.model.dynamics.CommandsHandler;
import it.unicam.cs.ragazzinipaul.api.model.dynamics.InstructionsQueue;
import it.unicam.cs.ragazzinipaul.api.model.dynamics.ThreadedRobotMovement;
import it.unicam.cs.ragazzinipaul.api.model.objects.Circle;
import it.unicam.cs.followme.utilities.FollowMeParserException;
import it.unicam.cs.ragazzinipaul.api.model.objects.Environment;
import it.unicam.cs.ragazzinipaul.api.model.objects.Rectangle;
import it.unicam.cs.ragazzinipaul.api.model.objects.Robot;
import it.unicam.cs.followme.utilities.FollowMeParser;
import it.unicam.cs.ragazzinipaul.api.utils.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ObjectsTest {

    @Test void robotShouldExist(){
        Environment environment= new Environment();
        RobotCreator robotCreator=new RobotCreator<>(environment);
        robotCreator.create("bob");
        assertTrue( !environment.getRoboList().isEmpty());
    }

    @Test void robotPositionShouldBeInRange(){
        Environment environment= new Environment();
        RobotCreator robotCreator=new RobotCreator<>(environment);
        robotCreator.create("bob");
        assertTrue( environment.getRobotPosition((Robot) environment.getRoboList().get(0)).getX() <=10 && environment.getRobotPosition((Robot) environment.getRoboList().get(0)).getY() <=10);
        assertTrue(environment.getRobotPosition((Robot) environment.getRoboList().get(0)).getX() >=0 && environment.getRobotPosition((Robot) environment.getRoboList().get(0)).getY() >=0);
    }

    @Test void shapeShouldExist() throws FollowMeParserException, InterruptedException {
        Environment environment= new Environment();
        ShapeCreator shapeCreator=new ShapeCreator<>(environment);
        shapeCreator.parseAndCreate("lable CIRCLE 10 10 10");
        assertTrue(!environment.getShapesList().isEmpty());

    }

    @Test void shapeShouldCorrespond() throws FollowMeParserException, InterruptedException {
        Rectangle rectangle=new Rectangle<>(10,10,10,10,"LABEL");

        Environment environment= new Environment();
        ShapeCreator shapeCreator=new ShapeCreator<>(environment);
        shapeCreator.parseAndCreate("label RECTANGLE 10 10 10 10");
        assertTrue(environment.getShape(10,10).getArea() == rectangle.getArea());
        assertTrue(environment.getShape(10,10).getLabel().equals(rectangle.getLabel()));
    }

    @Test void shapesShouldHaveExpectedClass() throws FollowMeParserException, InterruptedException {
        Environment environment= new Environment();
        ShapeCreator shapeCreator=new ShapeCreator<>(environment);
        shapeCreator.parseAndCreate("lable RECTANGLE 10 10 10 10\n" +
                "lable CIRCLE 20 20 10\n");
        assertEquals(environment.getShape(10,10).getClass(),new Rectangle<>(5,5,5,5,5).getClass());
        assertEquals(environment.getShape(20,20).getClass(),new Circle<>(6,6,6,"lable").getClass());
    }

    @Test void robotShouldBeSignaling() throws Exception {
        Environment environment= new Environment();

       CommandsHandler handler=new CommandsHandler<>(environment);
       FollowMeParser parser=new FollowMeParser(handler);


        Robot robot=new Robot<>("bob");
        environment.addRobots(robot,new Position(10,10));

        SwarmController controller=new SwarmController<>(parser,false);
        controller.runCommandSet("SIGNAL label");

        assertTrue(robot.getIfSignaling());
    }

    @Test void robotShouldBeSingnalingLable() throws Exception {
        Environment environment= new Environment();

        CommandsHandler handler=new CommandsHandler<>(environment);
        FollowMeParser parser=new FollowMeParser(handler);


        Robot robot=new Robot<>("bob");
        environment.addRobots(robot,new Position(10,10));

        SwarmController controller=new SwarmController<>(parser,false);
        controller.runCommandSet("SIGNAL THING");

        assertTrue(robot.getSinal().equals("THING"));
    }

    @Test void robotShouldStopSignaling() throws Exception {
        Environment environment= new Environment();

        CommandsHandler handler=new CommandsHandler<>(environment);
        FollowMeParser parser=new FollowMeParser(handler);


        Robot robot=new Robot<>("bob");
        environment.addRobots(robot,new Position(10,10));

        SwarmController controller=new SwarmController<>(parser,false);
        controller.runCommandSet("SIGNAL THING");
        Thread.sleep(100);
        controller.runCommandSet("UNSIGNAL THING");
        assertTrue(robot.getIfSignaling() == false);
    }

    @Test void InstructionsQueueShouldBeEmpty(){
        InstructionsQueue queue= new InstructionsQueue();
        queue.enqueue(new double[0]);
        queue.enqueue(new double[0]);
        queue.dequeue();
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }

    @Test void ThreadNameShouldCorrespond(){
        Environment env = new Environment();
        RobotCreator creator= new RobotCreator<>(env);
        creator.create("bob");
        ThreadedRobotMovement thread= new ThreadedRobotMovement<>((Robot)env.getRoboList().get(0),env,1,1,1);
        assertTrue("bob".equals( thread.getThreadName()));
    }


    @Test void robotShouldHaveMoved() throws Exception {
        Environment environment= new Environment();

        CommandsHandler handler=new CommandsHandler<>(environment);
        FollowMeParser parser=new FollowMeParser(handler);


        Robot robot=new Robot<>("bob");
        Position position=new Position(10,10);
        environment.addRobots(robot,new Position(10,10));

        SwarmController controller=new SwarmController<>(parser,false);
        controller.runCommandSet("MOVE 1 1 10");
        Thread.sleep(1000);
        controller.runCommandSet("STOP");
        assertTrue(environment.getRobotPosition(robot).getX() > position.getX());
        assertTrue(environment.getRobotPosition(robot).getY() > position.getY());
    }

    @Test void robotShouldBeThere() throws Exception {
        Environment environment= new Environment();

        CommandsHandler handler=new CommandsHandler<>(environment);
        FollowMeParser parser=new FollowMeParser(handler);


        Robot robot=new Robot<>("bob");
        environment.addRobots(robot,new Position(10,10));

        SwarmController controller=new SwarmController<>(parser,false);
        controller.runCommandSet("MOVE 1 0 10");
        Thread.sleep(1000);
        controller.runCommandSet("STOP");
        assertTrue(environment.getRobotPosition(robot).getX() > 19.5); //inserisco un certo errore di misura
        assertTrue(environment.getRobotPosition(robot).getY() == 10);
    }

    @Test void robotShouldHaveStopped() throws Exception {
        Environment environment= new Environment();

        CommandsHandler handler=new CommandsHandler<>(environment);
        FollowMeParser parser=new FollowMeParser(handler);


        Robot robot=new Robot<>("bob");
        environment.addRobots(robot,new Position(10,10));

        SwarmController controller=new SwarmController<>(parser,false);
        controller.runCommandSet("MOVE 1 0 10\n" +
                "CONTINUE 2\n");
        Thread.sleep(3500);
        Position finalPosition=environment.getRobotPosition(robot);

        assertTrue(finalPosition == ( environment.getRobotPosition(robot)));
    }

    @Test void executingThreadsShouldCorrespond() throws Exception {
        Environment environment= new Environment();

        CommandsHandler handler=new CommandsHandler<>(environment);
        FollowMeParser parser=new FollowMeParser(handler);

        Robot robot1=new Robot<>("mike");
        Robot robot2=new Robot<>("bob");
        environment.addRobots(robot1,new Position(10,10));
        environment.addRobots(robot2,new Position(1,1));

        SwarmController controller=new SwarmController<>(parser,false);
        controller.runCommandSet("MOVE 1 0 10");
        Thread.sleep(100);
        assertEquals(2,handler.getRunningThreadsNumber());
    }

    @Test void collisionShouldHaveHappened() throws Exception {
        Environment environment= new Environment();

        CommandsHandler handler=new CommandsHandler<>(environment);
        FollowMeParser parser=new FollowMeParser(handler);

        Robot robot1=new Robot<>("mike");
        environment.addRobots(robot1,new Position(10,10));

        ShapeCreator shapeCreator=new ShapeCreator<>(environment);
        shapeCreator.parseAndCreate("lable CIRCLE 20 20 5");

        SwarmController controller=new SwarmController<>(parser,false);
        controller.runCommandSet("MOVE 1 1 10");

        Thread.sleep(2000);
        assertTrue(robot1.getIfSignaling());

    }

    @Test void threadsShouldHaveBeenKilled() throws Exception {
        Environment environment= new Environment();

        CommandsHandler handler=new CommandsHandler<>(environment);
        FollowMeParser parser=new FollowMeParser(handler);

        Robot robot1=new Robot<>("mike");
        environment.addRobots(robot1,new Position(10,10));

        SwarmController controller=new SwarmController<>(parser,false);
        controller.runCommandSet("MOVE 1 1 10\n");
        Thread.sleep(100);
        handler.stopAll();
        Thread.sleep(1000);
        assertEquals(0,handler.getAliveThreads());
    }

    @Test void commandPairShouldCorrespond() throws InterruptedException {
        BufferedCommands buffer= new BufferedCommands();
        buffer.enqueue("comando",new double[0]);
        assertEquals("comando",buffer.dequeue().getCommand());
    }


    @Test void robotShouldBeFollowing() throws Exception {
        Environment environment = new Environment();

        CommandsHandler handler = new CommandsHandler<>(environment);
        FollowMeParser parser = new FollowMeParser(handler);

        Robot robot1 = new Robot<>("mike");
        environment.addRobots(robot1, new Position(100, 100));
        Robot robot2= new Robot("bob");
        environment.addRobots(robot2,new Position(10,10));

        SwarmController controller = new SwarmController<>(parser, false);
        robot1.signal("LABEL");
        controller.runCommandSet("FOLLOW LABEL 500 10");
        Thread.sleep(3000);
        assertTrue(environment.getRobotPosition(robot2).getX()>30 && environment.getRobotPosition(robot2).getY()>30);
    }





}
