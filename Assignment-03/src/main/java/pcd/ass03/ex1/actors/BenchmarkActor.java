package pcd.ass03.ex1.actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import pcd.ass03.ex1.actors.msg.NewBoard;
import pcd.ass03.ex1.actors.msg.Start;
import pcd.ass03.ex1.actors.msg.Stop;

public class BenchmarkActor extends AbstractLoggingActor {

    private final int numberOfWorkers;
    private ActorRef boardUpdater;

    public BenchmarkActor(int numberOfWorkers) {
        this.numberOfWorkers = numberOfWorkers;
    }

    public static Props props(int numberOfWorkers) {
        return Props.create(BenchmarkActor.class, numberOfWorkers);
    }

    @Override
    public void preStart() throws Exception {
        boardUpdater = getContext().actorOf(BoardUpdater.props(numberOfWorkers), "boardUpdater");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(Stop.class, stopMsg -> {
            log().info(getSelf().path().name() + " Stop");
            boardUpdater.tell(new Stop(), getSelf());
            context().stop(getSelf());
        }).match(Start.class, startMsg -> {
            log().info(getSelf().path().name() + " Start");
            this.boardUpdater.tell(startMsg, getSelf());
        }).match(NewBoard.class, newBoard -> {
            log().info(getSelf().path().name() + " newBoard");
            getSelf().tell(new Stop(), getSelf());
        }).build();
    }

}
