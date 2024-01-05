import java.util.*;

public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new TreeMap<>();

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static void main(String[] args) {
        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            String routeString = generateRoute("RLRFR", 100);
            Thread thread = new Thread(() -> {
                long counter = routeString
                        .chars()
                        .filter(w -> w == 'R')
                        .count();
                synchronized (sizeToFreq) {
                    int counter2 = Long.valueOf(counter).intValue();
                    if (sizeToFreq.containsKey(counter2)) {
                        Main.sizeToFreq.replace(counter2, sizeToFreq.get(counter2) + 1);
                    } else {
                        sizeToFreq.put(counter2, 1);
                    }
                }
            });
            thread.start();
            threadList.add(thread);
        }

        Iterator<Thread> iterator = threadList.iterator();
        while (iterator.hasNext()) {
            try {
                iterator.next().join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        Map.Entry<Integer, Integer> max = sizeToFreq.entrySet()
                .stream()
                .max(new Comparator<Map.Entry<Integer, Integer>>() {
                    @Override
                    public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                        return o1.getValue() - o2.getValue();
                    }
                }).get();

        System.out.println("Самое частое количество повторений " + max.getKey() + " (" + "встретилось " + max.getValue() + " раз)"
        );
        System.out.println("другие замеры:");
        for (Map.Entry<Integer, Integer> kv : sizeToFreq.entrySet()) {
            if (!kv.getKey().equals(max.getKey())) {
                System.out.println("- " + kv.getKey() + " (" + kv.getValue() + " раз)");
            }
        }

    }
}