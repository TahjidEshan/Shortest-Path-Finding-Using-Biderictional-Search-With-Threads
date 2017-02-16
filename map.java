import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class map implements Runnable {

    private int city, road;
    private String source, destination;
    private String map[][];
    private ArrayList<String> cities;
    private Queue<String> queueForward;
    private Queue<Node> nodeQueueForward;
    private ArrayList<String> visitedForward;
    private Queue<String> queueBackward;
    private Queue<Node> nodeQueueBackward;
    private ArrayList<String> visitedBackward;
    private ArrayList<Node> nodeListForward;
    private ArrayList<Node> nodeListBackward;
    private final Lock lock;

    public map() {
        Scanner s = new Scanner(System.in);
        city = s.nextInt();
        road = s.nextInt();
        s.nextLine();
        source = s.nextLine();
        destination = s.nextLine();
        map = new String[city + 1][city + 1];
        cities = new ArrayList<>();
        queueForward = new LinkedList<>();
        nodeQueueForward = new LinkedList<>();
        visitedForward = new ArrayList<>();
        queueBackward = new LinkedList<>();
        nodeQueueBackward = new LinkedList<>();
        visitedBackward = new ArrayList<>();
        nodeListForward = new ArrayList<>();
        nodeListBackward = new ArrayList<>();
        lock = new ReentrantLock();

        for (int i = 0; i < map.length; ++i) {
            Arrays.fill(map[i], "0");
        }

        int count = 1;
        for (int i = 0; i < (road); ++i) {
            StringTokenizer val = new StringTokenizer(s.nextLine(), ",");
            String c1 = val.nextToken();
            String c2 = val.nextToken();
            if (!cities.contains(c1)) {
                cities.add(c1);
                map[count][0] = c1;
                map[0][count] = c1;
                ++count;
            }
            if (!cities.contains(c2)) {
                cities.add(c2);
                map[count][0] = c2;
                map[0][count] = c2;
                ++count;
            }
            addEdge(c1, c2);
            addEdge(c2, c1);

        }
        Node n1 = new Node(source, 0, null);
        Node n2 = new Node(destination, 0, null);
        queueForward.add(source);
        visitedForward.add(source);
        nodeQueueForward.add(n1);
        nodeListForward.add(n1);
        queueBackward.add(destination);
        visitedBackward.add(destination);
        nodeQueueBackward.add(n2);
        nodeListBackward.add(n2);

    }

    public void run() {
        solve();
    }

    public void solve() {
        if (Thread.currentThread().getName().equals("Forward")) {
            Node start, current, previous;
            current = new Node(null, 0, null);
            start = new Node(null, 0, null);
            while (!queueForward.isEmpty()) {
                String temp = queueForward.remove();
                current = nodeQueueForward.remove();
                previous = current.previous;
                if (visitedBackward.contains(temp)) {
                    try {
                        if (lock.tryLock()) {
                            lock.lock();
                            //System.out.println("Here "+ temp);
                            Node temp1 = current.previous;
                            //System.out.println(current.name);
                            int length = 0;
                            ArrayList<String> list = new ArrayList<>();
                            while (!temp1.equals(start)) {
                                list.add(temp1.name);
                                temp1 = temp1.previous;
                            }
                            list.add(source);
                            Collections.reverse(list);
                            length = list.size();
                            if (!list.contains(destination)) {
                                Node temp2 = nodeListBackward.get(getIndex(nodeListBackward, temp));
                                // System.out.println(temp2.name+" found");
                                while (!temp2.name.equals(destination)) {
                                    list.add(temp2.name);
                                    temp2 = temp2.previous;
                                }
                                list.add(destination);

                            }
                            System.out.printf("Route :" + list + "\n" + "Length :" + (list.size() - 1) + "\n" + "Direction :" + Thread.currentThread().getName() + "\n" + "City :" + temp + "\n" + "#Roads :" + length + "\n");
                        }
                        lock.unlock();
                        System.exit(0);

                    } catch (Exception e) {

                    }
                }

                if (temp.equals(source)) {
                    start = current;
                    previous = start;
                }

                if (!temp.equals(destination)) {
                    for (int i = 0; i < map.length; ++i) {
                        if (map[0][i].equals(temp)) {
                            for (int j = 0; j < map[0].length; ++j) {
                                if (map[i][j].equals("1")) {
                                    if (!visitedForward.contains(map[j][0])) {
                                        queueForward.add(map[j][0]);
                                        visitedForward.add(map[j][0]);
                                        // System.out.println(visitedForward);
                                        Node n = new Node(map[j][0], current.cost + 1, current);
                                        nodeQueueForward.add(n);
                                        nodeListForward.add(n);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (Thread.currentThread().getName().equals("Backward")) {
            //System.out.println(Thread.currentThread().getName());
            Node start, current, previous;
            current = new Node(null, 0, null);
            start = new Node(null, 0, null);
            while (!queueBackward.isEmpty()) {
                String temp = queueBackward.remove();
                current = nodeQueueBackward.remove();
                previous = current.previous;
                if (visitedForward.contains(temp)) {
                    try {
                        if (lock.tryLock()) {
                            lock.lock();
                            //System.out.println("Here "+ temp);
                            Node temp1 = current.previous;
                            //System.out.println(current.name);
                            int length = 0;
                            ArrayList<String> list = new ArrayList<>();
                            while (!temp1.equals(start)) {
                                list.add(temp1.name);
                                temp1 = temp1.previous;
                            }
                            list.add(destination);
                            Collections.reverse(list);
                            length = list.size();
                            if (!list.contains(source)) {
                                Node temp2 = nodeListBackward.get(getIndex(nodeListBackward, temp));
                                // System.out.println(temp2.name+" found");
                                while (!temp2.name.equals(source)) {
                                    list.add(temp2.name);
                                    temp2 = temp2.previous;
                                }
                                list.add(source);
                                Collections.reverse(list);

                            }
                            System.out.printf("Route :" + list + "\n" + "Length :" + (list.size() - 1) + "\n" + "Direction :" + Thread.currentThread().getName() + "\n" + "City :" + temp + "\n" + "#Roads :" + length + "\n");
                        }
                        lock.unlock();
                        System.exit(0);

                    } catch (Exception e) {

                    }
                }

                if (temp.equals(destination)) {
                    start = current;
                    previous = start;
                }
                if (!temp.equals(source)) {
                    for (int i = 0; i < map.length; ++i) {
                        if (map[0][i].equals(temp)) {
                            for (int j = 0; j < map[0].length; ++j) {
                                if (map[i][j].equals("1")) {
                                    //System.out.println(map[j][0]);
                                    if (!visitedBackward.contains(map[j][0])) {
                                        queueBackward.add(map[j][0]);
                                        visitedBackward.add(map[j][0]);
                                        Node n = new Node(map[j][0], current.cost + 1, current);
                                        nodeQueueBackward.add(n);
                                        nodeListBackward.add(n);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void addEdge(String c1, String c2) {
        for (int i = 0; i < map.length; ++i) {
            if (map[i][0].equals(c1)) {
                for (int j = 1; j < map[0].length; ++j) {
                    if (map[0][j].equals(c2)) {
                        map[i][j] = "1";
                    }
                }
            }
        }
    }

    public int getIndex(ArrayList<Node> list, String str) {
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i).name.equals(str)) {
                return i;
            }
        }
        return 0;
    }

    public void printMap() {
        for (int i = 0; i < map.length; ++i) {
            for (int j = 0; j < map.length; ++j) {
                System.out.print(map[i][j] + "    ");
            }
            System.out.printf("\n");
        }

    }

}
