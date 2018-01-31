package by.expertsoft.myrunnable;


public interface ThrowingRunnable extends Runnable {

    @Override
    default void run(){
        try{
            runThrows();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    void runThrows() throws Exception;

}
