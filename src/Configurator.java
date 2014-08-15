import java.util.ArrayList;
import java.util.HashMap;


public class Configurator {

  public static ArrayList<String> partitions = new ArrayList<String>();
  public static ArrayList<String> replicas = new ArrayList<String>();
  public static HashMap<String, ArrayList<String>> partitionToReplicaMap = new HashMap<String, ArrayList<String>>();

  public static int totalBlobSize = 1000;
  public static int replicaSize = 3;
  public static int partitionSize = 3;
  public static final String partitionPrefix = "P";
  public static final String replicaPrefix = "R";

  public static void initiate() {

    for (int i = 0; i < partitionSize; i++) {
      String replica = replicaPrefix + "_" + i;
      replicas.add(replica);
    }

    for (int i = 0; i < partitionSize; i++) {
      String partition = partitionPrefix + "_" + i;
      partitions.add(partition);
      ArrayList<String> localReplicas = new ArrayList<String>();
      for (String replica : replicas) {
        localReplicas.add(partition + ":" + replica);
      }
      partitionToReplicaMap.put(partition, localReplicas);
    }
  }
}
