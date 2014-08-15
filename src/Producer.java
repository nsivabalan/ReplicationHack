import java.util.ArrayList;


public class Producer {

  private int numberOfThreads;
  private int blobCount;
  private ArrayList<Thread> producerRunnables;
  private int blobsPerThread;

  public Producer(int numberOfThreads, int blobCount) {
    this.numberOfThreads = numberOfThreads;
    this.blobCount = blobCount;
    producerRunnables = new ArrayList<Thread>();
    blobsPerThread = blobCount / numberOfThreads;
  }

  public void start() {

    try {
      for (int i = 0; i < numberOfThreads; i++) {
        ProducerRunnable producerRunnable =
            new ProducerRunnable(Configurator.partitions, Configurator.partitionToReplicaMap, (i * blobsPerThread) + 1,
                (i + 1) * blobsPerThread);
        Thread producerThread = new Thread(producerRunnable);
        producerRunnables.add(producerThread);
        producerThread.start();
      }

      for (Thread thread : producerRunnables) {
        thread.join();
      }
    } catch (Exception e) {
      System.out.println("Exception thrown in Producer");
    }
  }

  public static void main(String args[]) {
    Configurator.initiate();
    Producer producer = new Producer(1, 3);
    producer.start();
  }
}



