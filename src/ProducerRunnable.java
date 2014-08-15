import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class ProducerRunnable implements Runnable {

  private ArrayList<String> partitions;
  private int partitionSize;
  private HashMap<String, ArrayList<String>> partitionsToReplicaMap;
  private ArrayList<Integer> blobIdList;
  private HashMap<Integer, ArrayList<String>> blobIdToReplicaMap;
  private Random random;

  public ProducerRunnable(ArrayList<String> partitions, HashMap<String, ArrayList<String>> partitionsToReplicaMap,
      int startBlobId, int endBlobId) {
    System.out.println(partitions + ", " + partitionsToReplicaMap + " " + startBlobId + " " + endBlobId);
    this.partitions = partitions;
    this.partitionSize = partitions.size();
    this.partitionsToReplicaMap = partitionsToReplicaMap;
    blobIdToReplicaMap = new HashMap<Integer, ArrayList<String>>();
    random = new Random(endBlobId - startBlobId);
    blobIdList = new ArrayList<Integer>();
    populateBlobIds(startBlobId, endBlobId);
  }

  private void populateBlobIds(int start, int end) {
    for (int blobId = start; blobId < end + 1; blobId++) {
      System.out.println("Adding blob to list " + blobId);
      blobIdList.add(blobId);
    }
  }

  public void run() {
    try {
      while (!blobIdList.isEmpty()) {
        Integer blobId = getRandomBlob();
        String replica = getRandomReplica(blobId);
        System.out
            .println(blobId + " " + replica + "  of " + Configurator.partitionPrefix + "_" + blobId % partitionSize);
        if (blobIdToReplicaMap.get(blobId).size() == 1) {
          blobIdToReplicaMap.remove(blobId);
          blobIdList.remove(blobId);
        } else {
          blobIdToReplicaMap.get(blobId).remove(replica);
        }
        Thread.sleep(1000);
      }
    } catch (Exception e) {
      System.out.println("Exception thrown during iteration ");
    }
  }

  private Integer getRandomBlob() {
    Integer toRet = blobIdList.get(random.nextInt(blobIdList.size()));
    if (!blobIdToReplicaMap.containsKey(toRet)) {
      String partition = Configurator.partitionPrefix + "_" + toRet.intValue() % partitionSize;
      blobIdToReplicaMap.put(toRet, partitionsToReplicaMap.get(partition));
    }
    return toRet;
  }

  private String getRandomReplica(Integer blobId) {
    int randomRep = random.nextInt(blobIdToReplicaMap.get(blobId).size());
    return blobIdToReplicaMap.get(blobId).get(randomRep);
  }
}
