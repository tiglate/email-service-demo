package ludo.mentis.aciem.auctoritas.dev;

public interface DataLoaderCommand {

    int getOrder();
    String getName();
    boolean canItRun();
    int run();
}
